package com.youthor.zz.core.models.template;

public abstract class TContext {

    public abstract TContext put(String key, Object value);
    public abstract Object getInnerContext();
}