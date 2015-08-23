package com.youthor.zz.cms.blocks;

import com.youthor.zz.common.ZzApp;
import com.youthor.zz.common.c.block.BAbstract;
import com.youthor.zz.common.filter.ZzFilter;
import com.youthor.zz.common.xml.XmlElement;


public class Block extends BAbstract {

    protected String _toHtml() {
        String html = "";
        String identifier = (String)this.getData("identifier");
        com.youthor.zz.cms.models.Block mBlock = (com.youthor.zz.cms.models.Block)ZzApp.getModel("cms/block");
        //for block test
        mBlock.load(identifier, "testBlock");
        mBlock.addData("id", 1);
        mBlock.addData("is_active", 1);
        mBlock.addData("content", "i am block");
        //end for block test
        if (mBlock.getData("id") != null) {
            int isActive = (Integer)mBlock.getData("is_active");
            if (isActive == 1) {
                String content = (String)mBlock.getData("content");
                ZzFilter zzFilter = (ZzFilter)ZzApp.getSingletonModel("core/textFilter");
                html = zzFilter.filter(content);
            }
        }
        return html;
    }

    public void setBlockId(XmlElement xmlElement) {
        String identifier = xmlElement.getSubNodeValue("block_id");
        this.addData("identifier", identifier);
    }
}
