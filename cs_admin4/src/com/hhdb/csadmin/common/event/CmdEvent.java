package com.hhdb.csadmin.common.event;

public class CmdEvent extends HHEvent{
	
	public CmdEvent(String fromID, String toID,String cmd) {
		super(fromID, toID,EventTypeEnum.CMD.name());
		super.addProp(EventTypeEnum.CMD.name(), cmd);
	}
	public String getCmd(){
		return super.getValue(EventTypeEnum.CMD.name());
	}
}
