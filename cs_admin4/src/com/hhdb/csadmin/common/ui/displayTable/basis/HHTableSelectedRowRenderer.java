package com.hhdb.csadmin.common.ui.displayTable.basis;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

/**
 * 行颜色设置
 * @author hhxd
 *
 */
public class HHTableSelectedRowRenderer extends DefaultTableCellRenderer {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Color rowColor = new Color(70, 150, 255);//蓝色
	private Color evenRowColor = new Color(230, 253, 230);// 奇数行颜色
	private Color oddRowColor = new Color(255, 255, 255);// 偶数行颜色

	private int[] ss;   //选择的行
	
	public HHTableSelectedRowRenderer() {
		int[] a={-1};
		ss =a;
	}
	public HHTableSelectedRowRenderer(int[] ss) {
		this.ss = ss;
	}

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
		Component comp = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
		if(ss.length>1){
			for (int s : ss) { //1 2 
				if(row == s){  //1 2 3 4
					comp.setBackground(rowColor);
					comp.setForeground(Color.WHITE);
					break;
				}else{
					Boolean bool = true;
					for (int s1 : ss) {   //防止将之前已选择的改变颜色
						if(row == s1){
							bool =false;
							break;
						}
					}
					if(bool){
						if (row % 2 == 0) {
							comp.setBackground(evenRowColor);
						} else {
							comp.setBackground(oddRowColor);
						}
						comp.setForeground(Color.BLACK);
					}
				}
			}
		}else{
			if(row == ss[0]){
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
		}
		return comp;
	}

}
