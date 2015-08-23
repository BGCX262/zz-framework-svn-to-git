package com.youthor.zz.common.m;

import java.util.HashMap;
import java.util.Map;

import com.youthor.zz.common.ZzObject;
import com.youthor.zz.common.ZzApp;
import com.youthor.zz.common.m.resource.RAbstract;

public abstract class MAbstract extends ZzObject{
    protected String eventPrefix = "coreabstract";
    protected String eventObject = "object";
    protected boolean dataSaveAllowed = true;
    private String resourceSuffix = "Dao";

    private String resourceName = "";
    private String resourceCollectionName = "";
    private boolean isObjectNew = true;

    public  void init(String resourceModel, String resourceSuffix, String collectionModel) {
        this.resourceSuffix = resourceSuffix;
        this.setResourceModel(resourceModel, collectionModel);
    }

    public  void init(String resourceModel) {
        this.init(resourceModel, "Dao", "collection");
    }

    public  void init(String resourceModel, String resourceSuffix) {
        this.init(resourceModel, resourceSuffix, "collection");
    }

    protected void setResourceModel(String resourceModel, String collectionModel) {
        this.resourceName = resourceModel + this.resourceSuffix;
        this.resourceCollectionName = resourceModel + "" + collectionModel + this.resourceSuffix;
    }



    public String getResourceCollectionName() {
        return resourceCollectionName;
    }

    public void setResourceCollectionName(String resourceCollectionName) {
        this.resourceCollectionName = resourceCollectionName;
    }

    public MAbstract load(int id) {
        this.afterLoad();
        return this;
    }
    
    public MAbstract load(String value, String field) {
        this.afterLoad();
        return this;
    }

    protected MAbstract afterLoad() {
        Map<String, Object> dataMap = new HashMap<String , Object>();
        dataMap.put("object", this);
        ZzApp.dispatchEvent("model_load_after", dataMap);

        dataMap = new HashMap<String , Object>();
        dataMap.put(this.eventObject, this);
        ZzApp.dispatchEvent(this.eventPrefix + "load_after", dataMap);
        return this;
    }

    public MAbstract save() {
        RAbstract rAbstract = this.getResource().beginTransaction();
        try {
            this.beforeSave();
            rAbstract.save(this);
            this.afterSave();
            rAbstract.commit();
        }
        catch(Exception ex) {
            rAbstract.rollBack();
        }
        return this;
    }

    protected RAbstract getResource() {
    	return (RAbstract)ZzApp.getResourceSingleton(this.resourceName);
    }
    
    protected MAbstract beforeSave() {
        Map<String, Object> dataMap = new HashMap<String , Object>();
        dataMap.put("object", this);
        ZzApp.dispatchEvent("model_save_before", dataMap);
        dataMap = new HashMap<String , Object>();
        dataMap.put(this.eventObject, this);
        ZzApp.dispatchEvent(this.eventPrefix + "save_before", dataMap);
        return this;
    }

    protected MAbstract afterSave() {
        Map<String, Object> dataMap = new HashMap<String , Object>();
        dataMap.put("object", this);
        ZzApp.dispatchEvent("model_save_after", dataMap);
        dataMap = new HashMap<String , Object>();
        dataMap.put(this.eventObject, this);
        ZzApp.dispatchEvent(this.eventPrefix + "save_after", dataMap);
        return this;
    }

    public MAbstract delete() {
        return this;
    }

    protected MAbstract beforeDelete()
    {
        Map<String, Object> dataMap = new HashMap<String , Object>();
        dataMap.put("object", this);
        ZzApp.dispatchEvent("model_delete_before", dataMap);
        dataMap = new HashMap<String , Object>();
        dataMap.put(this.eventObject, this);
        ZzApp.dispatchEvent(this.eventPrefix + "delete_before", dataMap);
        return this;
    }

    protected MAbstract afterDelete() {
        Map<String, Object> dataMap = new HashMap<String , Object>();
        dataMap.put("object", this);
        ZzApp.dispatchEvent("model_delete_after", dataMap);
        dataMap = new HashMap<String , Object>();
        dataMap.put(this.eventObject, this);
        ZzApp.dispatchEvent(this.eventPrefix + "deleteafter", dataMap);
        return this;
    }

    public String getResourceName() {
        return resourceName;
    }

    public void setResourceName(String resourceName) {
        this.resourceName = resourceName;
    }
}
