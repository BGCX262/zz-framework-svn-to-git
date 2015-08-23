package com.youthor.zz.core.models;

import java.util.Map;

import com.youthor.zz.cms.blocks.Block;
import com.youthor.zz.common.ZzApp;
import com.youthor.zz.common.filter.ZzFilterTemplate;

public class TextFilter extends ZzFilterTemplate{
    public String varDirective(Map<String, String> paras) {
        return "varDirective";
    }

    public String blockDirective(Map<String, String> paras) {
        String blockId = (String)paras.get("id");
        Block block = (Block)ZzApp.getBlock("cms/block");
        block.addData("identifier", blockId);
        return block.toHtml();
    }

    public static void main(String[] args) {
        ZzFilterTemplate zzFilterTemplate = new TextFilter();
        String text = zzFilterTemplate.filter("fgdfgdgf{{var para1=\"bb\"  para2=\"ccc\"}}dfsdfsdfsdffgdfgdgf{{block template=\"cc.zz\"}}dfsdfsdfsdf{{block template=\"bb.zz\"}}{{block template=\"bb.zz\"}}");
        System.out.println(text);
    }
}
