package com.hhdb.csadmin.plugin.attribute;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.io.IOException;
import java.util.List;
import java.util.Vector;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.table.TableColumn;

import com.hhdb.csadmin.common.base.AbstractPlugin;
import com.hhdb.csadmin.common.event.CmdEvent;
import com.hhdb.csadmin.common.event.ErrorEvent;
import com.hhdb.csadmin.common.event.EventTypeEnum;
import com.hhdb.csadmin.common.event.HHEvent;
import com.hhdb.csadmin.common.ui.displayTable.BaseTablePanel;
import com.hhdb.csadmin.common.util.CSVUtil;
import com.hhdb.csadmin.common.util.EventUtil;
import com.hhdb.csadmin.plugin.table_open.ui.HHTableColumnCellRenderer;

public class AttributeShow extends AbstractPlugin {
	private JPanel attribute;

	public AttributeShow() {
	}

	@Override
	public HHEvent receEvent(HHEvent event) {
		if (event.getType().equals(EventTypeEnum.CMD.name())) {
			if(event.getValue("CMD").equals("Show")){
				//获取表面板
				BaseTablePanel btp = new BaseTablePanel(false);
				btp.drawAllowed(false);
				btp.nterlacedDiscoloration(true);
				btp.setRowSorter(true);
				btp.highlight(true,true);
				// 先请求面板
				String fromID = AttributeShow.class.getPackage().getName();
				String toID = "com.hhdb.csadmin.plugin.tabpane";

				CmdEvent requestComEvent = new CmdEvent(fromID, toID,
						"flushAttributeEvent");
				HHEvent hhevent = sendEvent(requestComEvent);
				attribute = (JPanel) hhevent.getObj();

				// 直接显示属性表格
				String csvStr = event.getValue("csvuEventStr");
				if (!csvStr.equals("")) {
					List<List<String>> list;
					try {
						list = CSVUtil.cSV2List(csvStr);
					} catch (IOException e) {
						ErrorEvent errorEvent = new ErrorEvent(AttributeShow.class.getPackage().getName(),
								event.getFromID(),
								ErrorEvent.ErrorType.EVENT_NOT_VALID.name());
						errorEvent.setErrorMessage(e.getMessage());
						return errorEvent;
					}

					Vector<String> columName = new Vector<String>();
					columName.add("");
					Vector<String> rowsLine = new Vector<String>();
					for (String colunm : list.get(0)) {
						columName.add(colunm);
					}
					btp.getTableDataModel().setColumnIdentifiers(columName);
					for(int j=1;j<list.size();j++){
						List<String> l = list.get(j);
						rowsLine.add(j+"");
						for(String value:l){
							rowsLine.add(value);
						}
						btp.getTableDataModel().addRow(rowsLine);
						rowsLine = new Vector<>();
					}
					
					//设置序号行样式
					TableColumn index = btp.getBaseTable().getColumnModel().getColumn(0);
					index.setPreferredWidth(20);
					index.setMaxWidth(25);
					index.setMinWidth(25);
					index.setCellRenderer(new HHTableColumnCellRenderer());
					
					attribute.setLayout(new BorderLayout());
					attribute.removeAll();
					attribute.add(btp, BorderLayout.CENTER);
					attribute.updateUI();
				} else {
					attribute.setLayout(new FlowLayout());
					attribute.removeAll();
					attribute.add(new JLabel("没有属性数据"));
					attribute.updateUI();
				}
			}
			return EventUtil.getReplyEvent(AttributeShow.class, event);
		}else{
			ErrorEvent errorEvent = new ErrorEvent(AttributeShow.class.getPackage().getName(),
					event.getFromID(),
					ErrorEvent.ErrorType.EVENT_NOT_VALID.name());
			errorEvent.setErrorMessage(AttributeShow.class.getPackage().getName() + "不能接受如下类型的事件:\n"
					+ event.toString());
			return errorEvent;
		}
	}

	@Override
	public Component getComponent() {
		return null;
	}

}
