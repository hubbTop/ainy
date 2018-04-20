package com.hhdb.csadmin.plugin.menu.util;


import java.util.Vector;

import javax.swing.table.DefaultTableModel;

/**
 * 表格模型
 * 
 * @author ZL
 * 
 */
public class TableDataModel extends DefaultTableModel {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1532328467743058804L;
	/**
	 * 表格只读 true:编辑 false：只读
	 */
	private boolean editable;
	/**
	 * 列只读
	 */
	private int[] cloumns;

	public TableDataModel(boolean editable, int[] cloumns) {
		this.cloumns = cloumns;
		this.editable = editable;
	}

	@Override
	public boolean isCellEditable(int row, int column) {
		if (cloumns != null) {
			for (int cl : cloumns) {
				if (cl == column) {
					return false;
				}
			}
		}
		return editable;
	}

	public void setDataVectors(Vector<Object> data, Vector<Object> columnNames) {
		setDataVector(data, columnNames);
	}
}
