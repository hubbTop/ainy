package com.hhdb.csadmin.common.base;


import java.awt.Component;

import com.hhdb.csadmin.common.event.HHEvent;

public abstract class AbstractPlugin {
	protected PluginBean pluginBean;
	protected IEventRoute eventRoute;
	
	public  void init(IEventRoute eRoute,PluginBean pluginBean){
		this.eventRoute=eRoute;
		this.pluginBean=pluginBean;
	}
	
	public String getId(){
		return pluginBean.getId();
	}
	
	public void setId(String id){
		pluginBean.setId(id);
	}
	
	
	public PluginBean getPluginBean(){
		return this.pluginBean;
	}

	//发送事件到，事件流转器
	public HHEvent sendEvent(HHEvent event){
		return eventRoute.processEvent(event);
	}
	//事件流转器调用该方法，把事件传入插件
	public abstract HHEvent receEvent(HHEvent event);
	
	//获得插件的根组件
	public abstract Component getComponent();


}
