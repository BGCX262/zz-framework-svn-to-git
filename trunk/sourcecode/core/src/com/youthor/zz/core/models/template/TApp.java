package com.youthor.zz.core.models.template;


public abstract class TApp {
    public abstract TContext getContext();
    public abstract TTemplate getTemplate(String templateFile);
    public abstract TTemplate getTemplate(String templateFile, String characterSet);
}