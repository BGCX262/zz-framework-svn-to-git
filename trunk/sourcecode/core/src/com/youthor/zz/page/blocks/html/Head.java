package com.youthor.zz.page.blocks.html;

import com.youthor.zz.core.blocks.Template;

public class Head extends Template {

    public Head setTitle(String title) {
        this.addData("title", title);
        return this;
    }

    public String getTitle() {
        return (String)this.getData("title") == null ? "" : (String)this.getData("title");
    }

    public Head setContentType(String ContentType) {
        this.addData("content_type", ContentType);
        return this;
    }

    public String getContentType() {
        return (String)this.getData("content_type") == null ? "" : (String)this.getData("content_type");
    }

    public Head setDescription(String description) {
        this.addData("description", description);
        return this;
    }

    public String getDescription() {
        return (String)this.getData("description") == null ? "" : (String)this.getData("description");
    }

    public Head setKeywords(String keywords) {
        this.addData("keywords", keywords);
        return this;
    }

    public String getKeywords() {
        return (String)this.getData("keywords") == null ? "" : (String)this.getData("keywords");
    }

    public Head setRobots(String robots) {
        this.addData("robots", robots);
        return this;
    }

    public String getRobots() {
        return (String)this.getData("robots") == null ? "" : (String)this.getData("robots");
    }
}