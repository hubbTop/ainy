package com.hhdb.csadmin.plugin.function.util;

import java.awt.Color;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.DefaultCellEditor;
import javax.swing.JScrollPane;

import com.hhdb.csadmin.plugin.function.component.ComboBoxCellEditor;
import com.hhdb.csadmin.plugin.function.component.CreateTableSQLSyntax;
import com.hhdb.csadmin.plugin.function.component.LabelCellEditor;
import com.hhdb.csadmin.plugin.function.component.LabelRender;
import com.hhdb.csadmin.plugin.function.component.NumberCellEditor;
import com.hhdb.csadmin.plugin.function.component.TextCellEditor;
import com.hhdb.csadmin.plugin.function.component.checkbox.CheckBoxCellEditor;
import com.hhdb.csadmin.plugin.function.component.checkbox.CheckBoxRender;
import com.hhdb.csadmin.plugin.function.component.listcombox.MulitCellEditor;
import com.hhdb.csadmin.plugin.function.component.listcombox.MulitRender;

public class BaseTablePanel extends JScrollPane implements CreateTableSQLSyntax {
	private static final long serialVersionUID = 2243385022814286978L;
	
	private BaseTable table;
	private TableDataModel tableModel;

	/**
	 * 
	 * @param editable
	 *            是否编辑
	 */
	public BaseTablePanel(boolean editable) {
		addTableModel(editable, null);
	}

	/**
	 * 
	 * @param cloumns
	 *            哪些列不可编辑
	 */
	public BaseTablePanel(int[] cloumns) {
		addTableModel(true, cloumns);
	}

	private void addTableModel(boolean editable, int[] cloumns) {
		setBorder(BorderFactory.createLineBorder(new Color(187, 187, 187), 1));
		table = new BaseTable();
		tableModel = new TableDataModel(editable, cloumns);
		table.setModel(tableModel);
		getViewport().add(table);
	}

	/**
	 * 添加表头
	 * 
	 * @param newIdentifiers
	 */
	public void setColumnIdentifiers(Object[] newIdentifiers) {
		tableModel.setColumnIdentifiers(newIdentifiers);
	}

	/**
	 * 添加一行数据
	 */
	public void addRow(Object[] rowData) {
		tableModel.addRow(rowData);
	}

	/**
	 * 表头和数据一起添加,格式已组装好
	 * 
	 * @param dataVector
	 *            数据
	 * @param columnIdentifiers
	 *            表头
	 */
	public void setDataList(Vector<Object> columnIdentifiers, Vector<Object> dataVector) {
		tableModel.setDataVector(dataVector, columnIdentifiers);
	}

	/**
	 * 表头和数据一起添加,无格式时
	 * 
	 * @param header
	 * @param data
	 */
	public void setDataList(List<String> header, List<Map<String, Object>> data) {
		Vector<Object> columnIdentifiers = initVector(header);
		Vector<Object> dataVector = initVector(data, header);
		tableModel.setDataVector(dataVector, columnIdentifiers);
	}

	/**
	 * 表头能否拖动
	 * 
	 * @param t
	 */
	public void drawAllowed(boolean t) {
		table.getTableHeader().setReorderingAllowed(t);
	}

	/**
	 * 第几列设置为JTextField
	 * 
	 * @param cloumn
	 *            列
	 * @param textcell
	 */
	public void setCellEditor(int column, TextCellEditor textcell) {
		table.getColumnModel().getColumn(column).setCellEditor(new DefaultCellEditor(textcell));
	}

	/**
	 * 第几列设置为JComboBox
	 * 
	 * @param cloumn
	 * @param comboBoxcell
	 */
	public void setCellEditor(int column, final ComboBoxCellEditor comboBoxcell) {
		table.getColumnModel().getColumn(column).setCellEditor(new DefaultCellEditor(comboBoxcell));
	}

	/**
	 * 第几列设置为JTextField 数字
	 * 
	 * @param cloumn
	 * @param numbercell
	 */
	public void setCellEditor(int column, final NumberCellEditor numbercell) {
		table.getColumnModel().getColumn(column).setCellEditor(new DefaultCellEditor(numbercell));
	}

	/**
	 * 第几列设置为JComboBox
	 * 
	 * @param cloumn
	 * @param comboBoxcell
	 */
	public void setCellEditor(int column, final LabelCellEditor labelcell, LabelRender labelrender) {
		// table.getColumnModel().getColumn(column).setCellEditor(labelcell);
		table.getColumnModel().getColumn(column).setCellRenderer(labelrender);
	}

	/**
	 * 第几列设置为JComboBox
	 *
	 * @param cloumn
	 * @param comboBoxcell
	 */
	public void setCellEditor(int column, final MulitCellEditor mulitcell, MulitRender mulitRender) {
		table.getColumnModel().getColumn(column).setCellEditor(mulitcell);
		// table.getColumnModel().getColumn(column).setCellRenderer(mulitRender);
	}

	/**
	 * 第几列设置为JCheckBox
	 * 
	 * @param cloumn
	 * @param checkboxcell
	 * @param checkboxRenderer
	 */
	public void setCellEditor(int column, final CheckBoxCellEditor checkboxcell, CheckBoxRender checkboxRender) {
		table.getColumnModel().getColumn(column).setCellEditor(checkboxcell);
		table.getColumnModel().getColumn(column).setCellRenderer(checkboxRender);
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
}
