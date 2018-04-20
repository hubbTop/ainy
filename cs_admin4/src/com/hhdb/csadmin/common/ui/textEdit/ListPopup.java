package com.hhdb.csadmin.common.ui.textEdit;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.MouseInputListener;

/**
 * @ClassName: ListPopup
 * @author: hyz
 * @Description: 自定提示弹出框
 */
public class ListPopup extends JPopupMenu implements MouseInputListener {

	private static final long serialVersionUID = 2546089629243743006L;
	public JList<String> list;
	public JScrollPane pane;
	private ArrayList<ListSelectionListener> listeners = new ArrayList<ListSelectionListener>();
	public void addListSelectionListener(ListSelectionListener l) {
		if (!listeners.contains(l))
			listeners.add(l);
	}
	public void setSelectedIndex(int index) {
		if (index >= list.getModel().getSize())
			index = 0;
		if (index < 0)
			index = list.getModel().getSize() - 1;
		list.ensureIndexIsVisible(index);
		list.setSelectedIndex(index);
	}
	public Object getSelectedValue() {
		return list.getSelectedValue();
	}
	public int getSelectedIndex() {
		return list.getSelectedIndex();
	}
	public boolean isSelected() {
		return list.getSelectedIndex() != -1;
	}
	public void setLastOneSelected() {
		int count = list.getModel().getSize();
		if (count > 0) {
			list.ensureIndexIsVisible(count - 1);
			list.setSelectedIndex(count - 1);
		}
	}
	public void removeListSelectionListener(ListSelectionListener l) {
		if (listeners.contains(l))
			listeners.remove(l);
	}
	private void fireValueChanged(ListSelectionEvent e) {
		for (ListSelectionListener l : listeners) {
			l.valueChanged(e);
		}
	}
	public ListPopup() {
		setLayout(new BorderLayout());
		list = new JList<String>();
		list.setFont(new Font("SimSan", Font.PLAIN, 14));
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list.addMouseListener(this);
		list.addMouseMotionListener(this);
		list.setModel(new DefaultListModel<String>());
		pane = new JScrollPane(list);
		pane.setBorder(null);
		//setBorder(new MatteBorder(7, 7, 7, 7, new Color(234, 234, 234)));
		add(pane, BorderLayout.CENTER);
	}
	public int getItemCount() {
		DefaultListModel<String> model = (DefaultListModel<String>) list.getModel();
		return model.getSize();
	}
	public Object getItem(int index) {
		DefaultListModel<String> model = (DefaultListModel<String>) list.getModel();
		return model.get(index);
	}
	public void removeItem(Object o) {
		DefaultListModel<String> model = (DefaultListModel<String>) list.getModel();
		model.removeElement(o);
		list.repaint();
	}
	public void setList(List<String> li) {
		DefaultListModel<String> model = new DefaultListModel<String>();
		for (String str : li) {
			model.addElement(str);
		}
		list.setCellRenderer(new DefaultListCellRenderer());
		list.setModel(model);
		list.repaint();
	}
	public void mouseClicked(MouseEvent e) {
	}
	public void mousePressed(MouseEvent e) {
		if (list.getSelectedIndex() != -1)
			fireValueChanged(new ListSelectionEvent(list, list.getSelectedIndex(), list.getSelectedIndex(), true));
	}
	public void mouseReleased(MouseEvent e) {
	}
	public void mouseEntered(MouseEvent e) {
	}
	public void mouseExited(MouseEvent e) {
	}
	public void mouseMoved(MouseEvent anEvent) {
		if (anEvent.getSource() == list) {
			Point location = anEvent.getPoint();
			Rectangle r = new Rectangle();
			list.computeVisibleRect(r);
			if (r.contains(location)) {
				updateListBoxSelectionForEvent(anEvent, false);
			}
		}
	}
	protected void updateListBoxSelectionForEvent(MouseEvent anEvent, boolean shouldScroll) {

		Point location = anEvent.getPoint();
		if (list == null) {
			return;
		}
		int index = list.locationToIndex(location);
		if (index == -1) {
			if (location.y < 0) {
				index = 0;
			} else {
				index = list.getModel().getSize() - 1;
			}
		}
		if (list.getSelectedIndex() != index) {
			list.setSelectedIndex(index);
			if (shouldScroll) {
				list.ensureIndexIsVisible(index);
			}
		}
	}
	public void mouseDragged(MouseEvent e) {}
}
