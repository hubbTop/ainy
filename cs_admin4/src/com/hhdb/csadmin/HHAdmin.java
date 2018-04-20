
package com.hhdb.csadmin;

import java.awt.Color;

import com.hhdb.csadmin.common.base.HHEventRoute;
import com.hhdb.csadmin.common.base.IEventRoute;
import com.hhdb.csadmin.common.event.EventTypeEnum;
import com.hhdb.csadmin.common.event.HHEvent;
import com.hhdb.csadmin.common.util.UiUtil;

public class HHAdmin {
	public static void main(String[] args) throws Exception {
		SplashPanel splash = createSplashPanel();
		for(int i=0;i<9;i++){
			splash.advance();
			Thread.sleep(110);
		}
		splash.dispose();
		UiUtil.setLookAndFeel();
		System.out.println(EventTypeEnum.CMD.name());
		IEventRoute eventRoute= new HHEventRoute();

		HHEvent loginEvent=new HHEvent("begin","com.hhdb.csadmin.plugin.login",EventTypeEnum.COMMON.name());
		eventRoute.processEvent(loginEvent);		
	}
	private static SplashPanel createSplashPanel() {
		return new SplashPanel(new Color(107, 148, 200), "/icon/splash.png", "产品：恒辉数据库C/S管理工具","主版本：9.6","提交版本：", new Color(107, 107, 107), 21, 165);
	}
}
