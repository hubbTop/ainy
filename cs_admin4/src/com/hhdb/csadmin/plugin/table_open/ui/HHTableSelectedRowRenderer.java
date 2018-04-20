package com.hhdb.csadmin.plugin.table_open.ui;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

public class HHTableSelectedRowRenderer extends DefaultTableCellRenderer {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Color rowColor = new Color(70, 150, 255);//蓝色
	private Color evenRowColor = new Color(230, 253, 230);// 奇数行颜色
	private Color oddRowColor = new Color(255, 255, 255);// 偶数行颜色

	private int rows;

	public HHTableSelectedRowRenderer(int row) {
		this.rows = row;
	}

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
		Component comp = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
		if(row == rows){
			comp.setBackground(rowColor);
			comp.setForeground(Color.WHITE);
		}else{
			if (row % 2 == 0) {
				comp.setBackground(evenRowColor);
			} else {
				comp.setBackground(oddRowColor);
			}
			comp.setForeground(Color.BLACK);
		}
		return comp;
	}

}
