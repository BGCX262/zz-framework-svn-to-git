package com.youthor.zz.common;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.youthor.zz.common.c.FrontController;
import com.youthor.zz.common.util.ObjectUtil;
import com.youthor.zz.common.util.StringUtil;
import com.youthor.zz.common.xml.XmlElement;
import com.youthor.zz.core.models.Design;
import com.youthor.zz.core.models.Layout;
import com.youthor.zz.core.models.layout.Update;
import com.youthor.zz.core.models.template.TApp;

public class ZzApp {
    private static final String CONTROLLER_REGISTRY_PREFIX = "_zz_controllers_";
    private static final String MODELS_REGISTRY_PREFIX = "_zz_models_";
    
    
    private static String appPath = null;
    private static String appendPath = null;
    private static String suffix = null;
    private static Map<String, Object> registry = new HashMap<String, Object>();
    private static FrontController frontController = null;
    private static ZzConfig zzConfig = null;
    
    public static ZzConfig getZzConfig() {
        return ZzApp.zzConfig;
    }
    
    public static void clear() {
        ZzApp.appPath = null;
        ZzApp.appendPath = null;
        ZzApp.suffix = null;
        if (ZzApp.zzConfig !=null ) {
            ZzApp.zzConfig.clear();
        }
        ZzApp.frontController = null;
        ZzApp.registry.clear();
    }

