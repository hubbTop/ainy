
package com.hhdb.csadmin.plugin.type_create.component;

import java.awt.Color;

import javax.swing.JTable;
import javax.swing.table.TableModel;

public class BaseTable extends JTable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final int DEFAULT_ROW_HEIGHT = 22;

	public BaseTable() {
		init();
	}

	public BaseTable(TableModel model) {
		super(model);
		init();
	}

	private void init() {
		setRowHeight(Math.max(getRowHeight(), DEFAULT_ROW_HEIGHT));
		setGridColor(new Color(240, 240, 240));
		setShowVerticalLines(true);
		getTableHeader().setReorderingAllowed(true);
		setColumnSelectionAllowed(true);
		repaint();
	}

}