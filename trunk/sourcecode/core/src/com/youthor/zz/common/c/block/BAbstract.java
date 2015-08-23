package com.youthor.zz.common.c.block;

import java.util.HashMap;
import java.util.Map;

import com.youthor.zz.common.ZzApp;
import com.youthor.zz.common.ZzContext;
import com.youthor.zz.common.ZzObject;
import com.youthor.zz.core.models.http.Request;
import com.youthor.zz.core.models.http.Response;
import com.youthor.zz.common.xml.XmlElement;
import com.youthor.zz.core.models.Layout;
import com.youthor.zz.core.models.layout.Update;

public abstract class BAbstract extends ZzObject{

    private BAbstract parentBlock = null;
    private Map<String, BAbstract> children = new HashMap<String, BAbstract>();
    private Layout layout = null;

    public Update getUpdate() {
        ZzContext zzContext = ZzContext.getContext();
        Update update = (Update)zzContext.getObject("zz_update");
        if (update == null) {
            update = (Update)ZzApp.getModel("core/layout_update");
            zzContext.addObject("zz_update", update);
        }
        return update;
    }

    public Layout getLayout() {
        return this.layout;
    }

    public BAbstract setLayout(Layout layout) {
        this.layout = layout;
        this.prepareLayout();
        return this;
    }

    protected BAbstract prepareLayout() {
        return this;
    }

    public String getName() {
        return (String)this.getData("name");
    }

    public BAbstract getParentBlock() {
        return parentBlock;
    }

    public void setParentBlock(BAbstract parentBlock) {
        this.parentBlock = parentBlock;
    }

    public String getAlias() {
        return (String)this.getData("alias");
    }

    public Map<String, BAbstract> getChildren() {
        return this.children;
    }

    public BAbstract getChild(String alias) {
        if (this.children.containsKey(alias)) {
            return this.children.get(alias);
        }
        return null;
    }

    public BAbstract setChild(String alias, BAbstract block) {
        this.children.put(alias, block);
        return this;
    }

    public BAbstract unsetChild(String alias) {
        if (this.children.containsKey(alias)) {
            this.children.remove(alias);
        }
        return this;
    }

    public BAbstract unsetChildren() {
        this.children.clear();
        return this;
    }
    
    public BAbstract unsetChildren(XmlElement xmlElement) {
        return this.unsetChildren();
    }

    public String getChildHtml(String alias) {
        if (this.getChildren().containsKey(alias)) {
            BAbstract childBlock = this.getChildren().get(alias);
            return childBlock.toHtml();
        }
        return "";
    }

    public Request getRequest() {
        return ZzContext.getContext().getRequest();
    }

    public Response getResponse() {
        return  ZzContext.getContext().getResponse();
    }

    final public String toHtml()
    {
        this.beforeToHtml();
        String html = this._toHtml();
        html = afterToHtml(html);
        return html;
    }

    protected BAbstract beforeToHtml() {
        return this;
    }

    protected String afterToHtml(String html) {
        return html;
    }

    protected String _toHtml() {
        return "";
    }
    
    public BAbstract unsetChild(XmlElement xmlElement) {
        String alias = xmlElement.getSubNodeValue("alias");
        return this.unsetChild(alias);
    }
}
