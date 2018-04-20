package com.hhdb.csadmin.plugin.query.dataselect.JFrame;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JToolBar;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.hhdb.csadmin.common.ui.textEdit.QueryEditorUi2;
import com.hhdb.csadmin.plugin.flow_editor.ui.BaseTabbedPaneCustom;

/**
 * 操作面板
 */
public class FlowEditorPanel extends JPanel  {
	private static final long serialVersionUID = 1L;
	private JToolBar toolBar = new JToolBar();
	private BaseTabbedPaneCustom tab = new BaseTabbedPaneCustom(JTabbedPane.TOP);
	private QueryEditorUi2 qed = new QueryEditorUi2(); // 预览面板
	private JLabel lab;
	private Object cellValue;

//	private int serial; // 选择的面板标号
//	private boolean isButton = true; // 按钮是否可用
//	private FileInputStream files; // 图片文件流
//	private byte[] by;				//图片byte

	public FlowEditorPanel(Object cellValue) {
		this.cellValue = cellValue;
	}

	/**
	 * 初始化面板
	 */
	public void initPanel() {
		tab.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
		setLayout(new GridBagLayout());
		toolBar.setFloatable(false);
		// text面板
		if(null != cellValue && !cellValue.equals("")){
			qed.setText(new String((byte[])cellValue));
		}
		tab.addTab("TEXT", new JScrollPane(qed.getTextArea()));

		lab = new JLabel(null, null, JLabel.CENTER); // 实例化标签对象
		tab.addTab("图片", new JScrollPane(lab));

		// 音频视频
//		tab.addTab("音频/视频", new JScrollPane());
		add(toolBar, new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0,GridBagConstraints.NORTH, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
		add(tab, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0,GridBagConstraints.NORTH, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));

		// table页切换事件
		tab.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				JTabbedPane tabbedPane = (JTabbedPane) e.getSource();
				int selectedIndex = tabbedPane.getSelectedIndex();
				if (selectedIndex == 0) {
//					serial = 0;
					if(null != cellValue && !cellValue.equals("")){
						qed.setText(new String((byte[])cellValue));
					}
				} else if (selectedIndex == 1) {
//					serial = 1;
					// 读取图片
					if (null != cellValue && !cellValue.equals("")) {
						byte[] b = null;
						try {
							b = (byte[]) cellValue;
						} catch (ClassCastException e2) {
							lab.setIcon(null);
							return;
						}
						ImageIcon icon = new ImageIcon(b); // 生成图片
						lab.setIcon(icon);
					}
				} else if (selectedIndex == 2) {
//					serial = 2;
				}
			}
		});
	}

	

	
	
	
		
	
}
