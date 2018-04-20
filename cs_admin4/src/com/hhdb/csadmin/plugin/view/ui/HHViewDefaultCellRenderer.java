package com.hhdb.csadmin.plugin.view.ui;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

public class HHViewDefaultCellRenderer extends DefaultTableCellRenderer {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Color evenRowColor = new Color(210, 250, 210);// 偶数行颜色
	private Color oddRowColor = new Color(255, 255, 255);// 奇数行颜色

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
		Component comp = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
//		if (!isSelected) {
			if (row % 2 == 0) {
				comp.setBackground(oddRowColor);
			} else {
				comp.setBackground(evenRowColor);
			}
//		}
		return comp;
	}

}
