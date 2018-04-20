package com.hhdb.csadmin.plugin.user_permission;

import javax.swing.JFrame;
import javax.swing.JPanel;

import com.hhdb.csadmin.common.base.HHEventRoute;
import com.hhdb.csadmin.common.base.IEventRoute;
import com.hhdb.csadmin.common.event.CmdEvent;
import com.hhdb.csadmin.common.event.HHEvent;

public class Test {

	public static void main(String[] args) {
		
		       IEventRoute eventRoute= new HHEventRoute();
		        //设置ServerBean
				CmdEvent setsbEvent = new CmdEvent("begin", "com.hhdb.csadmin.plugin.conn", "SetConn");
				setsbEvent.addProp("host_str", "127.0.0.1");
				setsbEvent.addProp("post_str", "1432");
				setsbEvent.addProp("dbname_str", "hhdb");
				setsbEvent.addProp("username_str", "dba");
				setsbEvent.addProp("pass_str", "123456");
				setsbEvent.addProp("superuser_value", "true");
				eventRoute.processEvent(setsbEvent);
		CmdEvent cmd=new CmdEvent("test", "com.hhdb.csadmin.plugin.user_permission", "test");
		cmd.addProp("userName", "dba");
		HHEvent reply = eventRoute.processEvent(cmd);
		JFrame frame=new JFrame();
		frame.add((JPanel)reply.getObj());
		frame.setVisible(true);
	}

}
