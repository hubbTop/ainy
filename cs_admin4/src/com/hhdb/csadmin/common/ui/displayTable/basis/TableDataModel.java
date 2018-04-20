package com.hhdb.csadmin.common.ui.displayTable.basis;

import javax.swing.table.DefaultTableModel;

/**
 * 表格模型
 * @author hhxd
 *
 */
public class TableDataModel extends DefaultTableModel {
	private static final long serialVersionUID = 1L;
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

}
