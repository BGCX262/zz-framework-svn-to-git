package com.youthor.zz.core.models;

import com.youthor.zz.common.ZzApp;
import com.youthor.zz.common.ZzContext;
import com.youthor.zz.common.ZzObject;
import com.youthor.zz.common.util.FileUtil;

public class Design extends ZzObject {

    public void setArea(String area) {
        this.addData("area", area);
    }

    public String getArea() {
        if (this.getData("area") == null) {
            return (String)ZzContext.getContext().getObject("zz_area");
        }
        return (String)this.getData("area");
    }

    public void setTheme(String theme) {
        this.addData("theme", theme);
    }

    public String getTheme() {
        if (this.getData("theme") == null) {
            return this.getDefaultTheme();
        }
        return (String)this.getData("theme");
    }

    public void setPackage(String packageName) {
        this.addData("package", packageName);
    }

    public String getPackage() {
        if (this.getData("package") == null) {
            return this.getDefaultPackage();
        }
        return (String)this.getData("package");
    }

    public String getDefaultPackage() {
        return ZzApp.getZzConfig().getNode("default/design/"+this.getArea()+"/package").getValue();
    }

    public String getDefaultTheme() {
        return ZzApp.getZzConfig().getNode("default/design/"+this.getArea()+"/theme").getValue();
    }

    public String getRelativeTemplateFile (String templateFile) {
    	 String templateFileTemp = this.getPath("template", this.getPackage(), this.getTheme()) + templateFile;
         if (FileUtil.isFileExsit(templateFileTemp)) {
             return this.getRelativePath("template", this.getPackage(), this.getTheme(), templateFile);
         }
         templateFileTemp = this.getPath("template", this.getDefaultPackage(), this.getDefaultTheme()) + templateFile;
         if (FileUtil.isFileExsit(templateFileTemp)) {
            return this.getRelativePath("template", this.getDefaultPackage(), this.getDefaultTheme(), templateFile);
         }
         return null;
    }

    public String getDesignPath () {
        String fileSeparator = ZzApp.getZzConfig().getOptions().getFileSeparator();
        String tempPath = ZzApp.getAppPath() + ZzApp.getAppendPath() + fileSeparator +"design" + fileSeparator;
        return tempPath;
    }

    public String getTemplatePath () {
        return this.getPath("template", this.getPackage(), this.getTheme());
    }

    public String getTemplateFile (String templateFile) {
       return this.getFile("template", templateFile);
    }

    public String getLayoutFile (String layoutFile) {
        return this.getFile("layout", layoutFile);
     }

    public String getLayoutPath () {
        return this.getPath("layout", this.getPackage(), this.getTheme());
    }

    private String getFile(String type, String file) {
         String templateFileTemp = this.getPath(type, this.getPackage(), this.getTheme()) + file;
         if (FileUtil.isFileExsit(templateFileTemp)) {
             return templateFileTemp;
         }
         templateFileTemp = this.getPath(type, this.getDefaultPackage(), this.getDefaultTheme()) + file;
         if (FileUtil.isFileExsit(templateFileTemp)) {
             return templateFileTemp;
         }
         return null;
    }

    private String getPath (String type, String packageName, String theme) {
        String fileSeparator = ZzApp.getZzConfig().getOptions().getFileSeparator();
        StringBuffer  sb = new StringBuffer();
        sb.append(this.getDesignPath());
        sb.append(this.getArea());
        sb.append(fileSeparator);
        sb.append(packageName);
        sb.append(fileSeparator);
        sb.append(theme);
        sb.append(fileSeparator);
        sb.append(type);
        sb.append(fileSeparator);
        return sb.toString();
    }

    private String getRelativePath (String type, String packageName, String theme, String templateFile) {
        String fileSeparator = ZzApp.getZzConfig().getOptions().getFileSeparator();
        StringBuffer  sb = new StringBuffer();
        sb.append(this.getArea());
        sb.append(fileSeparator);
        sb.append(packageName);
        sb.append(fileSeparator);
        sb.append(theme);
        sb.append(fileSeparator);
        sb.append(type);
        sb.append(fileSeparator);
        sb.append(templateFile);
        return sb.toString();
    }

}
