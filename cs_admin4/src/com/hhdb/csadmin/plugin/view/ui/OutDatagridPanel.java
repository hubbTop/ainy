package com.hhdb.csadmin.plugin.view.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Vector;

import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JToolBar;
import javax.swing.table.DefaultTableModel;

import com.hhdb.csadmin.plugin.view.HView;

/**
 * 视图运行结果集
 * @author hhxd
 *
 */
public class OutDatagridPanel extends JPanel implements ActionListener {
	private static final long serialVersionUID = 1L;
	public HView hv;
	
	public JToolBar toolBar = new JToolBar();
	private BaseTable table = new BaseTable();
	private String excutesql = "";
	private BaseButton nextpage = createBtn("下一页",
			IconUtilities.loadIcon("nextpage.png"), "nextpage", "获取下一页");
	private BaseButton allquery = createBtn("所有", 
			IconUtilities.loadIcon("allquery.png"), "queryall", "查询所有");
	private int position = 30;
	private BaseLabel msglabel = new BaseLabel();   //状态条
	private SwingWorker worker;

	public OutDatagridPanel(String sqls,HView hv) {
		this.hv=hv;
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		excutesql = sqls;
		init();
	}

	public void init() {
		toolBar.setFloatable(false);
		toolBar.setLayout(new GridBagLayout());
		toolBar.add(nextpage, new GridBagConstraints(0, 0, 1, 1, 0.0, 1.0,
				GridBagConstraints.NORTH, GridBagConstraints.NONE, new Insets(
						0, 5, 0, 0), 0, 0));
		toolBar.add(allquery, new GridBagConstraints(1, 0, 1, 1, 0.0, 1.0,
				GridBagConstraints.NORTH, GridBagConstraints.NONE, new Insets(
						0, 5, 0, 0), 0, 0));
		toolBar.add(new JPanel(), new GridBagConstraints(3, 0, 1, 1, 1.0, 1.0,
				GridBagConstraints.NORTH, GridBagConstraints.BOTH, new Insets(
						0, 0, 0, 0), 0, 0));
		add(toolBar, BorderLayout.NORTH);

		setBorder(null);
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		table.setRowHeight(22);

		JScrollPane scroll = new JScrollPane(table);
		scroll.getViewport().setBackground(Color.WHITE);
		scroll.setBorder(null);
		add(scroll, BorderLayout.CENTER);

		JPanel status = new JPanel();
		status.setPreferredSize(new Dimension(350, 22));
		status.setLayout(new GridBagLayout());
		status.add(msglabel, new GridBagConstraints(0, 0, 1, 1, 0.0, 1.0,
				GridBagConstraints.NORTH, GridBagConstraints.NONE, new Insets(
						0, 20, 10, 0), 0, 0));
		status.add(new JPanel(), new GridBagConstraints(1, 0, 1, 1, 1.0, 1.0,
				GridBagConstraints.NORTH, GridBagConstraints.BOTH, new Insets(
						0, 0, 0, 0), 0, 0));
		add(status, BorderLayout.SOUTH);
	}

	public BaseTable getResultData() {
		return table;
	}

	private BaseButton createBtn(String text, Icon icon, String comand,
			String prompt) {
		BaseButton basebtn = new BaseButton(text, icon);
		basebtn.setActionCommand(comand);
		basebtn.addActionListener(this);
		basebtn.setToolTipText(prompt);
		return basebtn;
	}

	// 查询数据并装载table
	private void queryData(String sql) throws Exception {
		try {
			long btime = System.currentTimeMillis();
			List<List<Object>> dbtable = hv.sosi.getListList(sql);
			Vector<Object> data = new Vector<Object>();
			Vector<String> colname = new Vector<String>();
			//获取列名
			for (Object field : dbtable.get(0)) {
				colname.add(field.toString());
			}
			sql = sql + ";";
			//获取对应数据
			for (int i = 1; i < dbtable.size(); i++) {
				List<Object> listobj = dbtable.get(i);
				Vector<Object> data1 = new Vector<Object>();
				for (int j = 0; j < listobj.size();j++) {
					data1.add(listobj.get(j));
				}
				
				data.add(data1);
			}
			table.setModel(new DefaultTableModel(data, colname));
			long etime = System.currentTimeMillis();
			msglabel.setText("查询总耗时：" + (etime - btime) + " ms, 检索到: "
					+ (dbtable.size()-1) + " 行");
			if (dbtable.size()-1 < position) {
				nextpage.setEnabled(false);
				allquery.setEnabled(false);
			}
		} catch (Exception e) {
			throw new Exception(e);
		}
	}
	
	/**
	 * 查询
	 * @throws BaseException
	 */
	public void loadData() throws Exception {
		enableQuery();
		String ssql = "";
		String trimExcutesql = excutesql.trim();
		if (trimExcutesql.toUpperCase().startsWith("SELECT") || (trimExcutesql.toUpperCase().startsWith("WITH") && trimExcutesql.toUpperCase().indexOf(" RECURSIVE") != -1)) {
			if (trimExcutesql.endsWith(";")) {
				trimExcutesql = trimExcutesql.substring(0,
						trimExcutesql.lastIndexOf(";"));
			}
			ssql = "select * from (" + trimExcutesql + ") a limit " + position;
		} else {
			ssql = excutesql;
		}
		queryData(ssql);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String actionCmd = e.getActionCommand();
		if ("nextpage".equals(actionCmd)) {    //下一页
			position = (position + 20);
			String ssql = "";
			String trimExcutesql = excutesql.trim();
			if (trimExcutesql.endsWith(";")) {
				trimExcutesql = trimExcutesql.substring(0,trimExcutesql.lastIndexOf(";"));
			}
			if (trimExcutesql.trim().toUpperCase().startsWith("SELECT")) {
				ssql = "select * from (" + trimExcutesql + ") a limit " + position;
			} else {
				ssql = trimExcutesql;
			}
			try {
				queryData(ssql);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		} else if ("queryall".equals(actionCmd)) {  //所有
			worker = new SwingWorker() {
				@Override
				public Object construct() {
					String trimExcutesql = excutesql.trim();
					if (trimExcutesql.endsWith(";")) {
						trimExcutesql = trimExcutesql.substring(0,trimExcutesql.lastIndexOf(";"));
					}
					try {
						queryData(trimExcutesql);
					} catch (Exception e) {
						e.printStackTrace();
					}
					return null;
				}
				@Override
				public void finished() {
					disableQuery();
				}
			};
			worker.start();
		} 
	}


	private void enableQuery() {
		nextpage.setEnabled(true);
		allquery.setEnabled(true);
	}

	private void disableQuery() {
		nextpage.setEnabled(false);
		allquery.setEnabled(false);
	}

}
