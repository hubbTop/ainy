package com.hhdb.csadmin.common.util;

import java.util.HashMap;
import java.util.Map;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.hhdb.csadmin.common.bean.ServerBean;

public class StartUtil {
	/**
	 * hh:hhdb数据库
	 * pg:postgresql数据库
	 */
	public static String prefix = "hh";
	public static void main(String[] args) throws Exception {
		initConnConfig();
	}
	public static Map<String,ServerBean> initConnConfig() throws Exception{
		Map<String,ServerBean> map = new HashMap<String, ServerBean>();
		Document doc = XmlFileUtil.getResXmlDoc("hh_conn_config.xml");

		NodeList connList = doc.getElementsByTagName("conn");
		for(int i=0;i<connList.getLength();i++){
			Element connElement = (Element) connList.item(i);
			ServerBean serverbean = new ServerBean();
			serverbean.setHost(connElement.getAttribute("host"));
			serverbean.setPort(connElement.getAttribute("post"));
			serverbean.setDBName(connElement.getAttribute("database"));
			serverbean.setUserName(connElement.getAttribute("username"));
			serverbean.setPassword(connElement.getAttribute("password"));
			String key = serverbean.getHost()+":"+serverbean.getPort()+":"+serverbean.getDBName()+":"
					+serverbean.getUserName();
			map.put(key, serverbean);
		}
		return map;
	}
	public static Map<String,ServerBean> initConnConfigPg() throws Exception{
		Map<String,ServerBean> map = new HashMap<String, ServerBean>();
		Document doc = XmlFileUtil.getResXmlDoc("pg_conn_config.xml");

		NodeList connList = doc.getElementsByTagName("conn");
		for(int i=0;i<connList.getLength();i++){
			Element connElement = (Element) connList.item(i);
			ServerBean serverbean = new ServerBean();
			serverbean.setHost(connElement.getAttribute("host"));
			serverbean.setPort(connElement.getAttribute("post"));
			serverbean.setDBName(connElement.getAttribute("database"));
			serverbean.setUserName(connElement.getAttribute("username"));
			serverbean.setPassword(connElement.getAttribute("password"));
			String key = serverbean.getHost()+":"+serverbean.getPort()+":"+serverbean.getDBName()+":"
					+serverbean.getUserName();
			map.put(key, serverbean);
		}
		return map;
	}
}
