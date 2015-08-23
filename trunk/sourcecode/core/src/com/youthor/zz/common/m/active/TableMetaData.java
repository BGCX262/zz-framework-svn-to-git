package com.youthor.zz.common.m.active;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 数据库信息
 * 包含主键和字段
 * @author jason
 *
 */
public class TableMetaData {
    //key为数据库表的字段
    private Map<String,TableColumn> tableColumnMap = null;
    //主键为别名
    private List<String> primaryKeys = null;
    private String pkAssignType;
    
    public TableMetaData(){
        this.tableColumnMap = new HashMap<String, TableColumn>();
        primaryKeys = new ArrayList<String>();
        
    }
    
    public void addTableColumn(TableColumn tableColumn){
        if(tableColumn == null){
            return;
        }
        
        String key = tableColumn.getColumnName();
        tableColumnMap.put(key, tableColumn);
    }
    
    public void addPrimaryKey(String pk){
        if(primaryKeys.contains(pk)){
            return;
        }
        primaryKeys.add(pk);
    }
    
    public TableColumn getTableColumn(String columnName){
        return tableColumnMap.get(columnName);
    }
    
    public Collection<TableColumn> getAllColumns(){
        return tableColumnMap.values();
    }
    
    public Collection<String> getAllPrimaryKeys(){
        return primaryKeys;
    }
    
    public boolean isPrimaryKey(String columnName){
        return primaryKeys.contains(columnName);
    }
    
    public String getPkAssignType() {
        return pkAssignType;
    }

    public void setPkAssignType(String pkAssignType) {
        this.pkAssignType = pkAssignType;
    }
}
