package com.youthor.zz.common;

public class ZzConstant {
    public static final String zzSessionFlag                    =    "ZZ_SESSION_DATA";
    
    //系统空字符串的值
    public static final String NULL_STRING_VALUE                =    "";
    
    //@符号
    public static final String AT_SIGN_SPLIT_NAME               =    "@";
    //common
    public static final String COMMA_SIGN_SPLIT_NAME            =    ",";
    //DOT
    public static final String DOT_SIGN_SPLIT_NAME              =    ".";
    //COLON
    public static final String COLON_SIGN_SPLIT_NAME            =    ";";
    //_
    public static final String UNDERLINE_SIGN_SPLIT_NAME        =    "_";
    
    //module etc配置路径在系统应用中的文件夹名
    public static final String ETC_DIR_NAME_IN_APP              =    "modules";
    
    //module配置文件中系统的文件名前缀
    public static final String MODULE_ALL_XML_NAME_PREFIX       =    "Zz_All";
    
    //module配置文件中ZZ应用的文件名前缀
    public static final String MODULE_ZZ_XML_NAME_PREFIX        =    "Zz_";
    
    //xml节点分割符号
    public static final String XML_NODE_SPLIT_SIGN              =    "/";
    
    //类名节点分割符号
    public static final String CLASS_NAME_SPLIT_SIGN            =    "_";
    
    //Controller节点名称
    public static final String CONTROLLERS_NODE_NAMESPACE       =    "controllers";
    
   //Models节点名称
    public static final String MODELS_NODE_NAMESPACE            =    "models";
    
   //Blocks节点名称
    public static final String BLOCKS_NODE_NAMESPACE            =    "blocks";
    
  //helpers节点名称
    public static final String HELPERS_NODE_NAMESPACE           =    "helpers";
    
    //配置文件后缀
    public static final String MODULE_CONFIG_FILE_NAME_SUFFIX   =    "xml";
    
    //配置文件后缀
    public static final String MODULE_CONFIG_ETC_DIR_NAME       =    "etc";
    
    //module 合并后的配置文件cache名
    public static final String MODULE_MERGED_CACHE_NAME         =    "config_cache.xml";
    
    //模块依赖节点名称
    public static final String MODULE_CONFIG_DEPENDS_NAME       =    "depends";
    
    //模块包所在路径节点名称
    public static final String MODULE_CONFIG_PACKAGE_NAME       =    "package";
    
    //模块活动节点名称
    public static final String MODULE_CONFIG_ACTIVE_NAME        =    "active";
    
    //模块配置的父级节点全名称
    public static final String MODULE_ALL_XML_NAMESPACE         =    "/config/modules";
    
    //模块配置的父级节点全名称
    public static final String CONFIG_ALL_XML_NAMESPACE         =    "/config";
    
    public static final String LAYOUT_ALL_XML_NAMESPACE         =    "/layout";
    
    //local配置
    public static final String ETC_LOCAL                        =    "local.xml";
    
    //DBAdapter配置节点参数名称
    public static final String DB_CONFIG_NODE_TYPE_NS 			 =   "type";
    public static final String DB_CONFIG_NODE_URL_NS			 =   "url";
    public static final String DB_CONFIG_NODE_CONN_TYPE_NS	 	 =   "conn_type";
    public static final String DB_CONFIG_NODE_USER_NAME_NS 		 =   "username";
    public static final String DB_CONFIG_NODE_PASSWORD_NS 		 =   "password";
    public static final String DB_CONFIG_NODE_JNDI_NAME_NS 		 =   "jndi_name";
    public static final String DB_CONFIG_NODE_MX_POOL_SIZE_NS 	 =   "max_pool_size";
    public static final String DB_CONFIG_NODE_MI_POOL_SIZE_NS	 =   "min_pool_size";
    public static final String DB_CONFIG_NODE_MX_IDLE_NS 		 =   "max_idle_time";
    public static final String DB_CONFIG_NODE_INIT_POOL_SIZE_NS	 =   "initial_pool_size";
    public static final String DB_CONFIG_NODE_ACQ_INCRE_NS 	 	 =   "acquire_increment";
    public static final String DB_CONFIG_NODE_ACQ_RETRY_INCRE_NS =   "acquire_retry_attempts";
}
