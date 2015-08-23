package com.youthor.zz.common.m.resource;

import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

import com.youthor.zz.common.ZzApp;
import com.youthor.zz.common.ZzObject;
import com.youthor.zz.common.m.active.TableHolder;
import com.youthor.zz.common.m.db.adapter.DBAdapter;
import com.youthor.zz.common.util.SqlBuilderUtil;
import com.youthor.zz.common.util.StringUtil;
import com.youthor.zz.core.models.Resource;

public abstract class RAbstract {

    private String idFieldName;
    private String mainTable;
    private String resourcePrefix;
    private Resource resource = null;
    
    private Map<String, String> tables = new HashMap<String, String>();
    
    protected RAbstract init(String mainTable, String idFieldName) {
        this.setMainTable(mainTable, idFieldName);
        TableHolder.getInstance().initTableMetaData(this);
        return this;
    }

    protected RAbstract init(String mainTable) {
        return init(mainTable, "id");
    }

    protected RAbstract setMainTable(String mainTable, String idFieldName) {
       String [] mainTableArr = StringUtil.split(mainTable, "/");
       if (mainTableArr.length == 2) {
           this.resourcePrefix = mainTableArr[0];
           this.setMainTable(mainTableArr[1], idFieldName);
       }
       else {
           this.mainTable = mainTableArr[0];
           this.idFieldName = idFieldName;
       }
        return this;
    }
    
    public void save(ZzObject zzObject) {
        String sql = null;
        if(isDirtyData(zzObject)){
            sql = SqlBuilderUtil.buildUpdateSql(this.resourcePrefix, this.mainTable, zzObject);
        }
        else{
            sql = SqlBuilderUtil.buildInsertSql(this.resourcePrefix, this.mainTable, zzObject);
        }
        
        System.out.println("sql->" + sql);
        try{
            getWriteAdapter().getConnection().createStatement().execute(sql);
        }
        catch(Exception e){
            throw new IllegalArgumentException(e.getMessage());
        }
    }
    
    public DBAdapter getDBAdapter(String name) {
        return this.resource.getDBAdapter(this.resourcePrefix + "_" + name);
    }
    
    public DBAdapter getReadAdapter() {
        return this.getDBAdapter("read");
    }

    public DBAdapter getWriteAdapter() {
        return this.getDBAdapter("write");
    }

    public RAbstract beginTransaction()
    {
        this.getWriteAdapter().beginTransaction();
        return this;
    }

    public RAbstract commit()
    {
        this.getWriteAdapter().commit();
        return this;
    }

    public RAbstract rollBack()
    {
        this.getWriteAdapter().rollBack();
        return this;
    }

    public String getTable(String key) {
        if (this.tables.containsKey(key)) {
            return this.tables.get(key);
        }
        return null;
    }

    public String getIdFieldName() {
        return idFieldName;
    }

    public String getMainTable() {
        return mainTable;
    }

    public RAbstract() {
        this.resource = (Resource)ZzApp.getSingletonModel("core/resource");
        this.construct();
    }

    public String getResourcePrefix() {
        return resourcePrefix;
    }

    protected Resource getResource() {
        return resource;
    }

    protected boolean isDirtyData(ZzObject zzObject){
        Object pk = zzObject.getData(idFieldName);
        
        if(pk == null || StringUtil.isBlank(pk.toString())){
            return false;
        }
        
        ZzObject obj = new ZzObject();
        obj.addData("EQS_" + idFieldName, pk);
        String sql = SqlBuilderUtil.buildSelectSql(this.resourcePrefix, this.mainTable, obj);
        try{
            ResultSet rs = getReadAdapter().getConnection().createStatement().executeQuery(sql);
            if(rs.next()){
                return true;
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return false;
    }
    
    abstract protected void construct();
}
