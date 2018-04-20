package com.hhdb.csadmin.common.base;

import java.util.HashMap;
import java.util.Map;

public class PluginBean {
	private String id;
	private String clazz;
	private String version;
	private String author;
	Map<String, String> propsMap = new HashMap<String, String>();

	public String getId() {
		return id;
	}

	public String getClazz() {
		return clazz;
	}

	public void setClazz(String clazz) {
		this.clazz = clazz;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public Map<String, String> getPropsMap() {
		return propsMap;
	}

	public void setPropsMap(Map<String, String> propsMap) {
		this.propsMap = propsMap;
	}
	
	public void addPropsMap(String key, String value) {
		this.propsMap.put(key, value);
	}

	public void setId(String id) {
		this.id = id;
	}
}
