package com.youthor.zz.core.blocks;

import com.youthor.zz.common.c.block.BAbstract;

public class Text extends BAbstract{

    public void setText(String text) {
        StringBuffer sb = new StringBuffer();
        this.addData("text", sb);
    }

    public String getText() {
        StringBuffer sb = (StringBuffer)this.getData("text");
        if (sb == null) {
            return "";
        }
        return sb.toString();
    }

    public void addText(String text) {
        StringBuffer sb = (StringBuffer)this.getData("text");
        if (sb == null) {
            sb = new StringBuffer();
            this.addData("text", sb);
        }
        sb.append(text);
    }

    public void addBeforeText(String text) {
        StringBuffer sb = (StringBuffer)this.getData("text");
        if (sb == null) {
            sb = new StringBuffer();
            this.addData("text", sb);
        }
        sb.insert(0, text);
    }

    protected String _toHtml() {
        return this.getText();
    }
}
