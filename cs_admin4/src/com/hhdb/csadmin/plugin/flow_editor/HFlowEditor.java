package com.hhdb.csadmin.plugin.flow_editor;

import java.awt.Component;

import javax.swing.JOptionPane;

import com.hhdb.csadmin.common.base.AbstractPlugin;
import com.hhdb.csadmin.common.event.ErrorEvent;
import com.hhdb.csadmin.common.event.EventTypeEnum;
import com.hhdb.csadmin.common.event.HHEvent;
import com.hhdb.csadmin.common.util.EventUtil;
import com.hhdb.csadmin.plugin.flow_editor.service.SqlOperationService;

/**
 * 流数据处理
 * 
 * @author hhxd
 * 
 */
public class HFlowEditor extends AbstractPlugin {

	public String PLUGIN_ID = HFlowEditor.class.getPackage().getName();

	public SqlOperationService serv;
	private FlowEditorPanel hfep;
	public String databaseName; // 数据库名称
	public String schemaName; // 模式名称
	public String tableName; // 表名
	public String columnName; // 列名
	public String ctid; // id
	public Object value; // 值
	public String componentId;  //打开表面板id
	public String name; // 模式加表名

	public HFlowEditor() {
		serv = new SqlOperationService(this);
	}

	/**
	 * 重写插件接收事件
	 */
	@Override
	public HHEvent receEvent(HHEvent event) {
		HHEvent hevent = EventUtil.getReplyEvent(HFlowEditor.class, event);
		if (event.getType().equals(EventTypeEnum.CMD.name())) { // 打开操作面板
			try {
				databaseName = event.getValue("databaseName");
				schemaName = event.getValue("schemaName");
				tableName = event.getValue("tableName");
				columnName = event.getValue("columnName");
				ctid = event.getValue("ctid");
				componentId = event.getValue("componentId");
				value = event.getObj();
				name = "\"" + schemaName + "\".\"" + tableName + "\"";
				hfep = new FlowEditorPanel(this);
				hfep.initPanel();
				// 弹出对话框
				new JFramePanel("数据操作", 600, 600, hfep, serv.getBaseFrame());
			} catch (Exception e) {
				JOptionPane.showMessageDialog(null, "错误信息：" + e.getMessage(),"错误", JOptionPane.ERROR_MESSAGE);
				return hevent;
			}
			return hevent;
		} else {
			ErrorEvent errorEvent = new ErrorEvent(PLUGIN_ID,event.getFromID(),ErrorEvent.ErrorType.EVENT_NOT_VALID.name());
			errorEvent.setErrorMessage(PLUGIN_ID + "不能接受如下事件:\n"+ event.toString());
			return errorEvent;
		}
	}

	@Override
	public Component getComponent() {
		return null;
	}
}
