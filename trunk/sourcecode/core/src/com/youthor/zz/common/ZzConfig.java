package com.youthor.zz.common;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import com.youthor.zz.common.util.FileUtil;
import com.youthor.zz.common.util.StringUtil;
import com.youthor.zz.common.util.XmlUtil;
import com.youthor.zz.common.xml.XmlElement;

public class ZzConfig {

    private ZzConfigOptions options = null;
    // 一个整体合并后的系统配置文件
    private XmlElement configXmlElement;

    // 是否已经做了初始化
    // private boolean initZzConfig = false;

    public void clear() {
        this.clear(this.configXmlElement);
        this.options = null;
    }

    private void clear(XmlElement configXmlElement) {
        List<XmlElement> children = configXmlElement.getChildren();
        if (children != null && !children.isEmpty()) {
            for(XmlElement child : children) {
                this.clear(child);
                child = null;
            }
        }
        configXmlElement = null;
    }

    public ZzConfig() {
        this.options = new ZzConfigOptions();
        this.configXmlElement = new XmlElement();
    }

    // 初始化
    public synchronized ZzConfig init() throws Exception {
        this.configXmlElement = XmlUtil.parseFile(ZzApp.getAppPath() + this.options.getFileSeparator() + ZzApp.getAppendPath() +"/etc/local.xml");
        loadModules();
        //saveCache();
        return this;
    }

    public ZzConfigOptions getOptions() {
        return this.options;
    }

    public ZzConfig cleanCache() throws Exception {
        FileUtil.deleteFile(this.options.getModuleEtcCacheFileName());
        return reinit();
    }

    /**
     * Reinitialize configuration
     */
    public ZzConfig reinit() throws Exception {
        return init();
    }

    // 得到所有的配置
    public XmlElement getNode(String path) {
        return this.configXmlElement.getNode(path);
    }

    public XmlElement getResourceConnectionConfig(String name) {
        XmlElement returnValue = null;
        XmlElement initConfig = this.getNode("global/resources/" + name + "/connection"); 
        if (initConfig == null) {
            returnValue = this.getNode("global/resources/default_setup/connection");
        }
        else {
            XmlElement userConfig  = initConfig.getNode("use");
            if (userConfig != null) {
                String use = userConfig.getValue();
                returnValue = this.getResourceConnectionConfig(use);
            }
        }
        return returnValue;
    }

    public String getConfigValue(String path) {
        XmlElement element = this.getNode(path);
        if (element != null) {
            return element.getValue();
        } else {
            return ZzConstant.NULL_STRING_VALUE;
        }
    }

    @SuppressWarnings("unchecked")
    protected ZzConfig loadModules() throws Exception {
        // list文件不包含完整的访问路径的
        List<String> xmlFileSortedList = this.getDeclaredModuleFiles();
        if (xmlFileSortedList != null && xmlFileSortedList.size() > 0) {
            // 解析ZZ_ALL
            String fileFullPath = this.options.getModuleEtcDir() + this.options.getFileSeparator();
            XmlUtil.mergeFile(this.configXmlElement, fileFullPath + xmlFileSortedList.get(0), true);
            xmlFileSortedList.remove(0);
            for (String fileName : xmlFileSortedList) {
                XmlUtil.mergeFile(this.configXmlElement, fileFullPath + fileName, true);
            }

            // 加载合并module的etc的config，list文件只包含class路径
            ZzObject sortedAndAtiveObj = this.sortModuleDepends();
            List<String> sortTagNameList = (List<String>) sortedAndAtiveObj.getData("sorted");
            Map<String, XmlElement> allActiveModuleNodeMap = (Map<String, XmlElement>) sortedAndAtiveObj.getData("active");

            if (sortTagNameList != null && sortTagNameList.size() > 0) {
                for (String moduleName : sortTagNameList) {
                    XmlElement element = allActiveModuleNodeMap.get(moduleName);
                    this.mergeEtcConfig(moduleName, element);
                }
            }
        }
        return this;
    }

    protected ZzConfig saveCache() {
        String filePath = this.options.getModuleEtcDir()+ this.options.getFileSeparator();
        String fileNameOnly = ZzConstant.MODULE_MERGED_CACHE_NAME;
        FileUtil.stringToFile(filePath, fileNameOnly, this.configXmlElement.toString());
        return this;
    }

