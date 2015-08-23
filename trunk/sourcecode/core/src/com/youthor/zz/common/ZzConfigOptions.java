package com.youthor.zz.common;

import com.youthor.zz.common.util.StringUtil;

public class ZzConfigOptions extends ZzObject {
    //文件分隔符
    private String fileSeparator;

    //得到文件的分隔符
    public String getFileSeparator() {
        if(StringUtil.isBlank(fileSeparator)) {
            fileSeparator = System.getProperty("file.separator");
        }
        return fileSeparator;
    }

    public String getModuleEtcDir() {
        StringBuilder sb = new StringBuilder(this.getBase());
        String etc = ZzApp.getZzConfig().getConfigValue("default/system/filesystem/etc");
        sb.append(etc);
        sb.append(this.getFileSeparator() + ZzConstant.ETC_DIR_NAME_IN_APP);
        return sb.toString();
    }

    public String getLocalFile() {
        StringBuilder sb = new StringBuilder(this.getBase());
        String etc = ZzApp.getZzConfig().getConfigValue("default/system/filesystem/etc");
        sb.append(etc);
        sb.append(this.getFileSeparator() + ZzConstant.ETC_DIR_NAME_IN_APP);
        sb.append(this.getFileSeparator() + ZzConstant.ETC_LOCAL);
        return sb.toString();
    }

    public String getClassDir() {
        StringBuilder sb = new StringBuilder(this.getBase());
        String etc = ZzApp.getZzConfig().getConfigValue("default/system/filesystem/class");
        sb.append(etc);
        return sb.toString();
    }

    public String getLibDir() {
        StringBuilder sb = new StringBuilder(this.getBase());
        String etc = ZzApp.getZzConfig().getConfigValue("default/system/filesystem/lib");
        sb.append(etc);
        return sb.toString();
    }

    public String getModuleEtcCacheFileName() {
        StringBuilder sb = new StringBuilder(this.getBase());
        String etc = ZzApp.getZzConfig().getConfigValue("default/system/filesystem/etc");
        sb.append(etc);
        sb.append(this.getFileSeparator() + ZzConstant.MODULE_MERGED_CACHE_NAME);
        return sb.toString();
    }

    public String getBase() {
        String base = ZzApp.getZzConfig().getConfigValue("default/system/filesystem/base");
        if (StringUtil.isBlank(base)) {
            base = ZzApp.getAppPath();
        }
        return base;
    }
}
