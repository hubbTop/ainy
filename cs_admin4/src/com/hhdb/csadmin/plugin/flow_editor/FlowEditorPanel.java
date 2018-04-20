package com.hhdb.csadmin.plugin.flow_editor;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.hhdb.csadmin.common.dao.ConnService;
import com.hhdb.csadmin.common.ui.textEdit.QueryEditorUi2;
import com.hhdb.csadmin.plugin.flow_editor.ui.BaseButton;
import com.hhdb.csadmin.plugin.flow_editor.ui.BaseTabbedPaneCustom;
import com.hhdb.csadmin.plugin.flow_editor.ui.IconUtilities;

/**
 * 操作面板
 */
public class FlowEditorPanel extends JPanel implements ActionListener {
	private static final long serialVersionUID = 1L;
	private HFlowEditor hfe;
	private JToolBar toolBar = new JToolBar();
	private BaseTabbedPaneCustom tab = new BaseTabbedPaneCustom(JTabbedPane.TOP);
	private QueryEditorUi2 qed = new QueryEditorUi2(); // 预览面板
	private JLabel lab;

	private int serial; // 选择的面板标号
	private boolean isButton = true; // 按钮是否可用
	private FileInputStream files; // 图片文件流
	private byte[] by;				//图片byte

	public FlowEditorPanel(HFlowEditor hfe) {
		this.hfe = hfe;
	}

	/**
	 * 初始化面板
	 */
	public void initPanel() {
		tab.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
		setLayout(new GridBagLayout());
		textTool();
		toolBar.setFloatable(false);
		// text面板
		if(null != hfe.value && !hfe.value.equals("")){
			qed.setText(new String((byte[])hfe.value));
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
					serial = 0;
					textTool();
					if(null != hfe.value && !hfe.value.equals("")){
						qed.setText(new String((byte[])hfe.value));
					}
				} else if (selectedIndex == 1) {
					serial = 1;
					imageTool();
					// 读取图片
					if (null != hfe.value && !hfe.value.equals("")) {
						byte[] b = null;
						try {
							b = (byte[]) hfe.value;
						} catch (ClassCastException e2) {
							lab.setIcon(null);
							return;
						}
						ImageIcon icon = new ImageIcon(b); // 生成图片
						lab.setIcon(icon);
					}
				} else if (selectedIndex == 2) {
					serial = 2;
					videoTool();
				}
			}
		});
	}

	/**
	 * 创建按钮
	 * 
	 * @param text
	 * @param icon
	 * @param comand
	 */
	private void createBtn(String text, Icon icon, String comand) {
		BaseButton basebtn = new BaseButton(text, icon);
		basebtn.setActionCommand(comand);
		basebtn.addActionListener(this);
		toolBar.add(basebtn);
	}

	/**
	 * 文字工具栏
	 */
	private void textTool() {
		toolBar.removeAll();
		toolBar.repaint();
		createBtn("保存", IconUtilities.loadIcon("save.png"), "save");
		toolBar.setFloatable(false);
		toolBar.getComponentAtIndex(0).setEnabled(isButton);
	}

	/**
	 * 图片工具栏
	 */
	private void imageTool() {
		toolBar.removeAll();
		toolBar.repaint();
		createBtn("保存", IconUtilities.loadIcon("save.png"), "save");
		toolBar.addSeparator();
		createBtn("添加图片", IconUtilities.loadIcon("addindex.png"), "addimage");
		toolBar.setFloatable(false);
		toolBar.getComponentAtIndex(0).setEnabled(isButton);
		toolBar.getComponentAtIndex(2).setEnabled(isButton);
	}

	/**
	 * 音频/视频工具栏
	 */
	private void videoTool() {
		toolBar.removeAll();
		toolBar.repaint();
		createBtn("保存", IconUtilities.loadIcon("save.png"), "save");
		toolBar.addSeparator();
		createBtn("添加文件", IconUtilities.loadIcon("addindex.png"), "addvideo");
		toolBar.setFloatable(false);
		toolBar.getComponentAtIndex(0).setEnabled(isButton);
		toolBar.getComponentAtIndex(2).setEnabled(isButton);
	}
	
	/**
	 * 保存
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		String actionCmd = e.getActionCommand();
		if (actionCmd.equals("save")) {
			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					try {
						switch (serial) {
						case 0: // 文字保存
							String vl = qed.getText();
							if (null != vl && !vl.equals("")) {
								String sql = "";
								if(hfe.ctid.equals("")){
									sql = "insert into " + hfe.name + "(" + hfe.columnName + ") values ('" + vl + "')";
								}else{
									sql = "update " + hfe.name + " set " + hfe.columnName + "='" + vl + "' where ctid='" + hfe.ctid + "'";
								}
								hfe.serv.sqlOperation(sql);
								JOptionPane.showMessageDialog(null, "保存成功");
								isButton = false;
								toolBar.getComponentAtIndex(0).setEnabled(isButton);
								hfe.value = vl.getBytes();   //刷新值
								hfe.serv.refreshTable(hfe.componentId); //刷新表格
							}
							break;
						case 1: // 图片保存
							if(null != files){
								Connection conn = null;
								PreparedStatement ps = null;
								try {
									conn = ConnService.createConnection(hfe.serv.getServerBean());
									String sql = "";
									if(hfe.ctid.equals("")){
										sql = "insert into" + hfe.name + "(" + hfe.columnName + ") values (?)";
									}else{
										sql = "update " + hfe.name + " set " + hfe.columnName + "=? where ctid='" + hfe.ctid + "'";
									}
									ps = conn.prepareStatement(sql);
									ps.setBinaryStream(1, files, files.available());
									int count = ps.executeUpdate();
									if (count > 0) {
										JOptionPane.showMessageDialog(null, "保存成功");
										isButton = false;
										toolBar.getComponentAtIndex(0).setEnabled(isButton);
										toolBar.getComponentAtIndex(2).setEnabled(isButton);
										hfe.value = by;   //刷新值
										hfe.serv.refreshTable(hfe.componentId); //刷新表格
									} else {
										JOptionPane.showMessageDialog(null, "保存失败");
									}
								} catch (Exception e) {
									e.printStackTrace();
								} finally {
									ConnService.closeConn(conn); // 关闭连接
									if (null != ps) {
										try {
											ps.close();
										} catch (SQLException e) {
											e.printStackTrace();
										}
									}
								}
							}
							break;
						case 2: // 保存视频

							break;
						}
					} catch (Exception e1) {
						JOptionPane.showMessageDialog(null,
								"错误信息：" + e1.getMessage(), "错误",
								JOptionPane.ERROR_MESSAGE);
						e1.printStackTrace();
					}
				}
			});
		} else if (actionCmd.equals("addimage")) {
			try {
				JFileChooser chooser = new JFileChooser();
				chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);// 设置选择模式，既可以选择文件又可以选择文件夹
				int result = chooser.showOpenDialog(null); // 打开"打开文件"对话框
				if (result == JFileChooser.APPROVE_OPTION) {
					File file = chooser.getSelectedFile();
					files = new FileInputStream(file);
					byte[] b = new byte[(int) file.length()];// 定义一个用于接收字符串的数组
					
					InputStream input = new FileInputStream(file);
					input.read(b); // 读取
					input.close();

					ImageIcon icon = new ImageIcon(b); // 生成图片
					lab.setIcon(icon);
					by = b;
				}
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
	}
}
