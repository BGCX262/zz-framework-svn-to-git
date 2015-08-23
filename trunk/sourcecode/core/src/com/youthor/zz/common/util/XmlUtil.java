package com.youthor.zz.common.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;

import com.youthor.zz.common.ZzConstant;
import com.youthor.zz.common.xml.XmlElement;

public class XmlUtil {
    
    public static  Element getXmlFileRootDocument(String elementsFileFullPath) {
        File file = new File(elementsFileFullPath);
        InputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream(file);
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        return XmlUtil.getXmlFileRootDocument(fileInputStream);
    }
    
    public static  Element getXmlTextRootDocument(String xml) {
    	 Document document = null;
        try {
            document = DocumentHelper.parseText(xml);
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        return document.getRootElement();
    }
    
    public static Element getXmlFileRootDocument(InputStream fileInputStream) {
        Document document = null;
        try {
            SAXReader reader = new SAXReader();
            document = reader.read(fileInputStream);
        }
        catch(Exception ex) {
            ex.printStackTrace();
        }
        return document.getRootElement();
    }
    
    public static XmlElement parseFile(String fileFullName) {
        XmlElement node = new XmlElement();
        Element rootElement = XmlUtil.getXmlFileRootDocument(fileFullName);
        node = XmlUtil.xmlNodeWalk(rootElement, node, ZzConstant.XML_NODE_SPLIT_SIGN);
        return node;
    }
    
    public static XmlElement parseText(String xml) {
        XmlElement node = new XmlElement();
        Element rootElement = XmlUtil.getXmlTextRootDocument(xml);
        node = XmlUtil.xmlNodeWalk(rootElement, node, ZzConstant.XML_NODE_SPLIT_SIGN);
        return node;
    }
    
    public static XmlElement xmlNodeWalk(Element element, XmlElement curNode, String parentPath) {
        String tagName = element.getName();
        String text = element.getText();
        int attrSize = element.attributeCount();
        int nodeSize = element.nodeCount();
        
        curNode.setTagName(tagName);
        curNode.setParentTagName(parentPath);
        curNode.setFullTagName(parentPath + tagName);
        curNode.setValue(text);
        
        for (int i = 0; i < attrSize; i++) {
            Attribute attr = element.attribute(i);
            curNode.addAttr(attr.getName(), attr.getValue());
        }
        
        for (int i = 0; i < nodeSize; i++) {
            Node node = element.node(i);
            if (node instanceof Element) {
                XmlElement xmlNode = new XmlElement();
                curNode.addChild(xmlNode);
                XmlUtil.xmlNodeWalk((Element) node, xmlNode, parentPath + tagName + ZzConstant.XML_NODE_SPLIT_SIGN);
            }
        }
        return curNode;
    }

    public static void mergeFile(XmlElement xmlElement, String elementsFileFullPath, boolean overlap) {
        Element rootElement = XmlUtil.getXmlFileRootDocument(elementsFileFullPath);
        String spaceSign = ZzConstant.XML_NODE_SPLIT_SIGN;
        xmlNodeToXmlElement(xmlElement, rootElement, spaceSign, overlap);
    }

    public static void mergeFile(XmlElement xmlElement, InputStream fileInputStream, boolean overlap)throws Exception {
        Element rootElement = XmlUtil.getXmlFileRootDocument(fileInputStream);
        String spaceSign = ZzConstant.XML_NODE_SPLIT_SIGN;
        xmlNodeToXmlElement(xmlElement, rootElement, spaceSign, overlap);
    }

    // 遍历xml并加入到当前的xml对象里
    private static void xmlNodeToXmlElement(XmlElement xmlElement,Element element,String parentNameSpace, boolean overlap){
        String tagName = element.getName();
        int nodeSize = element.nodeCount();
        
        List<XmlElement> tagNameElementList = xmlElement.getTagNode(parentNameSpace + tagName, true);
        //如过没有该节点则创建该节点
        if(tagNameElementList == null || tagNameElementList.size() == 0){
            //创建一个新的子节点
            addSubXmlElement(xmlElement,element,parentNameSpace);
        }
        else{
            if(overlap){
                updateXmlNodeInfo(element,parentNameSpace, tagNameElementList);
            }
            else{
                //创建一个新的子节点
                addSubXmlElement(xmlElement,element,parentNameSpace);
            }
        }
        
        for (int i = 0; i < nodeSize; i++) {
            Node node = element.node(i);
            if (node instanceof Element) {
                String subParentNameSpace = parentNameSpace + tagName + ZzConstant.XML_NODE_SPLIT_SIGN;
                XmlUtil.xmlNodeToXmlElement(xmlElement,(Element) node,subParentNameSpace,overlap);
            }
        }
    }
    
    
  //根据element在xmlElement对象上添加一个子节点对象
    private static void addSubXmlElement(XmlElement xmlElement, Element element, String parentNameSpace){
        List<XmlElement> parentXmlNodeList = xmlElement.getTagNode(parentNameSpace,true);
        if (parentXmlNodeList.size() ==0 ) {
            return;
        }
        XmlElement lastElement = parentXmlNodeList.get(parentXmlNodeList.size() - 1);
        XmlElement subElement = new XmlElement();
        List<XmlElement> list = new ArrayList<XmlElement>();
        list.add(subElement);
        XmlUtil.updateXmlNodeInfo(element, parentNameSpace, list);
        lastElement.addChild(subElement);
    }
    
    
    private static void updateXmlNodeInfo(Element element, String parentTagName, List<XmlElement> list) {
        for (XmlElement xmlNode : list) {
            xmlNode.setFullTagName(parentTagName + element.getName());
            xmlNode.setTagName(element.getName());
            xmlNode.setValue(element.getText());
            //xmlNode.getAttr().clear();
            int attrSize = element.attributeCount();
            for (int i = 0; i < attrSize; i++) {
                Attribute attr = element.attribute(i);
                xmlNode.addAttr(attr.getName(), attr.getValue());
            }
        }
    }
    
}
