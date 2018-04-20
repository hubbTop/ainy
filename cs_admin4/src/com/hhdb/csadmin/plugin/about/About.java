package com.hhdb.csadmin.plugin.about;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.Timer;

import com.hhdb.csadmin.common.base.AbstractPlugin;
import com.hhdb.csadmin.common.event.EventTypeEnum;
import com.hhdb.csadmin.common.event.HHEvent;
import com.hhdb.csadmin.common.util.EventUtil;

public class About extends AbstractPlugin{

	@Override
	public HHEvent receEvent(HHEvent event) {
		HHEvent replyE=EventUtil.getReplyEvent(About.class, event);
		if(event.getType().equals(EventTypeEnum.GET_OBJ.name())){
			about();
		}
		return replyE;
	}

	@Override
	public Component getComponent() {
		return null;
	}
	
	private void about() {
		
		final AboutPanel about = new AboutPanel(new Color(107, 148, 200),
				//背景图、版本信息及文字介绍位置
				"etc/icon/backgroundImg/splash.png","etc/icon/backgroundImg/splash2.png",
				"etc/icon/backgroundImg/splash3.png","etc/icon/backgroundImg/splash4.png",
				"产品：恒辉数据库C/S管理工具", "主版本：9.6", "提交版本：",
				new Color(107, 107, 107), 21, 165);
		//点击事件，关闭窗口
		about.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				about.dispose();
			}
		});
		//监听事件，过2S刷新背景图
		 Timer timer = new Timer(2000,new ActionListener() {
             @Override
             public void actionPerformed(ActionEvent e) {
            	 //重画
            	 about.repaint();
             }
         });
         timer.start();
	}

}
