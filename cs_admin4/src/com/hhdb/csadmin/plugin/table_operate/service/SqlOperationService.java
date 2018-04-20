package com.hhdb.csadmin.plugin.table_operate.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.JPanel;

import com.hhdb.csadmin.common.bean.ServerBean;
import com.hhdb.csadmin.common.bean.SqlBean;
import com.hhdb.csadmin.common.event.CmdEvent;
import com.hhdb.csadmin.common.event.ErrorEvent;
import com.hhdb.csadmin.common.event.HHEvent;
import com.hhdb.csadmin.common.util.HHSqlUtil;
import com.hhdb.csadmin.common.util.HHSqlUtil.ITEM_TYPE;
import com.hhdb.csadmin.common.util.StartUtil;
import com.hhdb.csadmin.plugin.table_operate.TableEdit;
/**
 * 处理数据
 *
 */
public class SqlOperationService {
	public TableEdit tbe ;
	
	public SqlOperationService (TableEdit tbe){
		this.tbe = tbe;
	}
	
	/**
	 * 刷新树节点
	 * @throws Exception
	 */
	public void refresh(String schemaName) throws Exception {
		String toId = "com.hhdb.csadmin.plugin.tree";
		CmdEvent tabPanelEvent = new CmdEvent(tbe.PLUGIN_ID, toId, "RefreshAddTreeNodeEvent");
		tabPanelEvent.addProp("schemaName", schemaName);
		tabPanelEvent.addProp("treenode_type", "table");
		HHEvent ev = tbe.sendEvent(tabPanelEvent);
		if (ev instanceof ErrorEvent) {
			throw new Exception(((ErrorEvent) ev).getErrorMessage());
		}
	}
	
	/**
	 * 发送事件获取分页面板
	 * @param tabName
	 * @param viewPanel
	 */
	public void getTabPanelTable(String id,String tabName, JPanel jp) throws Exception {
		String toId = "com.hhdb.csadmin.plugin.tabpane";
		CmdEvent tabPanelEvent = new CmdEvent(tbe.PLUGIN_ID, toId, "AddPanelEvent");
		tabPanelEvent.setObj(jp);
		tabPanelEvent.addProp("ICO", "tableindex.png");
		tabPanelEvent.addProp("TAB_TITLE", tabName);
		tabPanelEvent.addProp("COMPONENT_ID", id);
		HHEvent ev = tbe.sendEvent(tabPanelEvent);
		if (ev instanceof ErrorEvent) {
			throw new Exception(((ErrorEvent) ev).getErrorMessage());
		}
	}
	
	/**
	 * 关闭页面
	 * @param id
	 * @throws Exception
	 */
	public void closePane(String id) throws Exception {
		String toId = "com.hhdb.csadmin.plugin.tabpane";
		CmdEvent tabPanelEvent = new CmdEvent(tbe.PLUGIN_ID, toId, "closePane");
		tabPanelEvent.addProp("id", id);
		HHEvent ev = tbe.sendEvent(tabPanelEvent);
		if (ev instanceof ErrorEvent) {
			throw new Exception(((ErrorEvent) ev).getErrorMessage());
		}
	}
	
	/**
	 * 发送事件得到List< Map< String, Object > >格式数据
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getListMap(String sql) throws Exception {
		String toId = "com.hhdb.csadmin.plugin.conn";
		CmdEvent obtainRowsEvent = new CmdEvent(tbe.PLUGIN_ID, toId,"ExecuteListMapBySqlEvent");
		obtainRowsEvent.addProp("sql_str", sql);
		HHEvent rowEvent = tbe.sendEvent(obtainRowsEvent);
		List<Map<String, Object>> rowStr = null;
		if (rowEvent instanceof ErrorEvent) {
			throw new Exception(((ErrorEvent) rowEvent).getErrorMessage());
		} else {
			rowStr = (List<Map<String, Object>>) rowEvent.getObj();
		}
		return rowStr;
	}
	
	/**
	 * 根据xml参数类别获取List< Map< String, Object > >格式数据
	 * @param itemType
	 * @param type
	 * @param params
	 * @return 
	 */
	public List<Map<String, Object>> getListBySql(ITEM_TYPE itemType, String type, Object[] params)  {
		List<Map<String, Object>> list = new ArrayList<>();
		try {
			SqlBean sqlBean = HHSqlUtil.getSqlBean(itemType, type);
			String sql;
			if (params != null && params.length > 0) {
				sql = sqlBean.replaceParams(params);
			} else {
				sql = sqlBean.getSql();
			}
			list = getListMap(sql);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	
	/**
	 * 发送事件得到List< List< Object > >格式数据
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<List<Object>> getListList(String sql) throws Exception {
		String toId = "com.hhdb.csadmin.plugin.conn";
		CmdEvent obtainRowsEvent = new CmdEvent(tbe.PLUGIN_ID, toId,"ExecuteListBySqlEvent");
		obtainRowsEvent.addProp("sql_str", sql);
		HHEvent rowEvent = tbe.sendEvent(obtainRowsEvent);
		List<List<Object>> rowStr = null;
		if (rowEvent instanceof ErrorEvent) {
			throw new Exception(((ErrorEvent) rowEvent).getErrorMessage());
		} else {
			rowStr = (List<List<Object>>) rowEvent.getObj();
		}
		return rowStr;
	}
	
	
	/**
	 * 发送事件执行增，删，改sql的事件
	 * @return
	 */
	public String sqlOperation(String sql) throws Exception {
		String toId = "com.hhdb.csadmin.plugin.conn";
		CmdEvent obtainRowsEvent = new CmdEvent(tbe.PLUGIN_ID, toId,"ExecuteUpdateBySqlEvent");
		obtainRowsEvent.addProp("sql_str", sql);
		HHEvent rowEvent = tbe.sendEvent(obtainRowsEvent);
		String rowStr = null;
		if (rowEvent instanceof ErrorEvent) {
			throw new Exception(((ErrorEvent) rowEvent).getErrorMessage());
		} else {
			rowStr = rowEvent.getValue("res");
		}
		return rowStr;
	}
	
	/**
	 * 获取ServerBean
	 * @return
	 */
	public ServerBean getServerBean() throws Exception {
		String toId = "com.hhdb.csadmin.plugin.conn";
		CmdEvent obtainRowsEvent = new CmdEvent(tbe.PLUGIN_ID, toId,"GetServerBean");
		HHEvent rowEvent = tbe.sendEvent(obtainRowsEvent);
		ServerBean sb = null;
		if (rowEvent instanceof ErrorEvent) {
			throw new Exception(((ErrorEvent) rowEvent).getErrorMessage());
		} else {
			sb = (ServerBean) rowEvent.getObj();
		}
		return sb;
	}
	
	/**
	 * 获取索引字段
	 */
	public String getIndexColumn(int relid, int indkey) throws Exception {
		List<Map<String, Object>> list = getListBySql(ITEM_TYPE.TABLE, "indexdef",new String[] { relid + "", indkey + "" });
		return list.get(0).get(""+StartUtil.prefix+"_get_indexdef").toString();
	}
	
	/**
	 * 空数据处理
	 * @param value
	 * @return
	 */
	public  String nullOfStr(Object value) {
		if (value == null) {
			return "";
		}
		return value.toString();
	}
}
