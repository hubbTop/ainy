package com.hhdb.csadmin.common.event;

import java.awt.Component;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.swing.ImageIcon;

import com.hhdb.csadmin.common.util.EnumUtil;

public class HHEvent {
	private String fromID;
	private String toID;
	private String type = "";
	private Component component;
	private ImageIcon imageIcon;
	private Map<String, String> propMap = new HashMap<String, String>();
	private Object obj;
	public Object getObj() {
		return obj;
	}

	public void setObj(Object obj) {
		this.obj = obj;
	}
	
	public HHEvent(String fromID, String toID, String type) {
		setValue( fromID,toID,type);
	}
    @Deprecated
	public HHEvent(String fromID, String toID, String type,
			Component component, ImageIcon imageIcon) {
		setValue( fromID,toID,type);
		this.component = component;
		this.imageIcon = imageIcon;
	}
    
    
    @Deprecated
	public HHEvent(String fromID, String toID, String type, Component component) {
		setValue( fromID,toID,type);
		this.component = component;
	}

	public void addProp(String key, String value) {
		propMap.put(key, value);
	}

	public String getValue(String key) {
		return propMap.get(key);
	}

	public String getFromID() {
		return fromID;
	}

	public void setFromID(String fromID) {
		this.fromID = fromID;
	}

	public String getToID() {
		return toID;
	}

	public void setToID(String toID) {
		this.toID = toID;
	}

	public Map<String, String> getPropMap() {
		return propMap;
	}

	public void setPropMap(Map<String, String> propMap) {
		this.propMap = propMap;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
    @Deprecated
	public Component getComponent() {
		return component;
	}
    @Deprecated
	public void setComponent(Component component) {
		this.component = component;
	}
    @Deprecated
	public ImageIcon getImageIcon() {
		return imageIcon;
	}
    @Deprecated
	public void setImageIcon(ImageIcon imageIcon) {
		this.imageIcon = imageIcon;
	}

	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
		sb.append(String.format("<event from=\"%s\" to=\"%s\" type=\"%s\">\n",
				fromID, toID, type));
		Iterator<String> keys = propMap.keySet().iterator();
		while (keys.hasNext()) {
			String key = keys.next();
			sb.append(String.format("\t<property name=\"%s\">\n", key));
			sb.append("<![CDATA[\n");
			sb.append(propMap.get(key));
			sb.append("\n]]>\n");
			sb.append(String.format("\t</property>\n"));
		}
		sb.append("</event>\n");
		return sb.toString();
	}
	
	private void setValue(String fromID, String toID, String type){
		if(EnumUtil.contains(EventTypeEnum.class, type)){
			this.type = type;
		}
		this.fromID = fromID;
		this.toID = toID;
	}

}
