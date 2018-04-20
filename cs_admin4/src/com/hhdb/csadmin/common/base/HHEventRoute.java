package com.hhdb.csadmin.common.base;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Hashtable;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.lang3.StringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;


import com.hhdb.csadmin.common.event.HHEvent;
import com.hhdb.csadmin.common.util.LogDefault;
import com.hhdb.csadmin.common.util.XmlFileUtil;

public class HHEventRoute implements IEventRoute {
	private static LogDefault logger=LogDefault.getDefaultLogger("com.hhdb.csadmin.logger");
	private  Map<String,AbstractPlugin> pluginMap= new Hashtable<String,AbstractPlugin>();
	
	public HHEventRoute(){
	}

	@Override
	public HHEvent processEvent(HHEvent event) {
		logger.debug(event.toString());
		String toID=event.getToID();
		if(StringUtils.isBlank(toID)){
			logger.info("otID不能为空");
			return null;
		}
		if(!pluginMap.containsKey(toID)){
			boolean isSuc=addPlugin(toID);
			if(!isSuc){
				logger.info("找不到%s,请调用addPlugin添加",toID);
				return null;
			}
		}
		AbstractPlugin plugin=pluginMap.get(toID);
		HHEvent replyEvent=plugin.receEvent(event);
		logger.debug(replyEvent.toString());
		return replyEvent;
	}



	@Override
	public boolean addPlugin(String id) {
		if(StringUtils.isBlank(id)){
			logger.info("ID不能为空,");
			return false;
		}
		// 如果plugin没有存在
		try {
			PluginBean pluginBean=getBean(id);
			Class<?>  c = Class.forName(id+"."+pluginBean.getClazz());
			AbstractPlugin plugin = (AbstractPlugin) c.newInstance();
			plugin.init(this,pluginBean);
			pluginMap.put(id, plugin);
			return true;
			
		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException | ParserConfigurationException | SAXException | IOException | URISyntaxException e) {
			logger.error(e);
			return false;
		} 
	}
	
	private PluginBean getBean(String id) throws ParserConfigurationException, SAXException, IOException, URISyntaxException{
		
		PluginBean pluginBean=new PluginBean();
		String pluginXmlPath=id.replace('.', '/')+"/plugin.xml";
		Document pluginDoc=null;
		pluginDoc = XmlFileUtil.getResXmlDoc(pluginXmlPath);

		NodeList nodeList=pluginDoc.getChildNodes();
		Element pluginDetail=(Element)nodeList.item(0);
		pluginBean.setClazz(pluginDetail.getAttribute("class"));
		pluginBean.setAuthor(pluginDetail.getAttribute("author"));
		pluginBean.setVersion(pluginDetail.getAttribute("version"));
		pluginBean.setId(id);
		return pluginBean;
	}
	
	

}
