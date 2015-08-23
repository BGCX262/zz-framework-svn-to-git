package com.youthor.zz.core.models;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.youthor.zz.common.ZzApp;
import com.youthor.zz.common.c.block.BAbstract;
import com.youthor.zz.common.util.ObjectUtil;
import com.youthor.zz.common.util.StringUtil;
import com.youthor.zz.common.util.XmlUtil;
import com.youthor.zz.common.xml.XmlElement;
import com.youthor.zz.core.models.layout.Update;

public class Layout {

    private XmlElement layoutXmlElement = null;
    

    public XmlElement getLayoutXmlElement() {
        return this.layoutXmlElement;
    }

    public Update getUpdate() {
        return ZzApp.getUpdate();
    }

    public Design getDesign() {
        return ZzApp.getDesign();
    }

    public Object getBlock(String type) {
        BAbstract block = (BAbstract)ZzApp.getBlock(type);
        block.setLayout(this);
        return block;
    }

    public Object getBlock(String type, String name) {
        BAbstract block = (BAbstract)ZzApp.getBlock(type);
        block.addData("name", name);
        block.setLayout(this);
        this.getUpdate().addBlock(name, block);
        return block;
    }

    public Object getBlock(String type, String name, Map<String, Object> attributes) {
        BAbstract block = (BAbstract)ZzApp.getBlock(type, attributes);
        block.addData("name", name);
        block.setLayout(this);
        this.getUpdate().addBlock(name, block);
        return block;
    }

    public Object getBlockSetParent(String type, String parentBlockName) {
        BAbstract block = (BAbstract)ZzApp.getBlock(type);
        block.setLayout(this);
        BAbstract pblock = this.getUpdate().getBlock(parentBlockName);
        if (pblock !=null ) {
            String alias = "alias_" + (pblock.getChildren().size()+1);
            pblock.setChild(alias, block);
        }
        return block;
    }

    public Object getBlockSetParent(String type, String name, String parentBlockName) {
        BAbstract block = (BAbstract)ZzApp.getBlock(type);
        block.addData("name", name);
        block.setLayout(this);
        BAbstract pblock = this.getUpdate().getBlock(parentBlockName);
        if (pblock !=null ) {
            pblock.setChild(name, block);
        }
        return block;
    }

    public Object getBlockSetParent(String type, String name, Map<String, Object> attributes, String parentBlockName) {
        BAbstract block = (BAbstract)ZzApp.getBlock(type, attributes);
        block.addData("name", name);
        block.setLayout(this);
        BAbstract pblock = this.getUpdate().getBlock(parentBlockName);
        if (pblock !=null ) {
            pblock.setChild(name, block);
        }
        return block;
    }

    public Layout generateBlocks() {
        Update update = this.getUpdate();
        String blockXml = update.getBlockXml();
        XmlElement xmlElement = XmlUtil.parseText(blockXml);
        this.generateBlocks(xmlElement,null);
        return this;
    }

    public Layout generateBlocks(XmlElement xmlElement,BAbstract parentBlock) {
        Update update = this.getUpdate();
        List<XmlElement> handles = xmlElement.getChildren();
        for(XmlElement handle : handles) {
            String tagName = handle.getTagName();
            switch (this.getMapIntOfHandle(tagName)) {
                case 1 :
                    BAbstract block = this.generateBlock(handle,parentBlock);
                    this.generateBlocks(handle, block);
                    break;
                case 2 :
                    String blockName = handle.getAttr("name");
                    BAbstract blockRef = update.getBlock(blockName);
                    this.generateBlocks(handle, blockRef);
                    break;
                case 3 :
                    this.generateAction(handle, parentBlock);
                    break;
            }
        }
        return this;
    }

    private Layout generateAction(XmlElement action,BAbstract parentBlock) {
        String method = action.getAttr("method");
        Class<?> [] classes = new Class[1];
        classes[0] = XmlElement.class;
        boolean hasMehtod = ObjectUtil.isMethodExist(parentBlock, method, classes);
        if (hasMehtod) {
            Object [] data = new Object[1];
            data[0] = action;
            ObjectUtil.invokeMethod(parentBlock, method, classes, data);
        }
        return this;
    }

    private BAbstract generateBlock(XmlElement handle, BAbstract parentBlock) {
        Map<String, String> attrMap = handle.getAttr();
        Update update = this.getUpdate();
        String type = handle.getAttr("type");
        String blockName = handle.getAttr("name");
        String alias = handle.getAttr("as");
        BAbstract block = update.getBlock(blockName);

        if (block == null) {
            block = (BAbstract)ZzApp.getBlock(type);
            Iterator<String> iterator = attrMap.keySet().iterator();
            while(iterator.hasNext()) {
                String key = iterator.next();
                block.addData(key, attrMap.get(key));
            }
            block.setLayout(this);
            update.addBlock(blockName, block);
            
        }
        if (parentBlock == null) {
            parentBlock = block;
            update.setTopBlock(parentBlock);
        }
        else {
            if (StringUtil.isBlank(alias)) {
                alias = "alias_" + (parentBlock.getChildren().size()+1);
            }
            parentBlock.setChild(alias, block);
        }

        return block;
    }

    public Layout loadLayoutXml() {
        if (layoutXmlElement == null) {
            Design design = this.getDesign();
            XmlElement xmlElement = ZzApp.getZzConfig().getNode(design.getArea()+"/layout/updates");
            List<XmlElement> moduleXmlElement = xmlElement.getChildren();
            for(XmlElement item : moduleXmlElement) {
                String layoutFileName = item.getSubNodeValue("file");
                String layoutFile = design.getLayoutFile(layoutFileName);
                if (layoutFile == null) continue;
                if (this.layoutXmlElement == null) {
                    this.layoutXmlElement = XmlUtil.parseFile(layoutFile);
                }
                else {
                    XmlUtil.mergeFile(this.layoutXmlElement, layoutFile, false);
                }
            }
        }
        return this;
    }

    private int getMapIntOfHandle(String tagName) {
        Map<String,Integer> handlesMap = new HashMap<String,Integer>();
        handlesMap.put("block", 1);
        handlesMap.put("reference", 2);
        handlesMap.put("action", 3);
        if (!handlesMap.containsKey(tagName)) {
            return 0;
        }
        return handlesMap.get(tagName);
    }
}