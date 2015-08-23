package com.youthor.zz.core.models;

import java.util.HashMap;
import java.util.Map;

import com.youthor.zz.common.ZzApp;
import com.youthor.zz.common.m.db.adapter.DBAdapter;
import com.youthor.zz.common.xml.XmlElement;

public class Resource {
	
	@SuppressWarnings("unchecked")
    public DBAdapter getDBAdapter(String name) {
        Map<String, DBAdapter> dbAdapters = (Map<String, DBAdapter>)ZzApp.registry("zz_resource_adapter");
        XmlElement dbConfig = ZzApp.getZzConfig().getResourceConnectionConfig(name);
        String dbConfigParent = dbConfig.getParentTagName();
        if (dbAdapters == null) {
            dbAdapters = new HashMap<String, DBAdapter>();
            ZzApp.register("zz_resource_adapter", dbAdapters);
        }
        else {
            if (dbAdapters.containsKey(name)) {
                return dbAdapters.get(name);
            }
            if (dbAdapters.containsKey(dbConfigParent)) {
                return dbAdapters.get(dbConfigParent);
            }
        }
        
        String type = dbConfig.getSubNodeValue("type");
        DBAdapter dbAdapter = (DBAdapter)ZzApp.getModel(type);
        dbAdapter.init(dbConfig);

        dbAdapters.put(dbConfigParent, dbAdapter);
        return dbAdapter;
    }
}