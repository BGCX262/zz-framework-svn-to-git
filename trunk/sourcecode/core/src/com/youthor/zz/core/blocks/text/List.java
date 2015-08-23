package com.youthor.zz.core.blocks.text;

import java.util.Iterator;
import java.util.Map;

import com.youthor.zz.common.c.block.BAbstract;
import com.youthor.zz.core.blocks.Text;

public class List extends Text{
    protected String _toHtml()
    {
        this.setText("");
        Map<String, BAbstract> children = this.getChildren();
        Iterator<String> iterator = children.keySet().iterator();
        while(iterator.hasNext()) {
            String key = iterator.next();
            BAbstract block = children.get(key);
            if (block == null) {
                //@TODO Exception
            }
            else {
                this.addText(block.toHtml().toString());
            }
        }

        return super._toHtml();
    }
}
