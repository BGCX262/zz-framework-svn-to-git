package com.youthor.zz.core.models.template.velocity;

import org.apache.velocity.VelocityContext;

import com.youthor.zz.core.models.template.TContext;

public class ZzVelocityContext extends TContext{

    private VelocityContext velocityContext = null;

    public ZzVelocityContext() {
        velocityContext = new VelocityContext();
    }

    public TContext put(String key, Object value) {
        velocityContext.put(key, value);
        return this;
    }

    @Override
    public Object getInnerContext() {
        return this.velocityContext;
    }
}
