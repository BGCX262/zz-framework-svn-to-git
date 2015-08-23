package com.youthor.zz.core.models.template.velocity;

import java.io.StringWriter;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;

import com.youthor.zz.core.models.template.TContext;
import com.youthor.zz.core.models.template.TTemplate;

public class ZzVelocityTemplate extends TTemplate{

    private Template template = null;

    public void setTemplate(Template template) {
        this.template = template;
    }

    @Override
    public String toText(TContext tContext) {
        StringWriter sw = new StringWriter();
        VelocityContext context = (VelocityContext)tContext.getInnerContext();
        try {    
            this.template.merge(context, sw);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return sw.toString();
    }

}