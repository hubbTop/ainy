package com.hhdb.csadmin.plugin.view.service;

import java.util.List;
import java.util.Map;

import com.hhdb.csadmin.common.bean.SqlBean;
import com.hhdb.csadmin.common.event.CmdEvent;
import com.hhdb.csadmin.common.event.ErrorEvent;
import com.hhdb.csadmin.common.event.HHEvent;
import com.hhdb.csadmin.common.util.HHSqlUtil;
import com.hhdb.csadmin.plugin.view.HView;
import com.hhdb.csadmin.plugin.view.ViewOpenPanel;
/**
 * 处理数据
 *
 */
public class SqlOperationService {
	public HView hView ;
	
	public SqlOperationService (HView hView){
		this.hView = hView;
	}
	
	/**
	 * 刷新树节点
	 * @throws Exception
	 */
	public void refresh() throws Exception {
		String toId = "com.hhdb.csadmin.plugin.tree";
		CmdEvent tabPanelEvent = new CmdEvent(hView.PLUGIN_ID, toId, "RefreshAddTreeNodeEvent");
		tabPanelEvent.addProp("schemaName", hView.smName);
		tabPanelEvent.addProp("treenode_type", "view");
		HHEvent ev = hView.sendEvent(tabPanelEvent);
		if (ev instanceof ErrorEvent) {
			throw new Exception(((ErrorEvent) ev).getErrorMessage());
		}
	}
	
	/**
	 * 根据类别获取信息
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> getNameByType(HHSqlUtil.ITEM_TYPE type,String sqlType)throws Exception {
		Object[] params = new Object[1];
		params[0] = "'" + hView.smName + "'";
		SqlBean sb = HHSqlUtil.getSqlBean(type,sqlType);
		List<Map<String, Object>> rs = getListMap(sb.replaceParams(params));
		return rs;
	}
	
	/**
	 * 发送事件获取分页面板
	 * @param tabName
	 * @param viewPanel
	 * @param id
	 * @param bool  针对新建页面打开多个
	 * @throws Exception
	 */
	public void getTabPanelTable(String tabName, ViewOpenPanel viewPanel,int id,Boolean bool) throws Exception {
		String toId = "com.hhdb.csadmin.plugin.tabpane";
		CmdEvent tabPanelEvent = new CmdEvent(hView.PLUGIN_ID, toId, "AddPanelEvent");
		tabPanelEvent.setObj(viewPanel);
		tabPanelEvent.addProp("ICO", "view.png");
		tabPanelEvent.addProp("TAB_TITLE", tabName);
		if(bool){
			tabPanelEvent.addProp("COMPONENT_ID", id+"");
		}else{
			tabPanelEvent.addProp("COMPONENT_ID",tabName);
		}
		HHEvent ev = hView.sendEvent(tabPanelEvent);
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
		CmdEvent obtainRowsEvent = new CmdEvent(hView.PLUGIN_ID, toId,"ExecuteListMapBySqlEvent");
		obtainRowsEvent.addProp("sql_str", sql);
		HHEvent rowEvent = hView.sendEvent(obtainRowsEvent);
		List<Map<String, Object>> rowStr = null;
		if (rowEvent instanceof ErrorEvent) {
			throw new Exception(((ErrorEvent) rowEvent).getErrorMessage());
		} else {
			rowStr = (List<Map<String, Object>>) rowEvent.getObj();
		}
		return rowStr;
	}
	
	/**
	 * 发送事件得到List< List< Object > >格式数据
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<List<Object>> getListList(String sql) throws Exception {
		String toId = "com.hhdb.csadmin.plugin.conn";
		CmdEvent obtainRowsEvent = new CmdEvent(hView.PLUGIN_ID, toId,"ExecuteListBySqlEvent");
		obtainRowsEvent.addProp("sql_str", sql);
		HHEvent rowEvent = hView.sendEvent(obtainRowsEvent);
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
	public String sqlOperation(String sql) throws Exception{
		String toId = "com.hhdb.csadmin.plugin.conn";
		CmdEvent obtainRowsEvent = new CmdEvent(hView.PLUGIN_ID, toId,"ExecuteUpdateBySqlEvent");
		obtainRowsEvent.addProp("sql_str", sql);
		HHEvent rowEvent = hView.sendEvent(obtainRowsEvent);
		String rowStr = null;
		if (rowEvent instanceof ErrorEvent) {
			throw new Exception(((ErrorEvent) rowEvent).getErrorMessage());
		} else {
			rowStr = rowEvent.getValue("res");
		}
		return rowStr;
	}
	
}
