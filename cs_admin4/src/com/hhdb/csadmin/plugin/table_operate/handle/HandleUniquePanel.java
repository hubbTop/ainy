package com.hhdb.csadmin.plugin.table_operate.handle;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.DefaultCellEditor;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import com.hhdb.csadmin.common.ui.displayTable.BaseTablePanel;
import com.hhdb.csadmin.common.ui.displayTable.basis.BaseTable;
import com.hhdb.csadmin.common.util.HHSqlUtil.ITEM_TYPE;
import com.hhdb.csadmin.plugin.cmd.console.CommonsHelper;
import com.hhdb.csadmin.plugin.table_operate.TableEditPanel;
import com.hhdb.csadmin.plugin.table_operate.bean.TableUnqiueBean;
import com.hhdb.csadmin.plugin.table_operate.component.CreateTableSQLSyntax;
import com.hhdb.csadmin.plugin.table_operate.component.TextCellEditor;
import com.hhdb.csadmin.plugin.table_operate.component.listcombox.MulitCellEditor;

/**
 * 
 * @Description: 表格唯一键
 * @Copyright: Copyright (c) 2017年10月25日
 * @Company:H2 Technology
 * @author zhipeng.zhang
 * @version 1.0
 */
public class HandleUniquePanel extends JPanel implements TableModelListener, CreateTableSQLSyntax {
	private static final long serialVersionUID = 1L;
	private TableEditPanel tabp;
	
	private BaseTablePanel tablePanel;
	private HandleTablePanel tabPanel;
	private String[] values;
	private BaseTable baseTable;
	private Map<String, TableUnqiueBean> map = new HashMap<String, TableUnqiueBean>();
	private List<String> dels = new ArrayList<String>();
	private JTextField xs;
	private JTextField zs;
	private int prerow = -1;
	private static List<String> lists = new ArrayList<String>();
	static {
		lists.add("名");
		lists.add("栏位");
		lists.add("oid");
		lists.add("comment");
		lists.add("param");
	}

