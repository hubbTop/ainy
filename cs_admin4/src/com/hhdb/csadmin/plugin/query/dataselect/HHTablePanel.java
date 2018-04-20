package com.hhdb.csadmin.plugin.query.dataselect;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.Vector;

import javax.swing.AbstractCellEditor;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.RowSorter;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableRowSorter;

import com.hhdb.csadmin.plugin.query.HQueryPlugin;
import com.hhdb.csadmin.plugin.query.dataselect.JFrame.FlowEditorPanel;
import com.hhdb.csadmin.plugin.query.dataselect.JFrame.JFramePanel;
import com.hhdb.csadmin.plugin.view.ui.HHTableDataModel;

/**
 * 表格界面滚动面板
 * 
 * @author hx
 * 
 */
public class HHTablePanel extends JScrollPane {
	private static final long serialVersionUID = 1L;
	private HHTable table;
	private HHTableDataModel tableModel;
	private String tableId;
	private FlowEditorPanel flow;
	private HQueryPlugin queryPlugin;

	/**
	 * @param editable 是否编辑
	 */
	public HHTablePanel(boolean editable,HQueryPlugin queryPlugin) {
		this.queryPlugin=queryPlugin;
		addTableModel(editable);
	}

	private void addTableModel(boolean editable) {
		int[] cloumns=null;
		setBorder(BorderFactory.createLineBorder(new Color(187, 187, 187),1));
		table = new HHTable();
		tableModel = new HHTableDataModel(editable, cloumns);
		table.setModel(tableModel);
		getViewport().add(table);
	}

	/**
	 * 表头和数据一起添加,无格式时
	 * @param header 数据名
	 * @param data 数据
	 * @param typelist 数据类型
	 */
	public void setDataList(List<String> header, List<String> typelist,List<List<Object>> data) {
		Vector<Object> columnIdentifiers = initVector(header);
		Vector<Object> dataVector = initVector(data, header);
		tableModel.setDataVector(dataVector, columnIdentifiers);
		// 加入手动点击排序功能
		RowSorter<HHTableDataModel> sorter = new TableRowSorter<HHTableDataModel>(tableModel);
		table.setRowSorter(sorter);
		
		//判断哪些列需要加入流操作按钮
		ActionPanelEditorRenderer er = new ActionPanelEditorRenderer();
		for(int i=0;i<header.size();i++){
			String name = header.get(i);
			if(!"".equals(name)){
			String type=typelist.get(i);
				if(type.equals("bytea")){
					table.getColumn(name).setCellRenderer(er);
					table.getColumn(name).setCellEditor(er);
				}
			}
		}
	}

	/**
	 * 表格按钮渲染
	 */
	public class ActionPanelEditorRenderer extends AbstractCellEditor implements TableCellRenderer, TableCellEditor {
		private static final long serialVersionUID = 1L;
		private JPanel panel = new JPanel();
		private JButton jbt = new JButton("...");
		private JLabel jtex = new JLabel("BYTEA");
		private Object cellValue ;  // 点击单元格的值
//		private Object ob = ""; 		   // 点击行的id
//		private String columnNmne; // 点击列的名称
		
		public ActionPanelEditorRenderer() {
			panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS ));
			panel.setBorder(BorderFactory.createLineBorder(Color.red, 0));
			jtex.setBorder(BorderFactory.createLineBorder(Color.red, 0));
			jbt.setBorder(BorderFactory.createLineBorder(Color.red, 0));
//			jbt.setContentAreaFilled(false);  //设置透明
			jbt.setFocusPainted(false);  //去除焦点
			panel.add(Box.createHorizontalGlue ()); 
			panel.add(jtex);
			panel.add(Box.createHorizontalGlue ()); 
			panel.add(jbt);
			jbt.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					flow = new FlowEditorPanel(cellValue);
					flow.initPanel();
					// 弹出对话框
					SqlOperationService srv = new SqlOperationService(queryPlugin);
					try {
						new JFramePanel("数据操作", 600, 600, flow, srv.getBaseFrame());
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					fireEditingStopped();  // 刷新渲染器
				}
			});
			panel.addMouseListener(new MouseAdapter(){
			    public void mousePressed(MouseEvent e) {
			    	fireEditingStopped();  // 刷新渲染器
			    }
			});
			jtex.addMouseListener(new MouseAdapter(){
			    public void mousePressed(MouseEvent e) {
			    	fireEditingStopped();  // 刷新渲染器
			    }
			});
		}
		@Override
		public Component getTableCellRendererComponent(JTable table,Object value, boolean isSelected, boolean hasFocus, int row,int column) {
			panel.setBackground(isSelected ? table.getSelectionBackground(): table.getBackground());
			return panel;
		}
		@Override
		public Component getTableCellEditorComponent(JTable table,Object value, boolean isSelected, int row, int column) {
			fireEditingStopped();
			cellValue = (value == null) ? "" : value;
			return panel;
		}
		@Override
		public Object getCellEditorValue() {
			return cellValue;
		}
	}

	/**
	 * 表头转换
	 * 
	 * @param cloumns
	 * @return
	 */
	private Vector<Object> initVector(List<String> cloumns) {
		Vector<Object> colNames = new Vector<Object>();
		for (String key : cloumns) {
			colNames.add(key.trim());
		}
		return colNames;
	}

	/**
	 * 数据转换
	 * 
	 * @param datas
	 * @param cloumns
	 * @return
	 */
	private Vector<Object> initVector(List<List<Object>> datas,
			List<String> columns) {
		Vector<Object> colNames = new Vector<Object>();
		if (datas != null) {
			int i = 1;
			for (int j = 2; j < datas.size(); j++) {
				List<Object> list = datas.get(j);
				Vector<Object> item = new Vector<Object>();
				item.add(i++);
				for (Object object : list) {
					item.add(object);
				}
				colNames.add(item);
			}
		}
		return colNames;
	}
	
	public HHTable getBaseTable() {
		return table;
	}

	public HHTableDataModel getTableDataModel() {
		return tableModel;
	}
	
	public String getTableId() {
		return tableId;
	}

	public void setTableId(String tableId) {
		this.tableId = tableId;
	}
}