    // 合并etc下config配置，把下一个配置文件的的信息添加现在的curXmlElement对象里
    protected void mergeEtcConfig(String moduleName, XmlElement element)
            throws Exception {
        String packageNodeTagName = ZzConstant.MODULE_CONFIG_PACKAGE_NAME;
        String packageName = element.getSubNodeByValue(packageNodeTagName, true, null);
        String fileNameSuffix = ZzConstant.MODULE_CONFIG_FILE_NAME_SUFFIX;
        String moduleEtcFile = "config.xml";

        // 先找classes下面有没有这个包，如果没有则找jar包里的路径
        String packagePath = StringUtil.replaceAll(packageName, ZzConstant.DOT_SIGN_SPLIT_NAME, options.getFileSeparator());
        StringBuilder sbClassPath = new StringBuilder(packagePath);
        sbClassPath.append(this.options.getFileSeparator());
        sbClassPath.append(ZzConstant.MODULE_CONFIG_ETC_DIR_NAME);

        StringBuilder sbClassFullPath = new StringBuilder(this.options.getClassDir());
        sbClassFullPath.append(this.options.getFileSeparator());
        sbClassFullPath.append(sbClassPath);
        sbClassFullPath.append(this.options.getFileSeparator() + moduleEtcFile);
        File file = new File(sbClassFullPath.toString());
        if (file.exists()) {
              XmlUtil.mergeFile(this.configXmlElement, sbClassFullPath.toString(),true);
        }
        // 找jar包里的路径
        else {
            String jarFilePath = options.getLibDir() + options.getFileSeparator() + moduleName + ".jar";
            JarFile jarFile = new JarFile(jarFilePath);
            Enumeration<JarEntry> jarEntityEnum = jarFile.entries();
            while (jarEntityEnum.hasMoreElements()) {
                JarEntry jarEntry = jarEntityEnum.nextElement();
                String fileName = jarEntry.getName();
                if (fileName.startsWith(sbClassPath.toString())
                        && fileName.toLowerCase().endsWith(fileNameSuffix)) {
                    InputStream inputStream = jarFile.getInputStream(jarEntry);
                    XmlUtil.mergeFile(this.configXmlElement, inputStream, true);
                }
            }
        }
    }

