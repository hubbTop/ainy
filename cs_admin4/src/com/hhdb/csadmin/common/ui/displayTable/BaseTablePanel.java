package com.hhdb.csadmin.common.ui.displayTable;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.RowSorter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

import com.hhdb.csadmin.common.ui.displayTable.basis.BaseTable;
import com.hhdb.csadmin.common.ui.displayTable.basis.ButtonEditor;
import com.hhdb.csadmin.common.ui.displayTable.basis.ButtonRenderer;
import com.hhdb.csadmin.common.ui.displayTable.basis.HHTableSelectedRowRenderer;
import com.hhdb.csadmin.common.ui.displayTable.basis.TableDataModel;

/**
 * 基础表面板
 * @author hhxd
 *
 */
public class BaseTablePanel extends JScrollPane{

	private static final long serialVersionUID = 1L;
	private BaseTable table;
	private TableDataModel tableModel;

	/**
	 * 初始化化表面板
	 * @param editable 是否可以编辑
	 */
	public BaseTablePanel(boolean editable) {
		setBorder(BorderFactory.createLineBorder(new Color(187, 187, 187), 1));
		table = new BaseTable();
		tableModel = new TableDataModel(editable,null);
		table.setModel(tableModel);
		getViewport().add(table);
	}
	
	/**
	 * 
	 * @param cloumns
	 *            哪些列不可编辑
	 */
	public BaseTablePanel(int[] cloumns) {
		setBorder(BorderFactory.createLineBorder(new Color(187, 187, 187), 1));
		table = new BaseTable();
		tableModel = new TableDataModel(true, cloumns);
		table.setModel(tableModel);
		getViewport().add(table);
	}
	
	/**
	 * 有格式数据添加
	 * @param list
	 */
	public void setData(List<List<Object>> list){
		if (null != list && list.size()>0 ) {
			Vector<String> columName = new Vector<String>();
			Vector<String> rowsLine = new Vector<String>();
			for (Object sn : list.get(0)) {
				columName.add(sn.toString());
			}
			tableModel.setColumnIdentifiers(columName);

			for(int i=1;i<list.size();i++){
				List<Object> l = list.get(i);
				for(Object value:l){
					rowsLine.add(value.toString());
				}
				tableModel.addRow(rowsLine);
				rowsLine = new Vector<>();
			}			
		}
	}
	
	/**
	 * 表头和数据一起添加,无格式时
	 * @param header
	 * @param data
	 */
	public void setData(List<String> header, List<Map<String, Object>> data) {
		Vector<Object> columnIdentifiers = initVector(header);
		Vector<Object> dataVector = initVector(data, header);
		tableModel.setDataVector(dataVector, columnIdentifiers);
	}
	
	/**
	 * 表头能否拖动
	 */
	public void drawAllowed(boolean t) {
		table.getTableHeader().setReorderingAllowed(t);
	}
	
	/**
	 * 是否隔行变色
	 */
	public void nterlacedDiscoloration(boolean t) {
		if(t){
			table.setDefaultRenderer(Object.class,new HHTableSelectedRowRenderer());  //设置隔行变色
		}
	}
	/**
	 * 点击表头是否排序
	 */
	public void setRowSorter(boolean t) {
		if(t){
			//点击排序
			RowSorter<DefaultTableModel> sorter = new TableRowSorter<DefaultTableModel>(tableModel);
			table.setRowSorter(sorter);
		}
	}
	/**
	 * 点击行是否高亮显示
	 * @param t 是否高亮显示
	 * @param bool  是否能选择多行
	 */
	public void highlight(boolean t,final boolean bool) {
		if(t){
			//高亮显示点击行
			table.addMouseListener(new MouseAdapter() {
				@Override
				public void mousePressed(MouseEvent e) {
					if(bool){
						int[] ss = table.getSelectedRows(); 
						table.setDefaultRenderer(Object.class,new HHTableSelectedRowRenderer(ss));
					}else{
						int[] aa = new int[1];
						aa[0]= table.getSelectedRows()[0];
						table.setDefaultRenderer(Object.class,new HHTableSelectedRowRenderer(aa));
					}
				}
			});
			//头部点击事件
			table.getTableHeader().addMouseListener(new MouseAdapter() {
				@Override
				public void mouseReleased(MouseEvent e) {
					//去除选择行与选择行变色
					table.clearSelection();
					table.setDefaultRenderer(Object.class,new HHTableSelectedRowRenderer());  //设置隔行变色
				}
			});
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
			colNames.add(key);
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
	private Vector<Object> initVector(List<Map<String, Object>> datas, List<String> cloumns) {
		Vector<Object> colNames = new Vector<Object>();
		if (datas != null) {
			for (Map<String, Object> map : datas) {
				Vector<Object> item = new Vector<Object>();
				for (String key : cloumns) {
					item.add(map.get(key));
				}
				colNames.add(item);
			}
		}
		return colNames;
	}
	
	public BaseTable getBaseTable() {
		return table;
	}

	public TableDataModel getTableDataModel() {
		return tableModel;
	}
	
	
	
	public static void main(String[] args) {
		JFrame jf = new JFrame("JTable的排序测试");
		jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		// 表格中显示的数据、
		Object rows[][] = { { "王鹏", "江西", "43" }, { "周丹", "四川", "25" },
				{ "钱丽", "贵州", "32" }, { "孙军", "新疆", "24" },
				{ "李欢", "江苏", "45" }, { "苏菲", "广东", "33" } };
		String columns[] = { "姓名", "籍贯", "年龄" };
		
		BaseTablePanel bt =new BaseTablePanel(true);
		bt.getTableDataModel().setDataVector(rows,columns);
		bt.drawAllowed(false);
		bt.nterlacedDiscoloration(true);
		bt.setRowSorter(true);
		bt.highlight(true,true);
		
		bt.getBaseTable().getColumnModel().getColumn(0).setCellEditor(new ButtonEditor());
		bt.getBaseTable().getColumnModel().getColumn(0).setCellRenderer(new ButtonRenderer());
		
		
		jf.add(bt, BorderLayout.CENTER);
		jf.setSize(300, 150);
		jf.setVisible(true);
		
	}
}
