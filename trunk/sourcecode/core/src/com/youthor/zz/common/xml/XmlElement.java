package com.youthor.zz.common.xml;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.youthor.zz.common.ZzConstant;
import com.youthor.zz.common.util.StringUtil;

public class XmlElement {
    private String tagName;
    private String parentTagName;
    private String fullTagName;
    private List<XmlElement> childrenList = new ArrayList<XmlElement>();
    private Map<String,String> attrs;
    private String value;

    public XmlElement(){
        tagName = "";
        fullTagName = "";
        attrs = new HashMap<String,String>();
    }

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    public String getFullTagName() {
        return fullTagName;
    }

    public void setFullTagName(String fullTagName) {
        this.fullTagName = fullTagName;
    }

    public void addChild(XmlElement oneChildren){
        if(this.childrenList == null){
            this.childrenList = new ArrayList<XmlElement>();
        }
        this.childrenList.add(oneChildren);
    }

    public void addAttr(String key,String value){
        attrs.put(key, value);
    }

    public Map<String,String> getAttr(){
        return attrs;
    }

    public String getAttr(String attrName) {
        if (this.attrs != null && this.attrs.containsKey(attrName)) {
            return this.attrs.get(attrName);
        }
        return "";
    }

    public List<XmlElement> getChildren(){
        return childrenList;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getParentTagName() {
        return parentTagName;
    }

    public void setParentTagName(String parentTagName) {
        this.parentTagName = parentTagName;
    }

    public List<XmlElement> getTagNode(String tagName, boolean fullTagMatch){
        List<XmlElement> list = new ArrayList<XmlElement>();
        
        if(tagName.endsWith(ZzConstant.XML_NODE_SPLIT_SIGN) && tagName.length() > 1){
            tagName = tagName.substring(0,tagName.length() -1);
        }
        if(StringUtil.isBlank(tagName)){
            return list;
        }
        else{
            this.getTagNode(list, tagName, this, fullTagMatch);
        }
        
        return list;
    }

    public XmlElement getNode(String path) {
        StringBuilder tagName = new StringBuilder(ZzConstant.CONFIG_ALL_XML_NAMESPACE + ZzConstant.XML_NODE_SPLIT_SIGN);
        if (StringUtil.isNotBlank(path)) {
            tagName.append(path);
        }

        List<XmlElement> list = this.getTagNode(tagName.toString(), true);
        return (list == null || list.size() == 0) ? null : list.get(0);
    }

    public XmlElement getNode(String path,String flag) {
        StringBuilder tagName = new StringBuilder(ZzConstant.XML_NODE_SPLIT_SIGN + flag + ZzConstant.XML_NODE_SPLIT_SIGN);
        if (StringUtil.isNotBlank(path)) {
            tagName.append(path);
        }

        List<XmlElement> list = this.getTagNode(tagName.toString(), true);
        return (list == null || list.size() == 0) ? null : list.get(0);
    }

    public XmlElement getLayoutNode(String path) {
        StringBuilder tagName = new StringBuilder(ZzConstant.LAYOUT_ALL_XML_NAMESPACE + ZzConstant.XML_NODE_SPLIT_SIGN);
        if (StringUtil.isNotBlank(path)) {
            tagName.append(path);
        }
        List<XmlElement> list = this.getTagNode(tagName.toString(), true);
        return (list == null || list.size() == 0) ? null : list.get(0);
    }

    public String getConfigValue(String path) {
        XmlElement element = getNode(path);
        if (element != null) {
            return element.getValue();
        } else {
            return ZzConstant.NULL_STRING_VALUE;
        }
    }

    /**
     * 得到直接子节下特定节点名称值
     * @param subTagName:子节点名称
     * @param singleFlag:是否只取一个
     * @param mutilSplitor:如果取多个的话，多个值的分隔符
     * @return
     */
    public String getSubNodeByValue(String subTagName,boolean singleFlag, String separator){
        if(getChildren() == null || getChildren().size() == 0){
            return ZzConstant.NULL_STRING_VALUE;
        }

        StringBuilder sb = new StringBuilder();
        boolean isFirst = true;
        for(XmlElement node : getChildren()){
            if(subTagName.equals(node.getTagName())){
                if(singleFlag){
                    return node.getValue();
                }
                else{
                    if(!isFirst){
                        sb.append(separator);
                        isFirst = false;
                    }
                    sb.append(node.getValue());
                }
            }
        }
        return sb.toString();
    }

    public String getSubNodeValue(String subTagName){
        return getSubNodeByValue(subTagName, true, "/");
    }

    /**
     * 得到直接子节下特定节
     * @param subTagName:子节点名称
     * @return
     */
    public List<XmlElement> getSubNode(String subTagName) {
        if(getChildren() == null || getChildren().size() == 0) {
            return null;
        }

        List<XmlElement> subNodeList = new ArrayList<XmlElement>();
        for(XmlElement node : getChildren()){
            if(subTagName.equals(node.getTagName())) {
                subNodeList.add(node);
            }
        }
        return subNodeList;
    }

    public XmlElement getOneSubNode(String subTagName) {
        List<XmlElement> children = getSubNode(subTagName);
        if (children != null && !children.isEmpty()) return children.get(0);
        return null;
    }

    protected void getTagNode(List<XmlElement> list, String tageName, XmlElement curNode, boolean fullTagMatch) {
        if((fullTagMatch && tageName.equals(curNode.getFullTagName()) || ((!fullTagMatch) && tageName.equals(curNode.getTagName())))){
            list.add(curNode);
        }
        else{
            List<XmlElement> subList = curNode.getChildren();
            if(subList != null && subList.size() > 0){
                for(XmlElement subNode : subList){
                    this.getTagNode(list, tageName, subNode, fullTagMatch);
                }
            }
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        
        sb.append("<"  + tagName);
        if(attrs.size() > 0){
            for(String key : attrs.keySet()){
                sb.append(" " + key + "=\"" + attrs.get(key) + "\"");
            }
        }
        sb.append(">");
        if(StringUtil.isNotBlank(this.getValue())){
            sb.append(this.getValue());
        }
        if(childrenList != null && childrenList.size() > 0){
            for(XmlElement node : childrenList){
                sb.append(node.toString());
            }
        }
        sb.append("</" + tagName +">");
        
        return sb.toString();
    }

    public String innerXml() {
        StringBuilder sb = new StringBuilder();
        if(childrenList != null && childrenList.size() > 0){
            for(XmlElement node : childrenList){
                sb.append(node.toString());
            }
        }
        return sb.toString();
    }
}