package com.hhdb.csadmin.plugin.sql_book;

import java.awt.Component;

import javax.swing.JOptionPane;

import com.hhdb.csadmin.common.base.AbstractPlugin;
import com.hhdb.csadmin.common.event.ErrorEvent;
import com.hhdb.csadmin.common.event.EventTypeEnum;
import com.hhdb.csadmin.common.event.HHEvent;
import com.hhdb.csadmin.common.util.EventUtil;
import com.hhdb.csadmin.plugin.sql_book.ui.ShortcutPanel;

/**
 * sql宝典插件
 * @author hhxd
 *
 */
public class HSqlBook extends AbstractPlugin {
	public String PLUGIN_ID = HSqlBook.class.getPackage().getName();
	
	@Override
	public HHEvent receEvent(HHEvent event) {
		HHEvent relEvent= EventUtil.getReplyEvent(HSqlBook.class, event);
		if(event.getType().equals(EventTypeEnum.CMD.name())){       //打开sql宝典面板
			if(event.getValue("CMD").equals("SQLBook")){
				try {
					BooksPanel bookp = new BooksPanel(this,true);
					bookp.sqls.getTabPanelTable("sql宝典");
				} catch (Exception e) {
					JOptionPane.showMessageDialog(null, e.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
					return relEvent;
				}
			}else if(event.getValue("CMD").equalsIgnoreCase("RemovePanelEvent")) { //关闭打开页面事件
			}
			return relEvent;
		}else{
			ErrorEvent errorEvent = new ErrorEvent(PLUGIN_ID,event.getFromID(),ErrorEvent.ErrorType.EVENT_NOT_VALID.name());
			errorEvent.setErrorMessage(PLUGIN_ID + "不能接受如下类型的事件:\n"+ event.toString());
			return errorEvent;
		}

	}
	
	/**
	 * 获取新的树面板
	 * @return
	 */
	public BooksPanel getTreePanel(Integer myDirId,ShortcutPanel shp){
		BooksPanel bp = new BooksPanel(this,false);
		bp.myDirId = myDirId;
		bp.shp= shp;
		return bp;
		
	}
	
	@Override
	public Component getComponent() {
		return null;
	}

}
