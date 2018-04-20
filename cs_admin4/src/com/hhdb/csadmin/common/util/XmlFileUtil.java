package com.hhdb.csadmin.common.util;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

public class XmlFileUtil {

	public static Document getResXmlDoc(String xmlPath) throws ParserConfigurationException, SAXException, IOException, URISyntaxException {
		Document doc = null;
		InputStream is = ClassLoader.getSystemResourceAsStream(xmlPath);
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		doc = builder.parse(is);
		return doc;
	}
	
	public static Document getXmlDoc(String xmlStr) throws ParserConfigurationException, SAXException, IOException {
		Document doc = null;
		InputStream is = new  ByteArrayInputStream(xmlStr.getBytes("UTF-8"));   
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		doc = builder.parse(is);
		return doc;
	}

}
