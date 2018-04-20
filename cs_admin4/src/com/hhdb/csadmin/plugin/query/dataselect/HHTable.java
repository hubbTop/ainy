package com.hhdb.csadmin.plugin.query.dataselect;

import java.awt.Color;

import javax.swing.JTable;
/**
 * 表格界面初始化
 * @author hw
 *
 */
public class HHTable extends JTable {
	private static final long serialVersionUID = 1L;
	private static final int DEFAULT_ROW_HEIGHT = 22;

	public HHTable() {
		init();
	}

	private void init() {
		setRowHeight(Math.max(getRowHeight(), DEFAULT_ROW_HEIGHT));
		setGridColor(new Color(240, 240, 240));
		setShowVerticalLines(true);
		getTableHeader().setReorderingAllowed(true);
//		setColumnSelectionAllowed(true);
		repaint();
	}
	
}