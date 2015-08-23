package com.youthor.zz.cms.blocks;

import com.youthor.zz.common.ZzApp;
import com.youthor.zz.common.c.block.BAbstract;
import com.youthor.zz.common.filter.ZzFilter;
import com.youthor.zz.page.blocks.Html;
import com.youthor.zz.page.blocks.html.Head;

public class Page extends BAbstract {
    public com.youthor.zz.cms.models.Page getPage() {
       com.youthor.zz.cms.models.Page pageModel = (com.youthor.zz.cms.models.Page)this.getRequest().getParam("page");
       return pageModel;
    }
    
    protected BAbstract prepareLayout() {
        com.youthor.zz.cms.models.Page pageModel = this.getPage();
        Html root = (Html)this.getUpdate().getBlock("root");
        if(root != null) {
            root.addBodyClass("cms-" +pageModel.getData("identifier"));
        }
        Head head = (Head)this.getUpdate().getBlock("head");
        head.setTitle((String)pageModel.getData("title"));
        head.setDescription((String)pageModel.getData("description"));
        head.setKeywords((String)pageModel.getData("meta_description"));
        return this;
    }

    protected String _toHtml() {
        com.youthor.zz.cms.models.Page pageModel = this.getPage();
        String content = (String)pageModel.getData("content");
        ZzFilter zzFilter = (ZzFilter)ZzApp.getSingletonModel("core/textFilter");
        String html = zzFilter.filter(content);
        return html;
    }
}
