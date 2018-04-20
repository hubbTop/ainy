package com.hhdb.csadmin.common.ui.displayTable.basis;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JTable;

/**
 * 按钮表编辑器
 * @author hhxd
 */

public class ButtonEditor extends DefaultCellEditor {
	private static final long serialVersionUID = 1L;
	protected JButton button;
	private String cellValue;

	public ButtonEditor() {
		super(new JCheckBox());
		button = new JButton();
		button.setOpaque(true);
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(button, cellValue + ": Ouch!");
				// 刷新渲染器
				fireEditingStopped();
			}
		});
	}
	@Override
	public JComponent getTableCellEditorComponent(JTable table, Object value,
			boolean isSelected, int row, int column) {
		// value 源于单元格数值
		cellValue = (value == null) ? "" : value.toString();
		return button;
	}
	@Override
	public Object getCellEditorValue() {
		return new String(cellValue);
	}
}
