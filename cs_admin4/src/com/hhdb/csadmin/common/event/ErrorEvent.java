package com.hhdb.csadmin.common.event;

import com.hhdb.csadmin.common.util.EnumUtil;

public class ErrorEvent extends HHEvent {

	public static enum ErrorType{
		UNKNOW,EVENT_NOT_VALID,SYSTEM_ERROR
	}
	private final static String MSG="MSG";
	
	public ErrorEvent(String fromID, String toID,String errorType) {
		super(fromID, toID,EventTypeEnum.ERROR.name());
		if(EnumUtil.contains(ErrorType.class, errorType)){
			super.addProp(EventTypeEnum.ERROR.name(), errorType);
		}else{
			throw new RuntimeException("不合法的命令"+errorType);
		}
	}
	
	public void setErrorMessage(String errorMsg){
		this.addProp(MSG, errorMsg);
	}
	
	public String getErrorMessage(){
		return this.getValue(MSG);
	}
}
