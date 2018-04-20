package com.hhdb.csadmin.plugin.view.ui;

import java.awt.Insets;

import javax.swing.Icon;
import javax.swing.JLabel;

public class BaseLabel extends JLabel {
	public static final int DEFAULT_FONT_SIZE = 12;
	public static final int DEFAULT_FIELD_HEIGHT = 24;
	public static final int DEFAULT_BUTTON_HEIGHT = 30;
	public static final Insets DEFAULT_FIELD_MARGIN = new Insets(2, 2, 2, 2);
	public static final int DEFAULT_MENU_HEIGHT = 23;
	public static final String EMPTY = "";
	private static final long serialVersionUID = 1L;

	public BaseLabel() {
		super();
	}

	public BaseLabel(Icon image, int horizontalAlignment) {
		super(image, horizontalAlignment);
	}

	public BaseLabel(Icon image) {
		super(image);
	}

	public BaseLabel(String text, Icon icon, int horizontalAlignment) {
		super(text, icon, horizontalAlignment);
	}

	public BaseLabel(String text, int horizontalAlignment) {
		super(text, horizontalAlignment);
	}

	public BaseLabel(String text) {
		super(text);
	}

	public Insets getMargin() {
		return DEFAULT_FIELD_MARGIN;
	}
	
	public int getHeight() {
		return Math.max(super.getHeight(), DEFAULT_FIELD_HEIGHT);
	}
}
