package com.hhdb.csadmin.plugin.table_open;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.table.TableColumn;

import org.apache.commons.lang3.StringUtils;

import com.hhdb.csadmin.plugin.table_open.service.SqlOperationService;
import com.hhdb.csadmin.plugin.table_open.ui.HHOperateTablePanel;
import com.hhdb.csadmin.plugin.table_open.ui.HHPagePanel;
import com.hhdb.csadmin.plugin.table_open.ui.HHTable;
import com.hhdb.csadmin.plugin.table_open.ui.HHTableColumnCellRenderer;
import com.hhdb.csadmin.plugin.table_open.ui.HHTableDefaultCellRenderer;
import com.hhdb.csadmin.plugin.table_open.ui.HHTablePanel;
import com.hhdb.csadmin.plugin.table_open.ui.HHTableSelectedRowRenderer;

/**
 * 打开表操作面板
 */
public class TableOpenPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	public String databaseName; // 数据库名
	public String schemaName; // 模式名
	public String table; // 表名
	public SqlOperationService sqls;
	public int j = 0; // 面板id

	private TableOpen tabo;

	private HHTablePanel hhTbPanel;
	private HHOperateTablePanel hhOpTbPanel;
	private HHPagePanel hhPgPanel;
	// 每页显示的数据条数
	public final int inNum = 30;
	// 模式+表名称
	private String tableName;
	// 查询表第N页数据sql
	private String nextPageData = "select *,true upd,ctid from %s limit "
			+ inNum + " offset %d*" + inNum;
	// 当前页码数
	private int pageNum;
	// 表数据修改前的集合
	private List<List<Object>> oldValueLists = new ArrayList<>();
	// 表数据修改后的集合
	private Map<List<Object>, Object> newValueMap = new HashMap<List<Object>, Object>();
	// 选中表格数据的当前所在行数
	private int row = 0;
	// 选中表格数据的当前所在列数
	private int column = 0;

	public TableOpenPanel(String databaseName, String schemaName, String table,String tableName, TableOpen tableOpen) {
		this.databaseName = databaseName;
		this.schemaName = schemaName;
		this.table = table;
		this.tabo = tableOpen;
		this.tableName = tableName;
		sqls = new SqlOperationService(tabo);
		initPanel();
		List<List<Object>> firstPageTable = pageTable(nextPageData, 0);
		excutepage(firstPageTable);
		showPageCount();
		buttonAction();
	}

	// 初始化面板
	public void initPanel() {
		this.setLayout(new GridBagLayout());
		this.setBorder(BorderFactory.createLineBorder(new Color(178, 178, 178),1));
		hhTbPanel = new HHTablePanel(true, this);
		hhTbPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
		hhTbPanel.getBaseTable().setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		hhOpTbPanel = new HHOperateTablePanel();
		hhPgPanel = new HHPagePanel();
		add(hhTbPanel, new GridBagConstraints(0, 0, 2, 1, 1.0, 1.0,GridBagConstraints.NORTH, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
		add(hhOpTbPanel, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0,10, 0, 0), 0, 0));
		add(hhPgPanel, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0,0, 0, 30), 0, 0));
	}

	// 表格填充数据
	public void excutepage(List<List<Object>> pageTable) {
		hhTbPanel.setBorder(BorderFactory.createEmptyBorder());
		hhTbPanel.getBaseTable().setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		// 表头不能拖动
		hhTbPanel.getBaseTable().getTableHeader().setReorderingAllowed(false);
		// hhTbPanel.getBaseTable().removeAll();
		hhTbPanel.getBaseTable().setDefaultRenderer(Object.class,new HHTableDefaultCellRenderer());
		Vector<String> colname = new Vector<String>();
		for (Object field : pageTable.get(0)) {
			colname.add(field.toString());
		}
		List<String> list = new ArrayList<>();
		list.add("");
		list.addAll(colname);

		hhTbPanel.setDataList(list, pageTable);

		TableColumn index = hhTbPanel.getBaseTable().getColumnModel()
				.getColumn(0);
		index.setWidth(20);
		index.setPreferredWidth(20);
		index.setCellRenderer(new HHTableColumnCellRenderer());

		// 隐藏后面两列 upd ctid
		TableColumn coln = hhTbPanel.getBaseTable().getColumnModel().getColumn(list.size() - 1);
		coln.setMinWidth(0);
		coln.setMaxWidth(0);
		coln.setWidth(0);
		coln.setPreferredWidth(0);

		TableColumn coln1 = hhTbPanel.getBaseTable().getColumnModel().getColumn(list.size() - 2);
		coln1.setMinWidth(0);
		coln1.setMaxWidth(0);
		coln1.setWidth(0);
		coln1.setPreferredWidth(0);

		//亮显示一行数据
		hhTbPanel.getBaseTable().addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				// hhTbPanel.getBaseTable().setDefaultRenderer(Object.class, new
				// HHTableDefaultCellRenderer());
				int row = hhTbPanel.getBaseTable().getSelectedRow();
				hhTbPanel.getBaseTable().setDefaultRenderer(Object.class,new HHTableSelectedRowRenderer(row));
			}
		});
		//头部点击事件
		hhTbPanel.getBaseTable().getTableHeader().addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				//去除选择行与选择行变色
				hhTbPanel.getBaseTable().clearSelection();
				hhTbPanel.getBaseTable().setDefaultRenderer(Object.class,new HHTableDefaultCellRenderer());  //设置隔行变色
			}
		});
	}

	/**
	 * 发送sql请求数据
	 */
	private List<List<Object>> pageTable(String pageData, int num) {
		List<List<Object>> pageTb = new ArrayList<>();
		try {
			String pageDatas;
			if (num == 0) {
				pageDatas = String.format(pageData, tableName, 0);
			} else {
				pageDatas = String.format(pageData, tableName, num);
			}
			pageTb = sqls.getListList(pageDatas);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return pageTb;
	}

	private void showPageCount() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					// 先获取表总行数
					String totalRowsSql = "select count(1) from	" + tableName;
					List<Object> rowStrList = sqls.getListList(
							totalRowsSql).get(1);
					int rowCount = Integer.parseInt(rowStrList.get(0) + "");
					int intCount = rowCount / inNum;
					int modCount = rowCount % inNum;
					// 总页码数
					int pageCount;
					if (modCount != 0) {
						pageCount = intCount + 1;
					} else {
						pageCount = intCount;
					}
					hhPgPanel.setPageCount(pageCount);
					hhPgPanel.getTotalPageNum().setText(pageCount + "");
					if (pageCount <= 1) {
						hhPgPanel.getNextPage().setEnabled(false);
						hhPgPanel.getLastPage().setEnabled(false);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();
	}

	/**
	 * 刷新
	 */
	public void refresh() {
		String curPageNum = hhPgPanel.getCurPageNum().getText();
		int cPNum = pageNum;
		if (!"".equals(curPageNum)) {
			cPNum = Integer.parseInt(curPageNum);
		}
		hhPgPanel.getCurPageNum().setText(cPNum + "");
		List<List<Object>> nextPageTable = pageTable(nextPageData, cPNum - 1);
		excutepage(nextPageTable);
		hhOpTbPanel.getSaveData().setEnabled(true);
		oldValueLists.clear();
		newValueMap.clear();
	}

	/**
	 * 面板上的按钮点击事件
	 */
	private void buttonAction() {
		// 点击第一页按钮回到第一页
		hhPgPanel.getFirstPage().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				List<List<Object>> firstPageTable = pageTable(nextPageData, 0);
				excutepage(firstPageTable);
				hhPgPanel.getCurPageNum().setText("1");
				hhPgPanel.getFirstPage().setEnabled(false);
				hhPgPanel.getPrePage().setEnabled(false);
				if (hhPgPanel.getPageCount() > 1) {
					hhPgPanel.getNextPage().setEnabled(true);
					hhPgPanel.getLastPage().setEnabled(true);
				}
			}
		});
		// 点击上一页按钮回到前一页
		hhPgPanel.getPrePage().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String curPageNum = hhPgPanel.getCurPageNum().getText();
				int cPNum = pageNum;
				if (!"".equals(curPageNum)) {
					cPNum = Integer.parseInt(curPageNum);
				}
				List<List<Object>> prePageTable = pageTable(nextPageData,cPNum - 2);
				excutepage(prePageTable);
				cPNum--;
				hhPgPanel.getCurPageNum().setText(cPNum + "");
				if (cPNum <= 1) {
					hhPgPanel.getFirstPage().setEnabled(false);
					hhPgPanel.getPrePage().setEnabled(false);
				}
				if (hhPgPanel.getPageCount() > 1) {
					hhPgPanel.getNextPage().setEnabled(true);
					hhPgPanel.getLastPage().setEnabled(true);
				}
			}
		});
		// 保存当前页
		hhPgPanel.getCurPageNum().addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				String text = hhPgPanel.getCurPageNum().getText();
				if (text != null && !"".equals(text) && !"0".equals("")) {
					pageNum = Integer.parseInt(hhPgPanel.getCurPageNum().getText());
				}
			}

		});
		// 输入页码跳到当前输入页
		hhPgPanel.getCurPageNum().addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				SwingUtilities.invokeLater(new Runnable() {
					@Override
					public void run() {
						String curPageNum = hhPgPanel.getCurPageNum().getText();
						if ("".equals(curPageNum)) {
							return;
						}
						// 判断每个字符是不是数字
						char[] ch = curPageNum.toCharArray();
						for (char c : ch) {
							if (!Character.isDigit(c)) {
								JOptionPane.showMessageDialog(null, "请输入正确的页码");
								hhPgPanel.getCurPageNum().setText(pageNum + "");
								return;
							}
						}

						// 判断输入的页数是不是大于总页数
						int cPNum = Integer.parseInt(curPageNum);
						if (cPNum > hhPgPanel.getPageCount() || cPNum <= 0) {
							JOptionPane.showMessageDialog(null, "请输入正确的页码");
							hhPgPanel.getCurPageNum().setText(pageNum + "");
							return;
						}
						List<List<Object>> nextPageTable = pageTable(
								nextPageData, cPNum - 1);
						excutepage(nextPageTable);
						if (cPNum <= 1) {
							hhPgPanel.getFirstPage().setEnabled(false);
							hhPgPanel.getPrePage().setEnabled(false);
						} else {
							hhPgPanel.getFirstPage().setEnabled(true);
							hhPgPanel.getPrePage().setEnabled(true);
						}
						if (cPNum >= hhPgPanel.getPageCount()) {
							hhPgPanel.getNextPage().setEnabled(false);
							hhPgPanel.getLastPage().setEnabled(false);
						} else {
							hhPgPanel.getNextPage().setEnabled(true);
							hhPgPanel.getLastPage().setEnabled(true);
						}
					}
				});
			}
		});
		// 点击下一页按钮回到下一页
		hhPgPanel.getNextPage().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String curPageNum = hhPgPanel.getCurPageNum().getText();
				int cPNum = pageNum;
				if (!"".equals(curPageNum)) {
					cPNum = Integer.parseInt(curPageNum);
				}
				List<List<Object>> nextPageTable = pageTable(nextPageData,cPNum);
				excutepage(nextPageTable);
				cPNum++;
				hhPgPanel.getCurPageNum().setText(cPNum + "");
				if (cPNum > 1) {
					hhPgPanel.getFirstPage().setEnabled(true);
					hhPgPanel.getPrePage().setEnabled(true);
				}
				if (cPNum >= hhPgPanel.getPageCount()) {
					hhPgPanel.getNextPage().setEnabled(false);
					hhPgPanel.getLastPage().setEnabled(false);
				}
			}
		});
		// 点击最后一页按钮回到最后一页
		hhPgPanel.getLastPage().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				SwingUtilities.invokeLater(new Runnable() {
					@Override
					public void run() {
						int tPNum = hhPgPanel.getPageCount();
						if (tPNum <= 1) {
							return;
						}
						List<List<Object>> lastPageTable = pageTable(nextPageData, tPNum - 1);
						excutepage(lastPageTable);
						hhPgPanel.getCurPageNum().setText(tPNum + "");
						if (tPNum > 1) {
							hhPgPanel.getFirstPage().setEnabled(true);
							hhPgPanel.getPrePage().setEnabled(true);
						}
						hhPgPanel.getNextPage().setEnabled(false);
						hhPgPanel.getLastPage().setEnabled(false);
					}
				});
			}
		});
		// 点击添加按钮添加一个空行
		hhOpTbPanel.getAddRow().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				HHTable table = hhTbPanel.getBaseTable();
				Object[] object = new Object[table.getColumnCount()];
				int selectedRow = table.getSelectedRow();
				if (selectedRow == -1) {
//					hhTbPanel.getTableDataModel().insertRow(0, object);
					hhTbPanel.getTableDataModel().addRow(object);
				} else {
//					hhTbPanel.getTableDataModel().insertRow(selectedRow, object);
					hhTbPanel.getTableDataModel().addRow(object);
				}
			}
		});
		// 点击删除按钮删除所选行07 12
		hhOpTbPanel.getDelRow().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				HHTable table = hhTbPanel.getBaseTable();
				final int[] selectedRows = table.getSelectedRows();
				if(null != selectedRows && selectedRows.length != 0){    //判断是否选择了行
					int option = JOptionPane.showConfirmDialog(hhTbPanel,"是否删除已选择行", "提示信息", JOptionPane.YES_NO_OPTION);
					if (option == JOptionPane.YES_OPTION) {
						SwingUtilities.invokeLater(new Runnable() {
							@Override
							public void run() {
								if (selectedRows != null) {
									String delSql = "delete from " + tableName+ " where ctid='%s'";
									int totalColumnCount = hhTbPanel.getBaseTable().getColumnCount();
									for (int i = 0; i < selectedRows.length; ++i) {
										String upd = hhTbPanel.getBaseTable().getValueAt(selectedRows[i],totalColumnCount - 2)+ "";
										if (StringUtils.isNoneBlank(upd)&& ("t".equalsIgnoreCase(upd) || "true".equalsIgnoreCase(upd))) {
											String ctid = hhTbPanel.getBaseTable().getValueAt(selectedRows[i],totalColumnCount - 1).toString();
											String message = sqls.sendConn4Update(String.format(delSql, ctid));
											if (StringUtils.isBlank(message)) {
												return;
											}
										}
									}
									refresh();
								}
							}
						});
					}
				}
				
			}
		});
		// 点击刷新按钮刷新当前页面
		hhOpTbPanel.getRefreshData().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				refresh();
			}
		});

		// 点击保存按钮保存修改
		hhOpTbPanel.getSaveData().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				SwingUtilities.invokeLater(new Runnable() {
					@Override
					public void run() {
						try {
							//当在编辑某单元格时，点击保存则直接终止编辑获取当前编辑格已输入的值进行保存
							if (hhTbPanel.getBaseTable().isEditing()) {
								hhTbPanel.getBaseTable().getCellEditor().stopCellEditing();
								getEditedValues();
							}
							if(oldValueLists.size() == 0 ){ 
								return;
							}
							
							int totalColumnCount = hhTbPanel.getBaseTable().getColumnCount();
							String updateSql = "update " + tableName+ " set %s where ctid='%s'";
							String insertSql = "insert into " + tableName+ " (%s) values (%s)";
							List<List<Object>> newRowList = new ArrayList<>();
							List<Integer> rowNumList = new ArrayList<>();
							for (List<Object> cell : newValueMap.keySet()) {
								String column = hhTbPanel.getBaseTable().getColumnName((int) cell.get(1));
								Object ob = hhTbPanel.getBaseTable().getValueAt((int) cell.get(0),totalColumnCount - 2);
								String upd = null == ob ? null : ob.toString();
								if (StringUtils.isNoneBlank(upd)&& ("t".equalsIgnoreCase(upd) || "true".equalsIgnoreCase(upd))) {
									// 修改sql    
									String ctid = hhTbPanel.getTableDataModel().getValueAt((int) cell.get(0),totalColumnCount - 1).toString();
									String message = sqls.sendConn4Update(String.format(updateSql,column+ "="+ "'"+ newValueMap.get(cell)+ "'", ctid));
									if (StringUtils.isBlank(message)) {
										return;
									}
								} else {
									// 插入sql
									newRowList.add(cell);
									boolean flag = false;
									for (int rowNum : rowNumList) {
										if (rowNum == (int) cell.get(0)) {
											flag = true;
											break;
										}
									}
									if (!flag) {
										rowNumList.add((int) cell.get(0));
									}
								}

							}
							for (int rowNum : rowNumList) {
								StringBuffer columnBuffer = new StringBuffer();
								StringBuffer valueBuffer = new StringBuffer();
								for (List<Object> cell : newRowList) {
									if (rowNum == (int) cell.get(0)) {
										String column = hhTbPanel.getBaseTable().getColumnName((int) cell.get(1));columnBuffer.append(column);
										String value = (String) newValueMap.get(cell);
										if (value.contains("'")) {
											value = value.replaceAll("'", "''");
										}
										valueBuffer.append("'" + value + "'");
										columnBuffer.append(",");
										valueBuffer.append(",");
									}
								}
								String columnStr = columnBuffer.toString();
								String valueStr = valueBuffer.toString();
								columnStr = columnStr.substring(0,columnStr.length() - 1);
								valueStr = valueStr.substring(0,valueStr.length() - 1);
								String message = sqls.sendConn4Update(String.format(insertSql, columnStr, valueStr));
								if (StringUtils.isBlank(message)) {
									return;
								}
							}
							oldValueLists.clear();
							newValueMap.clear();
							JOptionPane.showMessageDialog(null, "保存成功！", "提示",JOptionPane.INFORMATION_MESSAGE);
						} catch (Exception e2) {
							e2.printStackTrace();
						}
					}
				});
			}
		});

		// 监听表格数据变更
		hhTbPanel.getBaseTable().addPropertyChangeListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				if ("tableCellEditor".equalsIgnoreCase(evt.getPropertyName().trim())) {
					SwingUtilities.invokeLater(new Runnable() {
						@Override
						public void run() {
							if (hhTbPanel.getBaseTable().isEditing()) {   //保存修改前的数据到数组
								row = hhTbPanel.getBaseTable().convertRowIndexToModel(hhTbPanel.getBaseTable().getEditingRow());
								column = hhTbPanel.getBaseTable().convertColumnIndexToModel(hhTbPanel.getBaseTable().getEditingColumn());
								if (row != -1 && column != -1) {
									Object oldValue = hhTbPanel.getBaseTable().getModel().getValueAt(row, column);
									if (oldValue == null) {
										oldValue = "";
									}
									List<Object> oldValueList = new ArrayList<>();
									boolean flag = false;
									for (List<Object> oldValues : oldValueLists) {
										if (row == (int) oldValues.get(0)&& column == (int) oldValues.get(1)) {
											flag = true;
											break;
										}
									}
									if (!flag) {
										oldValueList.add(row);
										oldValueList.add(column);
										oldValueList.add(oldValue);
										oldValueLists.add(oldValueList);
									}
								}
							} else {      	//保存修改后的数据到数组
								getEditedValues();
							}
						}
					});
				}
			}
		});
	}
	
	/**
	 * 获取单元格编辑后的值
	 */
	private void getEditedValues(){
		Object newValue = hhTbPanel.getBaseTable().getModel().getValueAt(row, column);
		if (newValue == null) {
			newValue = "";
		}
		List<Object> oldValues;
		Iterator<List<Object>> oldValueIter = oldValueLists.iterator();
		while (oldValueIter.hasNext()) {
			oldValues = oldValueIter.next();
			if (row == (int) oldValues.get(0)&& column == (int) oldValues.get(1)) {
				newValueMap.remove(oldValues);
				if (!newValue.equals(oldValues.get(2))) {
					newValueMap.put(oldValues,newValue);
				} else {
					oldValueLists.remove(oldValues);
				}
				break;
			}
		}
	}

}
