package com.hhdb.csadmin.plugin.login;

import java.awt.Component;

import com.hhdb.csadmin.common.base.AbstractPlugin;
import com.hhdb.csadmin.common.base.PluginBean;
import com.hhdb.csadmin.common.event.EventTypeEnum;
import com.hhdb.csadmin.common.event.HHEvent;
import com.hhdb.csadmin.common.util.EventUtil;

public class LoginPlugin extends AbstractPlugin{
	LoginFrame loginFrame=null;
	


	@Override
	public HHEvent receEvent(HHEvent event) {
		HHEvent replyE=EventUtil.getReplyEvent(LoginPlugin.class, event);
		if(event.getType().equals(EventTypeEnum.COMMON.name())){
			if(loginFrame==null){
				loginFrame=new LoginFrame(this);
			}
			loginFrame.setVisible(true);
		}
		
		return replyE;
	}

	@Override
	public Component getComponent() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public PluginBean getPluginBean(){
		return null;
	}

}