    public static void init() {
        if (zzConfig == null) {
            zzConfig = new ZzConfig();
            try {
                ZzBenchmark.start("Application Init");
                zzConfig.init();
                ZzBenchmark.stop("Application Init");
                
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
    public static FrontController getFrontController() {
        if (ZzApp.frontController == null) {
            ZzApp.frontController = new FrontController();
            ZzApp.frontController.init();
        }
        return ZzApp.frontController;
    }

    public static void register(String key, Object value) {
        registry.put(key, value);
    }
    
    public static void unregister(String key) {
        if (registry.containsKey(key)) {
              registry.remove(key);
        }
    }

    public static Object registry(String key) {
        if (registry.containsKey(key)) {
             return registry.get(key);
        }
        return null;
    }

    public static Object helper(String type){
        return ZzApp.helper(type, null);
    }

    public static Object helper(String type, Map<String, Object> datas) {
        return ZzApp.createObject(type, ZzConstant.HELPERS_NODE_NAMESPACE, datas);
    }

    public static Object getBlock(String type) {
        return ZzApp.getBlock(type, null);
    }

    public static Object getBlock(String type, Map<String, Object> datas) {
        return ZzApp.createObject(type, ZzConstant.BLOCKS_NODE_NAMESPACE, datas);
    }

    public static Object getModel(String type, Map<String, Object> datas) {
        return ZzApp.createObject(type, ZzConstant.MODELS_NODE_NAMESPACE, datas);
    }

    private static Object createObject(String type, String flag ,Map<String, Object> datas) {
        String [] types = StringUtil.split(type, "/");
        String objectName = ZzApp.getFullClassName(types[0], types[1], flag);
        try {
            Object object = ObjectUtil.getObject(objectName);
            if (datas != null && !datas.isEmpty()) {
                Class<?> [] classes = new Class[1];
                classes[0] = Map.class;
                boolean hasMehtod = ObjectUtil.isMethodExist(object, "setData", classes);
                if (hasMehtod) {
                    Object [] data = new Object[1];
                    data[0] = datas;
                    ObjectUtil.invokeMethod(object, "setData", classes, data);
                }
            }
            return object;
        }
        catch (RuntimeException e) {
            return null;
        }
    }

    public static Layout getLayout() {
        Design design = ZzApp.getDesign();
        String layoutFlag = "layout_" + design.getArea() + "_" + design.getPackage() + "_" + design.getTheme();
        Layout layout = (Layout)ZzApp.registry(layoutFlag);
        if (layout == null) {
            layout = (Layout)ZzApp.getModel("core/layout");
            ZzApp.register(layoutFlag, layout);
            layout.loadLayoutXml();
        }
        return layout;
    }

    public static Update getUpdate() {
        ZzContext zzContext = ZzContext.getContext();
        Update update = (Update)zzContext.getObject("zz_update");
        if (update == null) {
            update = (Update)ZzApp.getModel("core/layout_update");
            zzContext.addObject("zz_update", update);
        }
        return update;
    }

    public static Design getDesign() {
        ZzContext zzContext = ZzContext.getContext();
        Design design = (Design)zzContext.getObject("zz_design");
        if (design == null) {
            design = (Design)ZzApp.getModel("core/design");
            zzContext.addObject("zz_design", design);
        }
        return design;
    }

    


    public static Object getModel(String type) {
        return ZzApp.getModel(type,null);
    }

    public static Object getSingletonModel(String type) {
        String key = ZzApp.MODELS_REGISTRY_PREFIX + StringUtil.replaceAll(type, "/", "_");
        System.out.println("key->" + key +",type->" + type);
        Object model = ZzApp.registry(key);
        if (model == null ) {
            model = ZzApp.getModel(type,null);
            ZzApp.register(key, model);
        }
        return model;
    }

    public static Object getSingletonModel(String type, String extKey) {
        String key = ZzApp.MODELS_REGISTRY_PREFIX + StringUtil.replaceAll(type, "/", "_") + "_" + extKey;
        Object model = ZzApp.registry(key);
        if (model == null ) {
            model = ZzApp.getModel(type,null);
            ZzApp.register(key, model);
        }
        return model;
    }

    public static TApp getTemplateApp(String type) {
        
        String templateFlag = "zz_template_app";
        TApp  tApp = (TApp)ZzApp.registry(templateFlag);
        if (tApp == null) {
            tApp = (TApp)ZzApp.getModel(type);
            ZzApp.register(templateFlag, tApp);
        }
        return tApp;
    }
    
   

    public static void run() {
        ZzApp.getFrontController().dispatch();
    }

    public static String getAppPath() {
        return appPath;
    }

    public static void setAppPath(String appPath) {
        ZzApp.appPath = appPath;
    }

    public static void dispatchEvent(String eventName, Map<String, Object> eventData) {
        String area = (String)ZzContext.getContext().getObject("zz_area");
        if (StringUtil.isBlank(area)) area = "global";
        XmlElement xmlElement = ZzApp.getZzConfig().getNode(area+"/events/"+eventName +"/observers");
        if (xmlElement != null) {
            List<XmlElement> list = xmlElement.getChildren();
            for(XmlElement eventXmlElement :list) {
                String cls = StringUtil.doWithNull(eventXmlElement.getSubNodeValue("class"));
                String type = StringUtil.doWithNull(eventXmlElement.getSubNodeValue("type")).toLowerCase();
                String method = StringUtil.doWithNull(eventXmlElement.getSubNodeValue("method"));
                Object  object = null;
                if (type.equals("disabled"))  {
                    continue;
                }
                else if (type.equals("object") || type.equals("model")) {
                    object = createEventObject(cls);
                }
                else if (type.equals("thread")){
                    object = ZzContext.getContext().getObject(cls);
                    if (object == null) {
                        object = createEventObject(cls);
                        ZzContext.getContext().addObject(cls, object);
                    }
                }
                else {
                    object = ZzApp.registry(cls);
                    if (object == null) {
                        object = createEventObject(cls);
                        ZzApp.register(cls, object);
                    }
                }
                Class<?> [] classes = new Class[1];
                classes[0] = Map.class;
                boolean hasMehtod = ObjectUtil.isMethodExist(object, method, classes);
                if (hasMehtod) {
                    Object [] data = new Object[1];
                    data[0] = eventData;
                    ObjectUtil.invokeMethod(object, method, classes, data);
                }
            }
        }
    }

    private static Object createEventObject(String cls) {
        Object object = null;
        if (cls.indexOf("/") > 0) {
            object = ZzApp.getModel(cls);
        }
        else {
            object = ObjectUtil.getObject(cls);
        }
        return object;
    }

    public static Object createUserController(String frontName, String controllerName) {
        String cacheFlagName = CONTROLLER_REGISTRY_PREFIX + frontName + ZzConstant.UNDERLINE_SIGN_SPLIT_NAME + controllerName;
        Object cacheObject = ZzApp.registry(cacheFlagName);
        if(cacheObject != null){
            return cacheObject; 
        }
        String objectName = ZzApp.getFullClassName(frontName, controllerName, ZzConstant.CONTROLLERS_NODE_NAMESPACE);
        try {
            cacheObject = ObjectUtil.getObject(objectName);
        }
        catch (RuntimeException e) {
            return null;
        }
        
        ZzApp.register(cacheFlagName, cacheObject);
        
        return cacheObject;
    }

    private static String getFullClassName (String moduleName,  String classType,  String packageType) {
        StringBuilder sbObjectName = new StringBuilder("global");
        sbObjectName.append(ZzConstant.XML_NODE_SPLIT_SIGN);
        sbObjectName.append(packageType);
        sbObjectName.append(ZzConstant.XML_NODE_SPLIT_SIGN);
        sbObjectName.append(moduleName);
        sbObjectName.append(ZzConstant.XML_NODE_SPLIT_SIGN);
        sbObjectName.append(classType);
        
        String objectName = zzConfig.getConfigValue(sbObjectName.toString());
        if(StringUtil.isBlank(objectName)) {
            String [] classSplitName = StringUtil.split(classType, ZzConstant.CLASS_NAME_SPLIT_SIGN);
            int len = classSplitName.length;
            String packName = zzConfig.getConfigValue(
                                          ZzConstant.ETC_DIR_NAME_IN_APP + ZzConstant.XML_NODE_SPLIT_SIGN + moduleName +
                                          ZzConstant.XML_NODE_SPLIT_SIGN + ZzConstant.MODULE_CONFIG_PACKAGE_NAME
                                          ) + ZzConstant.DOT_SIGN_SPLIT_NAME + packageType ;
            String className = StringUtil.changeFirstToUpper(classSplitName[len - 1]);
            if (packageType.equals(ZzConstant.CONTROLLERS_NODE_NAMESPACE)) {
                className = className + "Controller";
            }
            classSplitName[len - 1] = null;
            String classPackageName = StringUtil.arrToString(classSplitName, ZzConstant.DOT_SIGN_SPLIT_NAME);
            if (!StringUtil.isBlank(classPackageName)) {
                classPackageName = ZzConstant.DOT_SIGN_SPLIT_NAME + classPackageName;
            }
            objectName = packName + classPackageName + ZzConstant.DOT_SIGN_SPLIT_NAME + className;
        }
        return objectName;
    }

    public static String getAppendPath() {
        return appendPath;
    }

    public static void setAppendPath(String appendPath) {
        ZzApp.appendPath = appendPath;
    }

    public static String getSuffix() {
        return suffix;
    }

    public static void setSuffix(String suffix) {
        ZzApp.suffix = suffix;
    }

    public static Object getResourceSingleton(String resourceName) {
        String [] resourceNames = StringUtil.split(resourceName, "/");
        String moduleName = resourceNames[0];
        String type = resourceNames[1];
        XmlElement xmlElement = ZzApp.getZzConfig().getNode("global/models/" + resourceNames[0] + "/resourceModel");
        if (xmlElement != null) {
            type = xmlElement.getValue() + "_" + type;
        }
        return ZzApp.getSingletonModel(moduleName+"/"+type);
    }
}