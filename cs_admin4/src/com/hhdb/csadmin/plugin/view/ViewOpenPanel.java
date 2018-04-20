package com.hhdb.csadmin.plugin.view;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JToolBar;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.TableColumn;

import com.hhdb.csadmin.common.ui.textEdit.QueryEditorUi2;
import com.hhdb.csadmin.common.ui.textEdit.QueryTextPane;
import com.hhdb.csadmin.common.util.HHSqlUtil;
import com.hhdb.csadmin.plugin.view.ui.BaseButton;
import com.hhdb.csadmin.plugin.view.ui.BaseTabbedPaneCustom;
import com.hhdb.csadmin.plugin.view.ui.HHPagePanel;
import com.hhdb.csadmin.plugin.view.ui.HHTablePanel;
import com.hhdb.csadmin.plugin.view.ui.HHTableSelectedRowRenderer;
import com.hhdb.csadmin.plugin.view.ui.HHViewColumnCellRenderer;
import com.hhdb.csadmin.plugin.view.ui.HHViewDefaultCellRenderer;
import com.hhdb.csadmin.plugin.view.ui.IconUtilities;
import com.hhdb.csadmin.plugin.view.ui.OutTabPanel;
import com.hhdb.csadmin.plugin.view.util.ViewsPanelHandle;

/**
 * 打开表操作面板
 */
public class ViewOpenPanel extends JPanel implements ActionListener {
	public ViewsPanelHandle vstp;
	private JToolBar toolBar = new JToolBar();
	private static final long serialVersionUID = 1L;
	private HHTablePanel hhTbPanel;
	private HHPagePanel hhPgPanel;
	private BaseTabbedPaneCustom tab;
	private int key = 0;
	private boolean isHave = false;
	private String viewName; //视图名
	private QueryEditorUi2 edit;    //编辑面板
	private QueryTextPane preview;			//预览面板
	public HView hv;
	private OutTabPanel outpanel = new OutTabPanel();
	JSplitPane vSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
	
	public ViewOpenPanel(HView hv){
		this.hv=hv;
		vstp = new ViewsPanelHandle(hv);
		outpanel.hv=hv;
	}
	
