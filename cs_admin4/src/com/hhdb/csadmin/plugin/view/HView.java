package com.hhdb.csadmin.plugin.view;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import com.hhdb.csadmin.common.base.AbstractPlugin;
import com.hhdb.csadmin.common.event.ErrorEvent;
import com.hhdb.csadmin.common.event.EventTypeEnum;
import com.hhdb.csadmin.common.event.HHEvent;
import com.hhdb.csadmin.common.util.EventUtil;
import com.hhdb.csadmin.plugin.view.service.SqlOperationService;

/**
 * 视图处理
 * 
 * @author hhxd
 * 
 */
public class HView extends AbstractPlugin {
	
	public int s = 1;
	
	public String PLUGIN_ID = HView.class.getPackage().getName();

	public SqlOperationService sosi;

	// 打开表分页面板
	private ViewOpenPanel viewPanel;
	// 每页显示的数据条数
	public final int inNum = 5;
	// 模式+视图名称
	private String tableName;
	// 查询视图第N页数据sql
	private String nextPageData = "select *,true upd from %s limit " + inNum
			+ " offset %d*" + inNum;
	// 当前页码数
	private int pageNum;
	// 模式名
	public String smName = "";
	// 数据库名
	public String dbName = "";

	public HView() {
		sosi = new SqlOperationService(this);
	}

	/**
	 * 重写插件接收事件
	 */
	@Override
	public HHEvent receEvent(HHEvent event) {
		HHEvent hevent = EventUtil.getReplyEvent(HView.class, event);
		if (event.getType().equals(EventTypeEnum.CMD.name())) {
			try {
				if (event.getValue("CMD").equals("viewOpenEvent")) { // 打开视图
					this.dbName = event.getValue("databaseName");
					this.smName = event.getValue("schemaName");
					viewPanel = new ViewOpenPanel(this);
					String viewName = event.getValue("viewName");
					// 发送sql,获取数据,展示第一页数据
					tableName = "\""+smName + "\".\"" + viewName + "\"";
					List<List<Object>> list = pageTable(nextPageData, 0);
					viewPanel.initPanel(list);
					String tabName = "打开视图" + "(" + smName + "." + viewName + ")";
					sosi.getTabPanelTable(tabName, viewPanel,s,false);// 获取分页面板
					buttonAction();
					showPageCount();
				} else if (event.getValue("CMD").equals("viewAddEvent")) { // 新建视图
					this.dbName = event.getValue("databaseName");
					this.smName = event.getValue("schemaName");
					unwrap();
					s++;
				} else if (event.getValue("CMD").equals("viewEditEvent")) { // 修改视图
					this.dbName = event.getValue("databaseName");
					this.smName = event.getValue("schemaName");
					viewPanel = new ViewOpenPanel(this);
					String viewName = event.getValue("viewName");
					viewPanel.setViewName(viewName);
					viewPanel.viewsTabPanelHandle(true);
					String tabName = "修改视图" + "(" + smName + "." + viewName + ")";
					sosi.getTabPanelTable(tabName, viewPanel,s,false);// 获取分页面板
				}
			} catch (Exception e) {
				JOptionPane.showMessageDialog(null, "错误信息：" + e.getMessage(),
						"错误", JOptionPane.ERROR_MESSAGE);
				return hevent;
			}
			return hevent;
		} else {
			ErrorEvent errorEvent = new ErrorEvent(PLUGIN_ID,event.getFromID(),ErrorEvent.ErrorType.EVENT_NOT_VALID.name());
			errorEvent.setErrorMessage(PLUGIN_ID + "不能接受如下事件:\n"+ event.toString());
			return errorEvent;
		}
	}

	/**
	 * 新建
	 * @param dbName
	 * @param smName
	 */
	public void unwrap() throws Exception {
		viewPanel = new ViewOpenPanel(this);
		viewPanel.viewsTabPanelHandle(false);
		String tabName = "新建视图" + "(" + dbName + "." + smName + ")";
		sosi.getTabPanelTable(tabName, viewPanel,s,true);
	}
	
