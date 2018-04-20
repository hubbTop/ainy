package com.hhdb.csadmin.common.base;

import com.hhdb.csadmin.common.event.HHEvent;

public interface IEventRoute {
	boolean addPlugin(String  id);
	HHEvent processEvent(HHEvent event);
}
