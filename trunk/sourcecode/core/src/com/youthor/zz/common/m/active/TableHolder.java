package com.youthor.zz.common.m.active;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;

import com.youthor.zz.common.ZzConstant;
import com.youthor.zz.common.ZzObject;
import com.youthor.zz.common.m.db.adapter.DBAdapter;
import com.youthor.zz.common.m.resource.RAbstract;


public class TableHolder {
    private static TableHolder instance = null;
    
    private ZzObject tableMetaData = null;
    
    private TableHolder(){
        this.tableMetaData = new ZzObject();
    }
    
    public TableMetaData getTableMetaData(String tableName){
        return (TableMetaData)tableMetaData.getData(tableName);
    }
    
    public void initTableMetaData(RAbstract r){
        String mainTable = r.getMainTable();
        String resourcePrefix = r.getResourcePrefix();
        
        String key = resourcePrefix + ZzConstant.XML_NODE_SPLIT_SIGN + mainTable;
        if(tableMetaData.getData(key) != null){
            tableMetaData.removeData(key);
        }
        
        TableMetaData tmd = new TableMetaData();
        tableMetaData.addData(key, tmd);
        try{
            String sql = "select * from " + mainTable;
            
            DBAdapter dbAdpater = r.getReadAdapter();
            Connection conn = dbAdpater.getConnection();
            Statement stm = conn.createStatement();
            
            //pk
            DatabaseMetaData dbmd = conn.getMetaData();
            ResultSet pkrs = dbmd.getPrimaryKeys(null, null, mainTable);
            while(pkrs.next()){
                tmd.addPrimaryKey(pkrs.getString(4));
            }
            
            //column
            ResultSet rs = stm.executeQuery(sql);
            ResultSetMetaData rsmd = rs.getMetaData();
            int columnCount = rsmd.getColumnCount();
            for(int i=1; i <= columnCount; i++){
                TableColumn tc = new TableColumn();
                
                String columnName = rsmd.getColumnName(i);
                String aliasName = rsmd.getColumnName(i);
                String dataType = "" + rsmd.getColumnType(i);
                String typeName = rsmd.getColumnTypeName(i);
                boolean nullable = rsmd.isNullable(i) == 1 ? true : false;
                tc.setColumnName(columnName);
                tc.setAliasName(aliasName);
                tc.setDataType(dataType);
                tc.setTypeName(typeName);
                tc.setNullable(nullable);
                tc.setPrecision(rsmd.getPrecision(i));
                tc.setScale(rsmd.getScale(i));
                tc.setAutoIncrement(rsmd.isAutoIncrement(i));
                if(tmd.isPrimaryKey(columnName)){
                    tc.setIdentifier(true);
                }
                
                tmd.addTableColumn(tc);
            }
        }
        catch(Exception e){
            throw new IllegalArgumentException(e.getMessage());
        }
    }
    
    public void reInit(RAbstract r){
        tableMetaData.getData().clear();
    }
    
    public synchronized static TableHolder getInstance(){
        if(instance == null){
            instance = new TableHolder();
        }
        return instance;
    }
}