package com.hhdb.csadmin.plugin.table_operate.handle;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.List;
import java.util.Map;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import com.hhdb.csadmin.plugin.table_operate.TableEditPanel;

/**
 * 
 * @Description: 注释
 * @Copyright: Copyright (c) 2017年10月25日
 * @Company:H2 Technology
 * @author zhipeng.zhang
 * @version 1.0
 */
public class HandleCommentPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	private TableEditPanel tabp;
	
	private JTextArea comment;

	public HandleCommentPanel(TableEditPanel tableeditpanel, HandleTablePanel tabsPanel) {
		this.tabp = tableeditpanel;
		comment = new JTextArea();
		initObj();
		JScrollPane jsol = new JScrollPane(comment);
		setLayout(new GridBagLayout());
		add(jsol, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0), 0, 0));
	}
	
	private void initObj(){
		try {
			String sql="select  obj_description("+tabp.getTableoId()+");";
			List<Map<String, Object>> map =	tabp.sqls.getListMap(sql);
			for (Map<String, Object> map2 : map) {
				if(null != map2.get("obj_description") && map2.get("obj_description") != ""){
					comment.setText(map2.get("obj_description").toString());
				}else{
					comment.setText("");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public String createCommentSql() {
		String sql = "\n COMMENT ON TABLE \"" + tabp.getSchemaName() + "\".\"" + tabp.getTableName() + "\" IS '" + comment.getText() + "';";
		return sql;
	}

	public JTextArea getComment() {
		return comment;
	}
}