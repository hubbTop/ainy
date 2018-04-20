package com.hhdb.csadmin.plugin.view.ui;

import java.awt.Color;
import java.util.List;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JScrollPane;
import javax.swing.RowSorter;
import javax.swing.table.TableRowSorter;
/**
 * 表格界面滚动面板
 * @author hw
 *
 */
public class HHTablePanel extends JScrollPane{
	private static final long serialVersionUID = 1L;
	private HHView table;
	private HHTableDataModel tableModel;
	private String tableId;//表格编码
	
	
	public String getTableId() {
		return tableId;
	}


	public void setTableId(String tableId) {
		this.tableId = tableId;
	}


	/**
	 * 
	 * @param editable
	 *            是否编辑
	 */
	public HHTablePanel(boolean editable) {
		addTableModel(editable, null);
	}
	
	/**
	 * 初始化
	 * @param editable
	 * @param cloumns
	 */
	private void addTableModel(boolean editable, int[] cloumns) {
		setBorder(BorderFactory.createLineBorder(new Color(187, 187,187),1));
		table = new HHView();
		tableModel = new HHTableDataModel(editable, cloumns);
		table.setModel(tableModel);
		getViewport().add(table);
	}

	/**
	 * 表头和数据一起添加,无格式时
	 * 
	 * @param header
	 * @param data
	 */
	public void setDataList(List<String> header, List<List<Object>> data) {
		Vector<Object> columnIdentifiers = initVector(header);
		Vector<Object> dataVector = initVector(data, header);
		tableModel.setDataVector(dataVector, columnIdentifiers);
		
		//加入手动点击排序功能
		RowSorter<HHTableDataModel> sorter = new TableRowSorter<HHTableDataModel>(tableModel);
        table.setRowSorter(sorter);
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
			colNames.add(key.toUpperCase().trim());
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
	private Vector<Object> initVector(List<List<Object>> datas, List<String> columns) {
		Vector<Object> colNames = new Vector<Object>();
		if(datas!=null)
		{
			int i=1;
			for (int j = 1; j < datas.size(); j++) {
				List<Object> lob = datas.get(j);
				Vector<Object> item = new Vector<Object>();
				item.add(i++);  //序号
				for (Object object : lob) {
					item.add(object);
				}
				colNames.add(item);
			}
//			Vector<Object> item = new Vector<Object>();
//			item.add("*");
//			colNames.add(item);
		}
		return colNames;
	}

	
	public HHView getBaseTable() {
		return table;
	}

	public HHTableDataModel getTableDataModel() {
		return tableModel;
	}
}
