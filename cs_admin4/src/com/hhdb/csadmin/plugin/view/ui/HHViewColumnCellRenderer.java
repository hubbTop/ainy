package com.hhdb.csadmin.plugin.view.ui;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

public class HHViewColumnCellRenderer extends DefaultTableCellRenderer {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public HHViewColumnCellRenderer(){
	}
	
	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
		Component comp = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);   
		if(column == 0){
			this.setBackground(Color.LIGHT_GRAY);
			this.setForeground(Color.BLACK);
			this.setHorizontalAlignment(JLabel.CENTER);
		}
		return comp;
	}

	
}
