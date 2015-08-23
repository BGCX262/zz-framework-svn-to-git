package com.youthor.zz.core.models.layout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.youthor.zz.common.c.block.BAbstract;
import com.youthor.zz.common.xml.XmlElement;
import com.youthor.zz.core.models.Layout;

public class Update {

    private static Map<String, String> uriXml = new HashMap<String, String>();
    private List<String> handles= new ArrayList<String>();
    private StringBuffer blockXml = new StringBuffer();
    private Map<String, BAbstract> blocks = new HashMap<String, BAbstract>();
    private BAbstract topBlock = null;
    private boolean isFromMap = true; 

    public BAbstract getTopBlock() {
        return topBlock;
    }

    public void setTopBlock(BAbstract topBlock) {
        this.topBlock = topBlock;
    }

    public Update addBlock(String blockName, BAbstract block) {
        if(!this.blocks.containsKey(blockName)) {
            this.blocks.put(blockName, block);
        }
        return this;
    }

    public BAbstract getBlock(String blockName) {
        if(this.blocks.containsKey(blockName)) {
            return this.blocks.get(blockName);
        }
        return null;
    }

    public Map<String, BAbstract> getBlocks() {
        return this.blocks;
    }

    public boolean isFromMap() {
        return this.isFromMap;
    }

    public void addBlockXml(String xml) {
        this.blockXml.append(xml);
    }

    public void setFromMap(boolean isFromMap) {
        this.isFromMap = isFromMap;
    }

    public Update addHandle(String handle) {
        if (this.handles.contains(handle)) {
            this.handles.remove(handle);
        }
        this.handles.add(handle);
        return this;
    }

    public List<String> getHandles() {
        return this.handles;
    }

    public Update resetHandles() {
        this.handles.clear();
        return this;
    }

    public Update removeHandle(String handle) {
        if (this.handles.contains(handle)) {
            this.handles.remove(handle);
        }
        return this;
    }

    public String getXmlByUri(String uri) {
        if (Update.uriXml.containsKey(uri)) {
            return Update.uriXml.get(uri);
        }
        return null;
    }

    public Update loadUpdateXml(String uri,Layout layout) {
        
        if (this.isFromMap) {
            if (uriXml.containsKey(uri)) {
                this.blockXml.append(uriXml.get(uri));
            }
            else {
                loadUpdateXml(layout);
                uriXml.put("uri", this.blockXml.toString());
            }
        }
        else {
            loadUpdateXml(layout);
        }
        return this;
    }

    public Update loadUpdateXml(Layout layout) {
        XmlElement xmlElement = layout.getLayoutXmlElement();
        List<String> handles = this.getHandles();
        for(String handle : handles) {
            XmlElement childXmlElement = xmlElement.getLayoutNode(handle);
            if (childXmlElement != null) {
                this.blockXml.append(childXmlElement.innerXml());
            }
        }
        return this;
    }

    public String getBlockXml() {
        return "<layout>" + this.blockXml.toString() + "</layout>";
    }
}