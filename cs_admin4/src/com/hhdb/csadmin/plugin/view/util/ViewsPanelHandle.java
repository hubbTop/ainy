package com.hhdb.csadmin.plugin.view.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import com.hhdb.csadmin.common.bean.SqlBean;
import com.hhdb.csadmin.common.ui.textEdit.QueryEditorUi2;
import com.hhdb.csadmin.common.util.HHSqlUtil;
import com.hhdb.csadmin.common.util.HHSqlUtil.ITEM_TYPE;
import com.hhdb.csadmin.common.util.StartUtil;
import com.hhdb.csadmin.plugin.view.HView;
import com.hhdb.csadmin.plugin.view.ui.OutTabPanel;

/**
 * 视图处理
 * @author hhxd
 *
 */
public class ViewsPanelHandle {
	public HView hView ;
	private String viewName;
	
	public ViewsPanelHandle(HView hView){
		this.hView = hView;
	}
	
	/**
	 * 修改视图
	 */
	public boolean updatView(QueryEditorUi2 jtp,String viewName,String parentName){
		try {
			String sql = "CREATE  OR REPLACE VIEW \""+parentName+"\".\""+viewName+"\"  AS "+jtp.getText()+";";
			//执行sql
			hView.sosi.sqlOperation(sql);
			//刷新tree节点
			hView.sosi.refresh();
			String sql2 = "SELECT  "+StartUtil.prefix+"_get_ruledef(rw.oid, true) AS definition  FROM "+StartUtil.prefix+"_rewrite rw ";
			sql2 +=" JOIN "+StartUtil.prefix+"_class cl ON cl.oid=rw.ev_class  WHERE relname = '"+viewName+"' ";
			String definitionSql = returnSql(sql2);	//返回视图sql
			jtp.setText(definitionSql);
			JOptionPane.showMessageDialog(null, "保存视图成功");
			return false;
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e.getMessage(), "提示",JOptionPane.ERROR_MESSAGE);
			return true;
		}
	}
	
	/**
	 * 保存视图
	 * @param sqlView
	 * @param jtp
	 * @param parentName 模式名称
	 * @param viewOrViews
	 * @return
	 */
	public boolean saveView(QueryEditorUi2 jtp,String parentName) {
		if("".equals(jtp.getText())){
			JOptionPane.showMessageDialog(new JFrame(), "SQL syntax error , SQL 为空！", "提示",JOptionPane.ERROR_MESSAGE); 
			return true;
		}else{
			try{
				viewName = (String) JOptionPane.showInputDialog(new JFrame(), "输入视图名", "视图名",JOptionPane.PLAIN_MESSAGE, null, null, viewName);
				boolean  isHave = getNameList(parentName);
				if(isHave){//判断视图名是否存在或者是否为空
					return true;
				}
				String sql = "CREATE VIEW \""+parentName+"\".\""+viewName+"\" AS "+jtp.getText()+";";
				try {
					//执行sql
					hView.sosi.sqlOperation(sql);
					//刷新tree节点
					hView.sosi.refresh();
					String sql2 = "SELECT  "+StartUtil.prefix+"_get_ruledef(rw.oid, true) AS definition  FROM "+StartUtil.prefix+"_rewrite rw ";
					sql2 +=" JOIN "+StartUtil.prefix+"_class cl ON cl.oid=rw.ev_class  WHERE relname = '"+viewName+"' ";
					String definitionSql = returnSql(sql2);//返回视图sql
					jtp.setText(definitionSql);
					viewName = "";
					JOptionPane.showMessageDialog(null, "保存视图成功");
					return false;
				}catch (Exception e) {
					JOptionPane.showMessageDialog(null, e.getMessage(), "提示",JOptionPane.ERROR_MESSAGE);
					return saveView(jtp, parentName);
				}
			}catch(Exception e){
				JOptionPane.showMessageDialog(null, e.getMessage()+"  请新建视图！", "提示",JOptionPane.ERROR_MESSAGE);
				return true;
			}
			
		}
	}
	
	/**
	 * 返回视图sql
	 */
	public String returnSql(String sql){
		String definitionSql = null;
		try {
			List<Map<String, Object>> valueList = hView.sosi.getListMap(sql);
			if(valueList.size() != 0){
				definitionSql = getString(valueList.get(0), "definition","").split("DO INSTEAD")[1];
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return definitionSql;
	}
	
	
	/**
	 * 判断视图名是否存在或者是否为空
	 * @param node
	 * @return
	 */
	public boolean getNameList(String parentName){
		boolean flag = false;
		if("".equals(viewName)){
			JOptionPane.showMessageDialog(new JFrame(), "视图名不能为空");
			flag = true;
			return flag;
		}else if(null == viewName ){//点击取消按钮返回null
			flag = true;
			return flag;
		}
		Object[] params = new Object[1];
		params[0] = "'" + parentName + "'";
		SqlBean sql =  HHSqlUtil.getSqlBean(ITEM_TYPE.VIEW,"prop_coll");
		List<Map<String, Object>> list = new ArrayList<>();
		try {
			list = hView.sosi.getListMap(sql.replaceParams(params));
		} catch (Exception e) {
			e.printStackTrace();
		}
		StringBuffer  sb = new StringBuffer(",");
		for (Map<String, Object> map : list) {
			sb.append(getString(map, "name","")).append(",");
		}
		if(sb.toString().contains(","+viewName+",")){
			JOptionPane.showMessageDialog(null, "视图名已经存在或者无效");
			flag = true;
		}
		return flag;
	}
	
	/**
	 * 预览
	 */
	public void previewPanel(QueryEditorUi2 jtp,OutTabPanel outpanel){
		String strSelected = jtp.getSelectedText();
		if (strSelected == null || "".equals(strSelected.trim())) {
			strSelected = jtp.getText();
		}
		executeSql(strSelected,outpanel);
		
		outpanel.setVisible(true);
		outpanel.setSelectedIndex(0);
		
	}
	
	/**
	 * 执行sql并显示结果到窗口
	 * 
	 * @param sql
	 */
	private void executeSql(String sql,OutTabPanel outpanel) {
		String message="";
		try {
			if (sql.trim().toUpperCase().startsWith("SELECT")) {
				outpanel.addShowData(sql);
			} else {
				if("".equals(sql)){
					message +=" [SQL] \n\n 时间: 0.000s \n 受影响的行: 0";
				}else{
					hView.sosi.sqlOperation(sql);;
					message +="执行成功！";
				}
				outpanel.setMessage(message);
			}
		} catch (Exception e) {
			message +=e.getMessage();
			outpanel.setMessage(message);
		}
	}
	
	/**
	 * 获取视图
	 */
	public void design(QueryEditorUi2 jtp){
		String sql = "SELECT  "+StartUtil.prefix+"_get_ruledef(rw.oid, true) AS definition  FROM "+StartUtil.prefix+"_rewrite rw ";
		sql +=" JOIN "+StartUtil.prefix+"_class cl ON cl.oid=rw.ev_class  WHERE relname = '"+viewName+"' ";
		String definitionSql = returnSql(sql);	//返回视图sql
		jtp.setText(definitionSql);
	}

	public String getViewName() {
		return viewName;
	}

	public void setViewName(String viewName) {
		this.viewName = viewName;
	}
	
	public static String getString(Map<?, ?> map, Object key, String defaultVal) {
		if (!isEmpty(map) && key != null) {
			Object val = map.get(key);
			return val == null ? defaultVal : val.toString();
		} else {
			return defaultVal;
		}
	}
	
	public static boolean isEmpty(Map<?, ?> map) {
		return map == null || map.isEmpty();
	}
}