	public HandleUniquePanel(TableEditPanel tableeditpanel,HandleTablePanel tabsPanel) throws Exception {
		this.tabp = tableeditpanel;
		this.tabPanel = tabsPanel;
		setBackground(Color.WHITE);
		tablePanel = new BaseTablePanel(true);
		tablePanel.setPreferredSize(new Dimension(285, 210));
		tablePanel.setBackground(Color.WHITE);
		tablePanel.getViewport().setBackground(Color.WHITE);
		tablePanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
		baseTable = tablePanel.getBaseTable();
		baseTable.setBackground(new Color(220, 255, 220));
		tablePanel.drawAllowed(false);
		editUnique();
		setLayout(new GridBagLayout());
		xs = new JTextField();
		xs.setPreferredSize(new Dimension(200, 20));
		xs.setBackground(new Color(220, 255, 220));
		zs = new JTextField();
		zs.setPreferredSize(new Dimension(300, 20));
		zs.setBackground(new Color(220, 255, 220));
		add(tablePanel, new GridBagConstraints(0, 0, 2, 1, 0.0, 0.0, GridBagConstraints.NORTHWEST,
				GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		add(new JLabel("注释："), new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.NORTHWEST,
				GridBagConstraints.NONE, new Insets(50, 10, 0, 0), 0, 0));
		add(zs, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0, GridBagConstraints.NORTHWEST, GridBagConstraints.NONE,
				new Insets(50, 10, 0, 0), 0, 0));
		JPanel jpl = new JPanel();
		jpl.setBackground(Color.WHITE);
		add(jpl, new GridBagConstraints(0, 3, 2, 1, 1.0, 1.0, GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0), 0, 0));
		tablePanel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				tablePanel.requestFocus();
			}
		});
		baseTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				rowClicked();
			}
		});
	}

	/**
	 * 编辑唯一键界面
	 * 
	 * @throws Exception
	 */
	public void editUnique() throws Exception {
		int rowcount = baseTable.getRowCount();
		for (int i = rowcount - 1; i >= 0; i--) {
			tablePanel.getTableDataModel().removeRow(i);
		}
		// 索引字段查询
		List<Map<String, Object>> lic = tabp.sqls.getListBySql(ITEM_TYPE.TABLE, "columnsinfo", new String[] { tabp.getTableoId() });
		values = new String[lic.size()];
		for (int i = 0; i < lic.size(); i++) {
			Map<String, Object> m = lic.get(i);
			values[i] = m.get("名称").toString();
		}

		// 查询表的索引
		List<Map<String, Object>> li = tabp.sqls.getListBySql(ITEM_TYPE.UNQIUE, "prop_coll", new String[] { tabp.getTableoId() });
		for (int i = 0; i < li.size(); i++) {
			TableUnqiueBean bean = new TableUnqiueBean();
			Map<String, Object> m = li.get(i);
			int relid = Integer.parseInt(m.get("conindid").toString());
			bean.setOid(m.get("oid").toString());
			bean.setConindid(relid);
			bean.setUnqiueName(m.get("名").toString());
			String columns = "";  
			String str = m.get("conkey").toString().replace("{", "").replace("}", "").replace("\"", "");
			int len = 1;
			if(str.contains(",")){
				len = str.split(",").length;
			}
			for (int j = 1; j <= len; j++) {
				columns += tabp.sqls.getIndexColumn(relid, j) + ",";
			}
			m.put("栏位", columns.substring(0, columns.length() - 1));
			bean.setColumns(columns.substring(0, columns.length() - 1));
			if (m.get("comment") != null) {
				bean.setComment(m.get("comment").toString());
			} else {
				bean.setComment("");
			}
			if (m.get("param") != null) {
				bean.setParam(m.get("param").toString());
			} else {
				bean.setParam("");
			}
			map.put(bean.getOid(), bean);
		}
		tablePanel.setData(lists, li);
		initCellEditor();
	}

	/**
	 * 初始化单元格控件
	 */
	public void initCellEditor() {
		TextCellEditor textcell = new TextCellEditor();
		MulitCellEditor mulitcell = new MulitCellEditor(values);
		tablePanel.getTableDataModel().addTableModelListener(this);
		tablePanel.getBaseTable().getColumnModel().getColumn(0).setCellEditor(new DefaultCellEditor(textcell));
		tablePanel.getBaseTable().getColumnModel().getColumn(1).setCellEditor(mulitcell);
		setColumnWidth(new int[] { 1 });
	}

	/**
	 * 设置表格列的宽度
	 * 
	 * @param column
	 * @param table
	 */
	private void setColumnWidth(int[] column) {
		for (int i = 0; i < baseTable.getColumnCount(); i++) {
			TableColumn firsetColumn = baseTable.getColumnModel().getColumn(i);
			firsetColumn.setPreferredWidth(60);
			firsetColumn.setMaxWidth(200);
			if (i == column[0]) {
				firsetColumn.setPreferredWidth(230);
				firsetColumn.setMaxWidth(230);
			}
			firsetColumn.setMinWidth(30);
		}
		hideColumn(new int[] { 2, 3, 4 });
	}
	/**
	 * 隐藏列
	 * @param cols
	 */
	private void hideColumn(int[] cols) {
		for (int c : cols) {
			TableColumn coln = baseTable.getColumnModel().getColumn(c);
			coln.setMinWidth(0);
			coln.setMaxWidth(0);
			coln.setWidth(0);
			coln.setPreferredWidth(0);
		}
	}

	/**
	 * 添加一行
	 */
	public void addRows() {
		Object[] object = new Object[] { null, "" };
		tablePanel.getTableDataModel().addRow(object);
	}

	/**
	 * 删除一行
	 */
	public void delRow() {
		int row = baseTable.getSelectedRow();
		if (row != -1) {
			int result = JOptionPane.showConfirmDialog(null, "是否删除当前行", "提示信息", JOptionPane.YES_NO_OPTION);
			if (result == JOptionPane.YES_OPTION) {
				if (baseTable.getValueAt(baseTable.getSelectedRow(), 2) != null) {
					dels.add(baseTable.getValueAt(baseTable.getSelectedRow(), 0).toString());
				}
				tablePanel.getTableDataModel().removeRow(row);
			}
		}
	}

	/**
	 * 组装编辑SQL
	 * 
	 * @return
	 */
	public String editUniqueSql() {
		StringBuffer sqlBuffer = new StringBuffer();
		StringBuffer str = new StringBuffer();
		StringBuffer comments = new StringBuffer();
		int rows = baseTable.getRowCount();
		if (dels.size() > 0) {
			for (String st : dels) {
				sqlBuffer.append(NEW_LINE).append(ALTER_TABLE).append("\""+tabp.getSchemaName()+"\".\""+tabp.getTableName()+"\"").append(NEW_LINE);
				sqlBuffer.append(DROP_CONSTRAINT).append(st).append(SEMI_COLON);
			}
		}
		for (int i = 0; i < rows; i++) {
			if (baseTable.getValueAt(i, 2) != null) {   //修改
				StringBuffer lsfat = new StringBuffer();
				TableUnqiueBean bean = map.get(baseTable.getValueAt(i, 2).toString());
				if (!bean.getUnqiueName().equals(baseTable.getValueAt(i, 0).toString().trim())
						|| !bean.getColumns().equals(baseTable.getValueAt(i, 1).toString().trim())
						|| !bean.getParam().equals(CommonsHelper.nullOfStr(baseTable.getValueAt(i, 4)))) {
					str.append(NEW_LINE).append(ALTER_TABLE).append("\""+tabp.getSchemaName()+"\".\""+tabp.getTableName()+"\"").append(NEW_LINE);
					str.append(DROP_CONSTRAINT).append(bean.getUnqiueName()).append(COMMA).append(NEW_LINE);
					str.append(ADD_CONSTRAINT).append(baseTable.getValueAt(i, 0));
					str.append(UNIQUE);
					str.append(B_OPEN).append(baseTable.getValueAt(i, 1)).append(B_CLOSE);
					if (!"".equals(CommonsHelper.nullOfStr(baseTable.getValueAt(i, 4)))) {
						str.append(" WITH (fillfactor=" + baseTable.getValueAt(i, 4) + ")");
					}
					str.append(SEMI_COLON);
					lsfat.append(NEW_LINE).append(" COMMENT ON CONSTRAINT ").append(baseTable.getValueAt(i, 0));
					lsfat.append(" ON ").append("\""+tabp.getSchemaName()+"\".\""+tabp.getTableName()+"\"").append(" IS ")
							.append("'" + baseTable.getValueAt(i, 3) + "'").append(SEMI_COLON);
				}
				if (!bean.getComment().equals(CommonsHelper.nullOfStr(baseTable.getValueAt(i, 3))) && lsfat.length() == 0) {
					lsfat.append(NEW_LINE).append(" COMMENT ON CONSTRAINT ").append(baseTable.getValueAt(i, 0));
					lsfat.append(" ON ").append("\""+tabp.getSchemaName()+"\".\""+tabp.getTableName()+"\"").append(" IS ").append("'" + baseTable.getValueAt(i, 3) + "'")
							.append(SEMI_COLON);
				}
				comments.append(lsfat);
			} else {    //添加列
				if (baseTable.getValueAt(i, 0) != null || "".equals(baseTable.getValueAt(i, 0))) {
					str.append(NEW_LINE).append(ALTER_TABLE).append("\""+tabp.getSchemaName()+"\".\""+tabp.getTableName()+"\"");
					str.append(NEW_LINE).append(ADD_CONSTRAINT).append(baseTable.getValueAt(i, 0));
					str.append(UNIQUE);
					str.append(B_OPEN).append(baseTable.getValueAt(i, 1)).append(B_CLOSE);
					if (!"".equals(CommonsHelper.nullOfStr(baseTable.getValueAt(i, 4)))) {
						str.append(" WITH (fillfactor=" + baseTable.getValueAt(i, 4) + ")");
					}
					str.append(SEMI_COLON);
					if (!"".equals(CommonsHelper.nullOfStr(baseTable.getValueAt(i, 3)))) {
						comments.append(NEW_LINE).append(" COMMENT ON CONSTRAINT ").append(baseTable.getValueAt(i, 0));
						comments.append(" ON ").append("\""+tabp.getSchemaName()+"\".\""+tabp.getTableName()+"\"").append(" IS ")
								.append("'" + baseTable.getValueAt(i, 3) + "'").append(SEMI_COLON);
					}
				}
			}
		}
		sqlBuffer.append(str);
		sqlBuffer.append(comments);
		if(sqlBuffer.length()>0){
			return sqlBuffer.append(NEW_LINE).append(NEW_LINE).toString();
		}else{
			sqlBuffer.setLength(0);
			return sqlBuffer.toString();
		}
	}

	@Override
	public void tableChanged(TableModelEvent e) {
		int rows = tabPanel.getBaseTable().getRowCount();
		String columns = "";
		for (int i = 0; i < rows; i++) {
			if (tabPanel.getBaseTable().getValueAt(i, 0) != null) {
				columns += tabPanel.getBaseTable().getValueAt(i, 0).toString() + ",";
			}
		}
		if (columns.length() > 0) {
			columns = columns.substring(0, columns.length() - 1);
		}
		MulitCellEditor cell = (MulitCellEditor) baseTable.getCellEditor(0, 1);
		cell.getCellEditor().setData(columns.split(","));
	}
	/**
	 * 取消表格编辑状态
	 */
	public void cancleEdit() {
		int row = baseTable.getSelectedRow();
		if (row != -1) {
			DefaultTableModel dtm = (DefaultTableModel) baseTable.getModel();
			dtm.setValueAt(zs.getText(), row, 3);
			dtm.setValueAt(xs.getText(), row, 4);
			if (baseTable.isEditing()) {
				baseTable.getCellEditor().stopCellEditing();
			}
		}
	}

	public void rowClicked() {
		DefaultTableModel dtm = (DefaultTableModel) baseTable.getModel();
		if (prerow != -1 && (prerow + 1) <= baseTable.getRowCount()) {
			dtm.setValueAt(zs.getText(), prerow, 3);
			dtm.setValueAt(xs.getText(), prerow, 4);
		}
		int row = baseTable.getSelectedRow();
		if (row != -1) {
			String comment = (String) dtm.getValueAt(row, 3);
			String parm = (String) dtm.getValueAt(row, 4);
			zs.setText(comment);
			xs.setText(parm);
			prerow = row;
		}
	}

	public BaseTable getBaseTable() {
		return baseTable;
	}

	public JTextField getXs() {
		return xs;
	}

	public void setXs(JTextField xs) {
		this.xs = xs;
	}

	public JTextField getZs() {
		return zs;
	}

	public void setZs(JTextField zs) {
		this.zs = zs;
	}

	public void setBaseTable(BaseTable baseTable) {
		this.baseTable = baseTable;
	}
}
