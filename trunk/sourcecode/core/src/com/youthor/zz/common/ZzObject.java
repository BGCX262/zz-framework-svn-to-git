package com.youthor.zz.common;

import java.util.HashMap;
import java.util.Map;


public class ZzObject {
    private Map<String, Object> dataMap = new HashMap<String , Object>();

    public ZzObject addData(String key, Object value) {
        this.dataMap.put(key, value);
        return this;
    }

    public ZzObject setData(Map<String , Object> map) {
        this.dataMap.putAll(map);
        return this;
    }

    public Object getData(String key) {
        return this.dataMap.get(key);
    }

    public boolean hasData(String key) {
        return this.dataMap.containsKey(key);
    }

    public Map<String, Object> getData() {
        return this.dataMap;
    }

    public ZzObject removeData(String key) {
        if (this.dataMap.containsKey(key)) {
            this.dataMap.remove(key);
        }
        return this;
    }
}