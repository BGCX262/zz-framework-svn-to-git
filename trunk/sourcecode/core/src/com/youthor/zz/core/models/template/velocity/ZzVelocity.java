package com.youthor.zz.core.models.template.velocity;

import java.util.Properties;

import org.apache.velocity.Template;
import org.apache.velocity.app.Velocity;

import com.youthor.zz.common.ZzApp;
import com.youthor.zz.core.models.Design;
import com.youthor.zz.core.models.template.TApp;
import com.youthor.zz.core.models.template.TContext;
import com.youthor.zz.core.models.template.TTemplate;

public class ZzVelocity extends TApp{

    public ZzVelocity() {
        this.init();
    }
    
    private void init() {
        String fileSeparator = ZzApp.getZzConfig().getOptions().getFileSeparator();
        String tempPath = ZzApp.getAppPath() + ZzApp.getAppendPath() + fileSeparator +"design" + fileSeparator ;
        Properties p = new Properties(); 
        p.setProperty(Velocity.FILE_RESOURCE_LOADER_PATH, tempPath);
        p.setProperty(Velocity.RUNTIME_LOG, tempPath + "velocity.log");
        Velocity.init(p);
    }

    @Override
    public TContext getContext() {
        return new ZzVelocityContext();
    }

    @Override
    public TTemplate getTemplate(String templateFile) {
        return this.getTemplate(templateFile, "UTF-8");
    }

    @Override
    public TTemplate getTemplate(String templateFile, String characterSet) {
        Design design = ZzApp.getDesign();
        templateFile = design.getRelativeTemplateFile(templateFile);
        Template template = Velocity.getTemplate(templateFile, characterSet);
        ZzVelocityTemplate zzVelocityTemplate = new ZzVelocityTemplate();
        zzVelocityTemplate.setTemplate(template);
        return zzVelocityTemplate;
    }
}
