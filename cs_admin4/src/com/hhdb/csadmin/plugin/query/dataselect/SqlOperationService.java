package com.hhdb.csadmin.plugin.query.dataselect;


import com.hhdb.csadmin.common.event.ErrorEvent;
import com.hhdb.csadmin.common.event.EventTypeEnum;
import com.hhdb.csadmin.common.event.HHEvent;
import com.hhdb.csadmin.common.ui.BaseFrame;
import com.hhdb.csadmin.plugin.query.HQueryPlugin;
/**
 * 处理数据
 *
 */
public class SqlOperationService {
	private HQueryPlugin hquery ;
	public SqlOperationService(HQueryPlugin hquery){
		this.hquery=hquery;
	}
	public BaseFrame getBaseFrame() throws Exception {
		BaseFrame bf = null;
		String toID = "com.hhdb.csadmin.plugin.main";       
		HHEvent event = new HHEvent(hquery.PLUGIN_ID, toID, EventTypeEnum.GET_OBJ.name());
		HHEvent ev = hquery.sendEvent(event);
		if (ev instanceof ErrorEvent) {
			throw new Exception(((ErrorEvent) ev).getErrorMessage());
		}
		bf = (BaseFrame)ev.getObj();
		return bf;
	}
	
}
