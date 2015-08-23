package com.youthor.zz.core.blocks;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.youthor.zz.common.ZzApp;
import com.youthor.zz.common.ZzContext;
import com.youthor.zz.common.ZzObject;
import com.youthor.zz.common.c.block.BAbstract;
import com.youthor.zz.common.util.StringUtil;
import com.youthor.zz.common.xml.XmlElement;
import com.youthor.zz.core.models.Design;
import com.youthor.zz.core.models.template.TApp;
import com.youthor.zz.core.models.template.TContext;
import com.youthor.zz.core.models.template.TTemplate;

public class Template extends BAbstract{

    public String getTemplate() {
        return (String)this.getData("template");
    }

    public void setTemplate(String template) {
        this.addData("template", template);
    }

    public Template setTemplate(XmlElement xmlElement) {
        String template = xmlElement.getSubNodeValue("template");
        this.addData("template", template);
        return this;
    }

    public Template addJs(XmlElement xmlElement) {
            String jsName = xmlElement.getSubNodeValue("script");
            XmlElement paramsXmlElement = xmlElement.getOneSubNode("params");
            Map<String, String> params = new HashMap<String, String>();
            if (paramsXmlElement != null) {
                params = paramsXmlElement.getAttr();
            }
            String ifConfig = xmlElement.getSubNodeValue("if");
            this.addItem("js", jsName, params, ifConfig);
            return this;
    }

    public Template addCss(XmlElement xmlElement) {
        String cssName = xmlElement.getSubNodeValue("stylesheet");
        XmlElement paramsXmlElement = xmlElement.getOneSubNode("params");
        Map<String, String> params = new HashMap<String, String>();
        if (paramsXmlElement != null) {
            params = paramsXmlElement.getAttr();
        }
        String ifConfig = xmlElement.getSubNodeValue("if");
        this.addItem("skin_css", cssName, params, ifConfig);
        return this;
    }

    public Template addItem(XmlElement xmlElement) {
        String type = xmlElement.getSubNodeValue("type");
        String name = xmlElement.getSubNodeValue("name");
        String ifConfig = xmlElement.getSubNodeValue("if");
        XmlElement paramsXmlElement = xmlElement.getOneSubNode("params");
        Map<String, String> params = new HashMap<String, String>();
        if (paramsXmlElement != null) {
            params = paramsXmlElement.getAttr();
        }
        this.addItem(type, name, params, ifConfig);
        return this;
    }

    public Template addItem(String type, String name, Map<String, String> params, String ifConfig) {
        ZzObject zzObject = this.addItem(type, name);
        zzObject.addData("ifConfig", ifConfig);
        zzObject.addData("params", params);
        return this;
    }

    public Template addItem(String type, String name, Map<String, String> params) {
        ZzObject zzObject = this.addItem(type, name);
        zzObject.addData("params", params);
        return this;
    }

    

    @SuppressWarnings("unchecked")
    public ZzObject addItem(String type, String name) {
        ZzObject zzObject = new ZzObject();
        zzObject.addData("type", type);
        zzObject.addData("name", name);
        List<ZzObject> skinItems = (List<ZzObject>)this.getData("skin_items");
        if (skinItems == null) {
            skinItems = new ArrayList<ZzObject>();
            this.addData("skin_items", skinItems);
        }
        skinItems.add(zzObject);
        return zzObject;
    }

    @SuppressWarnings("unchecked")
    public List<ZzObject> getSkinItems() {
    	List<ZzObject> skinItems = (List<ZzObject>)this.getData("skin_items");
        if (skinItems == null) return Collections.emptyList();
        return skinItems;
    }

    public String getTemplateFile() {
        Design design = ZzApp.getDesign();
        return design.getTemplateFile(this.getTemplate());
    }

    public String getArea() {
        String areaInThread = (String)ZzContext.getContext().getObject("zz_area");
        return areaInThread;
    }

    public String renderView() {
        String templateEngine = ZzApp.getZzConfig().getNode("default/design/template_engine").getValue();
        TApp tApp = (TApp)ZzApp.getTemplateApp(templateEngine);
        TContext tContext = tApp.getContext();
        tContext.put("this", this);
        TTemplate tTemplate = tApp.getTemplate(this.getTemplate());
        String html = tTemplate.toText(tContext);
        return html;
    }

    protected String _toHtml()
    {
        if (StringUtil.isBlank(this.getTemplate())) {
            return "";
        }
        String html = this.renderView();
        return html;
    }
}