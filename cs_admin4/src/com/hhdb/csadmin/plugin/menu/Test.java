package com.hhdb.csadmin.plugin.menu;

import java.awt.Frame;

import javax.swing.JMenuBar;

import com.hhdb.csadmin.common.base.HHEventRoute;
import com.hhdb.csadmin.common.base.IEventRoute;
import com.hhdb.csadmin.common.event.EventTypeEnum;
import com.hhdb.csadmin.common.event.HHEvent;

public class Test {

	public static void main(String[] args) {
		
		IEventRoute eventRoute= new HHEventRoute();
		HHEvent menuEvent=new HHEvent("com.hhdb.csadmin.plugin.test","com.hhdb.csadmin.plugin.menu",EventTypeEnum.GET_OBJ.name());
		HHEvent processEvent = eventRoute.processEvent(menuEvent);
		JMenuBar menubar=(JMenuBar) processEvent.getObj();
		Frame f=new Frame();
		f.add(menubar);
		f.setVisible(true);
	}

}