    // 对合并后的module进行排序
    protected ZzObject sortModuleDepends() {
        // 所有模块列表,key为tagName
        Map<String, XmlElement> allActiveModuleNodeMap = new HashMap<String, XmlElement>();
        String modelXmlNS = ZzConstant.MODULE_ALL_XML_NAMESPACE;

        // 得到模块配置的上级节点
        List<XmlElement> moduleXmlParentNodeList = this.configXmlElement.getTagNode(modelXmlNS, true);
        // 所有被依赖的模块，key为module的tagName，value为被依赖的tagName列表
        Map<String, List<String>> dependRelationMap = new HashMap<String, List<String>>();

        if (moduleXmlParentNodeList != null
                && moduleXmlParentNodeList.size() > 0) {
            for (XmlElement moduleNodeParent : moduleXmlParentNodeList) {
                List<XmlElement> moduleNodeList = moduleNodeParent.getChildren();
                if (moduleNodeList != null) {
                    for (XmlElement moduleNode : moduleNodeList) {
                        allActiveModuleNodeMap.put(moduleNode.getTagName(),moduleNode);
                        String dependsTagName = ZzConstant.MODULE_CONFIG_DEPENDS_NAME;
                        List<XmlElement> dependsList = moduleNode.getSubNode(dependsTagName);
                        if (dependsList != null && dependsList.size() > 0) {
                            for (XmlElement dependsNode : dependsList) {
                                List<XmlElement> dependsNodeChildren = dependsNode.getChildren();
                                if (dependsNodeChildren != null && dependsNodeChildren.size() > 0) {
                                    for (XmlElement dependsSubNode : dependsNodeChildren) {
                                        if (dependRelationMap.get(moduleNode.getTagName()) == null) {
                                            dependRelationMap.put(moduleNode.getTagName(),new ArrayList<String>());
                                        }
                                        List<String> dependedList = dependRelationMap.get(moduleNode.getTagName());
                                        if (!dependedList.contains(dependsSubNode.getTagName())) {
                                            dependedList.add(dependsSubNode.getTagName());
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // 判断是否有依赖死循环、有效等
        checkIsValidRelation(allActiveModuleNodeMap, dependRelationMap);
        // 排序
        List<String> sortTagNameList = sortModulePackage(allActiveModuleNodeMap, dependRelationMap);

        ZzObject object = new ZzObject();
        object.addData("sorted", sortTagNameList);
        object.addData("active", allActiveModuleNodeMap);

        return object;
    }

    // 根据依赖关系排序
    protected List<String> sortModulePackage(Map<String, XmlElement> allActiveModuleNodeMap,Map<String, List<String>> dependRelationMap) {
        List<String> sortTagNameList = new LinkedList<String>();

        Set<String> dependKeyList = dependRelationMap.keySet();
        for (String key : dependKeyList) {
            sortDependsModuleTagName(key, sortTagNameList, dependRelationMap);
        }

        for (String key : dependKeyList) {
            if (!sortTagNameList.contains(key)) {
                sortTagNameList.add(key);
            }
        }

        Set<String> activeKeyList = allActiveModuleNodeMap.keySet();
        for (String key : activeKeyList) {
            if (!sortTagNameList.contains(key)) {
                sortTagNameList.add(key);
            }
        }
        // sortTagNameList为已经排好序的模块名了
        return sortTagNameList;
    }

    // 递归调用排列依赖顺序
    protected void sortDependsModuleTagName(String curModuleTagName,List<String> sortTagNameList,Map<String, List<String>> dependRelationMap) {
        if (!dependRelationMap.containsKey(curModuleTagName)) {
            return;
        }

        if (!sortTagNameList.contains(curModuleTagName)) {
            sortTagNameList.add(curModuleTagName);
        }

        List<String> dependedList = dependRelationMap.get(curModuleTagName);
        for (String depends : dependedList) {
            if (sortTagNameList.contains(depends)) {
                sortTagNameList.remove(depends);
            }
            int index = sortTagNameList.indexOf(curModuleTagName);
            sortTagNameList.add(index, depends);
            // 递归
            sortDependsModuleTagName(depends, sortTagNameList,dependRelationMap);
        }
    }

    // 检查依赖是否满足条件
    protected void checkIsValidRelation(Map<String, XmlElement> allActiveModuleNodeMap,Map<String, List<String>> dependRelationMap) {
        if (dependRelationMap == null || dependRelationMap.size() == 0) {
            return;
        }

        Set<String> keyList = dependRelationMap.keySet();
        for (String key : keyList) {
            List<String> dependedList = dependRelationMap.get(key);
            this.checkIsValidRelation(key, dependedList,allActiveModuleNodeMap, dependRelationMap);
        }
    }

    protected void checkIsValidRelation(String activeModuleTagName,List<String> dependedList,Map<String, XmlElement> allActiveModuleNodeMap,
            Map<String, List<String>> dependRelationMap) {
        if (dependedList == null || dependedList.size() == 0) {
            return;
        }
        for (String nextTagName : dependedList) {
            this.checkIsValidRelation(activeModuleTagName, dependRelationMap.get(nextTagName), allActiveModuleNodeMap,dependRelationMap);
        }
    }

    protected List<String> getDeclaredModuleFiles() {
        File file = new File(this.options.getModuleEtcDir());
        List<String> allFileList = new ArrayList<String>();
        List<String> zzAllFileList = new ArrayList<String>();
        List<String> zzFileList = new ArrayList<String>();
        List<String> customFileList = new ArrayList<String>();
        if (file.isDirectory()) {
            String[] fileNames = file.list();
            for (String fileName : fileNames) {
                String fileNameSuffix = ZzConstant.MODULE_CONFIG_FILE_NAME_SUFFIX;
                if (!fileName.toLowerCase().endsWith(fileNameSuffix)) {
                    continue;
                }
                if (fileName.startsWith(ZzConstant.MODULE_ALL_XML_NAME_PREFIX)) {
                    zzAllFileList.add(fileName);
                } else if (fileName.startsWith(ZzConstant.MODULE_ZZ_XML_NAME_PREFIX)) {
                    zzFileList.add(fileName);
                } else {
                    customFileList.add(fileName);
                }
            }
        }
        allFileList.addAll(zzAllFileList);
        allFileList.addAll(zzFileList);
        allFileList.addAll(customFileList);
        return allFileList;
    }
}