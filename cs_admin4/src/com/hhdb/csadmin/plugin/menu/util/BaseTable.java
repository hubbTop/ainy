/*
 * DefaultTable.java
 *
 * Copyright (C) 2002-2013 Takis Diakoumis
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 3
 * of the License, or any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 *
 */

package com.hhdb.csadmin.plugin.menu.util;

import java.awt.Color;

import javax.swing.JTable;
import javax.swing.table.TableModel;

public class BaseTable extends JTable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7543654569715344172L;
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
	public boolean isCellEditable(int rowIndex, int columnIndex) {

	       
	       return false;

	   }
}