package com.hhdb.csadmin.plugin.view.ui;

import java.awt.BorderLayout;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;

import com.hhdb.csadmin.plugin.view.HView;

/**
 * 结果Tabbe面板
 * @author hhxd
 *
 */
public class OutTabPanel extends BaseTabbedPaneCustom{
	public HView hv;
	private static final long serialVersionUID = 1L;
	
	public OutTabPanel(){
		super(JTabbedPane.TOP);
		setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
		setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
	}
	
	/**
	 * 异常情况
	 * @param text
	 */
	public void setMessage(String text) {
		JPanel messagePanel = new JPanel(); 	//结果面板
		messagePanel.setLayout(new BorderLayout());
		messagePanel.setBorder(null);
		
		JTextArea message = new JTextArea(); //结果
		message.setEditable(false); //不可编辑
		
		messagePanel.add(message);
		
		message.setText(text);
		add(messagePanel,"消息",0);
	}
	
	/**
	 * 正常执行sql
	 * @param sql
	 * @throws BaseException
	 */
	public void addShowData(String sql) throws Exception{
		OutDatagridPanel datagridPanel=new OutDatagridPanel(sql,hv);
		datagridPanel.loadData();
		add(datagridPanel, "结果集",0);
	}
	
	/**
	 * 清除里面所有列表
	 */
	public void cleanTab(){
		removeAll();
	}
}