	/**
	 * 展示页码
	 */
	private void showPageCount() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					// 先获取表总行数
					String totalRowsSql = "select count(1) from	" + tableName;
					List<List<Object>> rowStr = sosi.getListList(totalRowsSql);
					List<Object> rowStrList = rowStr.get(1);
					int rowCount = Integer.parseInt(rowStrList.get(0)+"");
					int intCount = rowCount / inNum;
					int modCount = rowCount % inNum;
					// 总页码数
					int pageCount;
					if (modCount != 0) {
						pageCount = intCount + 1;
					} else {
						pageCount = intCount;
					}
					viewPanel.getHhPgPanel().setPageCount(pageCount);
					viewPanel.getHhPgPanel().getTotalPageNum()
							.setText(pageCount + "");
					if (pageCount <= 1) {
						viewPanel.getHhPgPanel().getNextPage()
								.setEnabled(false);
						viewPanel.getHhPgPanel().getLastPage()
								.setEnabled(false);
					}
				} catch (IOException e) {
					e.printStackTrace();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();
	}

	/**
	 * 发送sql请求数据
	 * 
	 * @param pageData
	 *            sql
	 * @param num
	 * @return
	 */
	private List<List<Object>> pageTable(String pageData, int num) {
		List<List<Object>> list = new ArrayList<>();
		try {
			String pageDatas;
			if (num == 0) {
				pageDatas = String.format(pageData, tableName, 0);
			} else {
				pageDatas = String.format(pageData, tableName, num);
			}
			list = sosi.getListList(pageDatas);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}


	/**
	 * 面板上的按钮点击事件
	 */
	private void buttonAction() {
		// 点击第一页按钮回到第一页
		viewPanel.getHhPgPanel().getFirstPage()
				.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						int cPNum = Integer.parseInt(viewPanel.getHhPgPanel()
								.getCurPageNum().getText());
						if (cPNum <= 1) {
							return;
						}
						List<List<Object>> list = pageTable(nextPageData, 0);
						viewPanel.excutepage(list);
						viewPanel.getHhPgPanel().getCurPageNum().setText("1");
						viewPanel.getHhPgPanel().getFirstPage()
								.setEnabled(false);
						viewPanel.getHhPgPanel().getPrePage().setEnabled(false);
						if (viewPanel.getHhPgPanel().getPageCount() > 1) {
							viewPanel.getHhPgPanel().getNextPage()
									.setEnabled(true);
							viewPanel.getHhPgPanel().getLastPage()
									.setEnabled(true);
						}
					}
				});
		// 点击上一页按钮回到前一页
		viewPanel.getHhPgPanel().getPrePage()
				.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						int cPNum = Integer.parseInt(viewPanel.getHhPgPanel()
								.getCurPageNum().getText());
						if (cPNum <= 1) {
							return;
						}
						List<List<Object>> list = pageTable(nextPageData,
								cPNum - 2);
						viewPanel.excutepage(list);
						cPNum--;
						viewPanel.getHhPgPanel().getCurPageNum()
								.setText(cPNum + "");
						if (cPNum <= 1) {
							viewPanel.getHhPgPanel().getFirstPage()
									.setEnabled(false);
							viewPanel.getHhPgPanel().getPrePage()
									.setEnabled(false);
						}
						if (viewPanel.getHhPgPanel().getPageCount() > 1) {
							viewPanel.getHhPgPanel().getNextPage()
									.setEnabled(true);
							viewPanel.getHhPgPanel().getLastPage()
									.setEnabled(true);
						}
					}
				});
		// 保存当前页
		viewPanel.getHhPgPanel().getCurPageNum()
				.addMouseListener(new MouseAdapter() {
					@Override
					public void mousePressed(MouseEvent e) {
						pageNum = Integer.parseInt(viewPanel.getHhPgPanel()
								.getCurPageNum().getText());
					}

				});
		// 输入页码跳到当前输入页
		viewPanel.getHhPgPanel().getCurPageNum()
				.addKeyListener(new KeyAdapter() {
					@Override
					public void keyReleased(KeyEvent e) {
						SwingUtilities.invokeLater(new Runnable() {
							@Override
							public void run() {
								char[] ch = viewPanel.getHhPgPanel()
										.getCurPageNum().getText()
										.toCharArray();
								for (char c : ch) {
									if (!Character.isDigit(c)) {
										JOptionPane.showMessageDialog(
												viewPanel, "请输入正确的页码");
										viewPanel.getHhPgPanel()
												.getCurPageNum()
												.setText(pageNum + "");
										return;
									}
								}
								int cPNum = Integer.parseInt(viewPanel
										.getHhPgPanel().getCurPageNum()
										.getText());
								if (cPNum > viewPanel.getHhPgPanel()
										.getPageCount()) {
									JOptionPane.showMessageDialog(viewPanel,
											"请输入正确的页码");
									viewPanel.getHhPgPanel().getCurPageNum()
											.setText(pageNum + "");
									return;
								}
								List<List<Object>> nextPageTable = pageTable(nextPageData,
										cPNum - 1);
								viewPanel.excutepage(nextPageTable);
								if (cPNum <= 1) {
									viewPanel.getHhPgPanel().getFirstPage()
											.setEnabled(false);
									viewPanel.getHhPgPanel().getPrePage()
											.setEnabled(false);
								} else {
									viewPanel.getHhPgPanel().getFirstPage()
											.setEnabled(true);
									viewPanel.getHhPgPanel().getPrePage()
											.setEnabled(true);
								}
								if (cPNum >= viewPanel.getHhPgPanel()
										.getPageCount()) {
									viewPanel.getHhPgPanel().getNextPage()
											.setEnabled(false);
									viewPanel.getHhPgPanel().getLastPage()
											.setEnabled(false);
								} else {
									viewPanel.getHhPgPanel().getNextPage()
											.setEnabled(true);
									viewPanel.getHhPgPanel().getLastPage()
											.setEnabled(true);
								}
							}
						});
					}
				});
		// 点击下一页按钮回到下一页
		viewPanel.getHhPgPanel().getNextPage()
				.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						int cPNum = Integer.parseInt(viewPanel.getHhPgPanel()
								.getCurPageNum().getText());
						if (cPNum >= viewPanel.getHhPgPanel().getPageCount()) {
							return;
						}
						List<List<Object>> nextPageTable = pageTable(nextPageData, cPNum);
						viewPanel.excutepage(nextPageTable);
						cPNum++;
						viewPanel.getHhPgPanel().getCurPageNum()
								.setText(cPNum + "");
						if (cPNum > 1) {
							viewPanel.getHhPgPanel().getFirstPage()
									.setEnabled(true);
							viewPanel.getHhPgPanel().getPrePage()
									.setEnabled(true);
						}
						if (cPNum >= viewPanel.getHhPgPanel().getPageCount()) {
							viewPanel.getHhPgPanel().getNextPage()
									.setEnabled(false);
							viewPanel.getHhPgPanel().getLastPage()
									.setEnabled(false);
						}
					}
				});
		// 点击最后一页按钮回到最后一页
		viewPanel.getHhPgPanel().getLastPage()
				.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						SwingUtilities.invokeLater(new Runnable() {
							@Override
							public void run() {
								int tPNum = viewPanel.getHhPgPanel()
										.getPageCount();
								if (tPNum <= 1) {
									return;
								}
								List<List<Object>> lastPageTable = pageTable(nextPageData,
										tPNum - 1);
								viewPanel.excutepage(lastPageTable);
								viewPanel.getHhPgPanel().getCurPageNum()
										.setText(tPNum + "");
								if (tPNum > 1) {
									viewPanel.getHhPgPanel().getFirstPage()
											.setEnabled(true);
									viewPanel.getHhPgPanel().getPrePage()
											.setEnabled(true);
								}
								viewPanel.getHhPgPanel().getNextPage()
										.setEnabled(false);
								viewPanel.getHhPgPanel().getLastPage()
										.setEnabled(false);
							}
						});
					}
				});
	}

	@Override
	public Component getComponent() {
		return null;
	}
}