	/**
	 * 打开视图初始化
	 * @param firstPageTable
	 */
	public void initPanel(List<List<Object>> list) {
		this.setLayout(new GridBagLayout());
		this.setBorder(BorderFactory.createLineBorder(new Color(178, 178, 178),1));
		hhTbPanel = new HHTablePanel(false);
		hhPgPanel = new HHPagePanel();
		excutepage(list);
		add(hhTbPanel, new GridBagConstraints(0, 0, 2, 1, 1.0, 1.0,
				GridBagConstraints.NORTH, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
		add(hhPgPanel, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0,
				GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0,0, 0, 30), 10, 0));
	}

	
	/**
	 * 处理数据显示
	 * @param pageTable
	 */
	public void excutepage(List<List<Object>> lis) {
		hhTbPanel.setBorder(BorderFactory.createEmptyBorder());
		hhTbPanel.getBaseTable().setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		hhTbPanel.getBaseTable().getTableHeader().setReorderingAllowed(false);
		hhTbPanel.getBaseTable().removeAll();
		hhTbPanel.getBaseTable().setDefaultRenderer(Object.class,
				new HHViewDefaultCellRenderer());
		Vector<String> colname = new Vector<String>();
		for (Object ob : lis.get(0)) {
			colname.add(ob.toString());
		}
		//加一行空格
		List<String> list = new ArrayList<>();
		list.add("");
		list.addAll(colname);
		hhTbPanel.setDataList(list, lis);
		// 处理特殊字段的显示
		TableColumn index = hhTbPanel.getBaseTable().getColumnModel().getColumn(0);
		index.setWidth(20);
		index.setPreferredWidth(20);
		index.setCellRenderer(new HHViewColumnCellRenderer());
		TableColumn coln = hhTbPanel.getBaseTable().getColumnModel().getColumn(list.size() - 1);
		coln.setMinWidth(0);
		coln.setMaxWidth(0);
		coln.setWidth(0);
		coln.setPreferredWidth(0);
		// 单击序列高亮显示一行数据
		hhTbPanel.getBaseTable().addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				hhTbPanel.getBaseTable().setDefaultRenderer(Object.class,new HHViewDefaultCellRenderer());
				int row = hhTbPanel.getBaseTable().getSelectedRow();
				hhTbPanel.getBaseTable().setDefaultRenderer(Object.class,new HHTableSelectedRowRenderer(row));
			}
		});
	}

	/**
	 * 编辑页面
	 * 
	 */
	public void viewsTabPanelHandle(boolean isHaves) {
		//获取sql编辑面板
		edit = new QueryEditorUi2();
		getKeyName();
		//获取sql预览面板
		preview = new QueryTextPane();
		
		if(isHaves){
			this.isHave = isHaves;
			vstp.setViewName(viewName);
			vstp.design(edit);
		}
		tab = new BaseTabbedPaneCustom(JTabbedPane.TOP);
		tab.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
		setLayout(new GridBagLayout());
		createBtn("新建", IconUtilities.loadIcon("newview.png"), "addfield");
		createBtn("保存", IconUtilities.loadIcon("save.png"), "save");
		createBtn("另存为", IconUtilities.loadIcon("save.png"), "saveAs");
		toolBar.addSeparator();  //分隔符
		createBtn("预览", IconUtilities.loadIcon("runview.png"), "preview");
		toolBar.addSeparator();
		toolBar.setFloatable(false);
		tab.addTab("定义", edit.getContentPane());
		
		tab.addTab("SQL预览", new JScrollPane(preview));
		add(toolBar, new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0,
				GridBagConstraints.NORTH, GridBagConstraints.BOTH, new Insets(
						0, 0, 0, 0), 0, 0));
		add(tab, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0,
				GridBagConstraints.NORTH, GridBagConstraints.BOTH, new Insets(
						0, 0, 0, 0), 0, 0));
		
		//sql预览页
		tab.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				JTabbedPane tabbedPane = (JTabbedPane) e.getSource();
			    int selectedIndex = tabbedPane.getSelectedIndex();
			    if(selectedIndex==1){ 
			    	String temp = "";
		    		temp = "CREATE VIEW \""+hv.smName+"\".\""+viewName+"\" AS"+"\n";
		    		String sqlText=edit.getText();
		    		if(sqlText.trim().lastIndexOf(";")<0){
		    			sqlText+=";";
		    		}
		    		preview.setText(temp+sqlText);
			    }
			}
		});
		
		//监听键盘
		edit.getContentPane().addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if(e.isControlDown() && e.getKeyCode()==KeyEvent.VK_S ){
					key = 1;
					SaveViewKey();
					key = 0;
				}
			}
		});
	}

	/**
	 * 创建按钮
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
	
	@Override
	public void actionPerformed(ActionEvent e) {
		String actionCmd = e.getActionCommand();
		if (actionCmd.equals("save")) {
			if (isHave) {  //修改
				boolean flag = vstp.updatView(edit, viewName,hv.smName);
				toolBar.getComponentAtIndex(1).setEnabled(flag);
			} else {   									 //保存
				boolean flag = vstp.saveView(edit,hv.smName);
				toolBar.getComponentAtIndex(1).setEnabled(flag);
			}
		} else if (actionCmd.equals("addfield")) {
			try {
				hv.unwrap();
				hv.s++;
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		} else if ("saveAs".equals(actionCmd)) {
			vstp.saveView(edit, hv.smName);
		} else if (actionCmd.equals("preview")) {   //运行
			String strSelected = edit.getText();
			if (strSelected != null && !"".equals(strSelected.trim())) {
				outpanel.cleanTab();
				vSplitPane.addComponentListener(new ComponentAdapter() {
					@Override
					public void componentResized(ComponentEvent e) {
						vSplitPane.setDividerLocation(0.5);
					}
				});
				vSplitPane.setBottomComponent(outpanel);
				vSplitPane.setTopComponent(tab);
				vSplitPane.setDividerLocation(0.5);
				vSplitPane.setIgnoreRepaint(true);
				vstp.previewPanel(edit,outpanel);
				add(vSplitPane, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0,GridBagConstraints.NORTH, GridBagConstraints.BOTH,
						new Insets(0, 0, 0, 0), 0, 0), 0);
				vSplitPane.getParent().doLayout();  //进行布局
			}
		}

	}
	public void SaveViewKey(){
		if(key==1){
			if(isHave){
				boolean flag = vstp.updatView(edit, viewName,hv.smName);
				toolBar.getComponentAtIndex(1).setEnabled(flag);
			}else{
				boolean flag = vstp.saveView(edit, hv.smName);
				toolBar.getComponentAtIndex(1).setEnabled(flag);
			}
		}
	}
	
	/**
	 * 设置表名视图名提示关键词
	 */
	public void getKeyName() {
		List<Map<String, Object>> list = new ArrayList<>();
		List<String> lis = new ArrayList<>();
		List<String> liss = new ArrayList<>();
		try {
			// 表名
			list = hv.sosi.getNameByType(HHSqlUtil.ITEM_TYPE.TABLE, "prop_coll"); 
			for (Map<String, Object> maps : list) {
				lis.add(maps.get("name").toString());
			}
			edit.setTableCompletionProvider(lis);
			// 视图
			list = hv.sosi.getNameByType(HHSqlUtil.ITEM_TYPE.VIEW, "prop_coll");
			for (Map<String, Object> maps : list) {
				liss.add(maps.get("name").toString());
			}
			edit.setViewCompletionProvider(liss);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e.getMessage(), "提示",JOptionPane.ERROR_MESSAGE);
		}
	}
	
	
	public HHTablePanel getHhTbPanel() {
		return hhTbPanel;
	}

	public void setHhTbPanel(HHTablePanel hhTbPanel) {
		this.hhTbPanel = hhTbPanel;
	}

	public HHPagePanel getHhPgPanel() {
		return hhPgPanel;
	}

	public void setHhPgPanel(HHPagePanel hhPgPanel) {
		this.hhPgPanel = hhPgPanel;
	}

	public String getViewName() {
		return viewName;
	}

	public void setViewName(String viewName) {
		this.viewName = viewName;
	}
	

}
