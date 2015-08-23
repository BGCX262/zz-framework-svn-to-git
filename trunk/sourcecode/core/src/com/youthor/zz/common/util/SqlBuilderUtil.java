package com.youthor.zz.common.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;


import org.apache.commons.lang.StringUtils;

import com.youthor.zz.common.ZzConstant;
import com.youthor.zz.common.ZzObject;
import com.youthor.zz.common.m.active.TableColumn;
import com.youthor.zz.common.m.active.TableHolder;
import com.youthor.zz.common.m.active.TableMetaData;
import com.youthor.zz.common.m.query.PropertyFilter;
import com.youthor.zz.common.m.query.PropertyFilter.MatchType;


public class SqlBuilderUtil {
    
    public static String buildInsertSql(String resourcePrefix, String mainTable, ZzObject obj){
        if(StringUtil.isBlank(mainTable) || obj == null){
            throw new IllegalArgumentException("the mainTable and zzObject should not be null");
        }
        StringBuilder sql = new StringBuilder();
        
        if(resourcePrefix == null){
            resourcePrefix = "";
        }
        String key = resourcePrefix + ZzConstant.XML_NODE_SPLIT_SIGN + mainTable;
        TableHolder tableHolder = TableHolder.getInstance();
        TableMetaData tmd = tableHolder.getTableMetaData(key);
        if(tmd == null){
            return "";
        }
        
        sql.append("insert into " + mainTable);
        StringBuilder sqlColumnTitles = new StringBuilder();
        StringBuilder sqlColumnValues = new StringBuilder();
        Collection<TableColumn> tableColumns = tmd.getAllColumns();
        boolean isFirst = true;
        for(TableColumn tableColumn : tableColumns){
            if(tableColumn.isAutoIncrement()){
                continue;
            }
            
            if(!isFirst){
                sqlColumnTitles.append(",");
                sqlColumnValues.append(",");
            }
            else{
                isFirst = false;
            }
            
            //处理主键，主要用于seq或者uuid等
            if(StringUtil.isNotBlank(tmd.getPkAssignType())){
                //TODO
            }
            
            sqlColumnTitles.append(tableColumn.getColumnName());
            String aliasName = tableColumn.getAliasName();
            Object objValue = obj.getData(aliasName);
            String value = dataTypeFormat(objValue, tableColumn.getDataType());
            sqlColumnValues.append(value);
        }
        
        sql.append(" (" + sqlColumnTitles.toString() +") ");
        sql.append(" VALUES(" + sqlColumnValues.toString() +") ");
        
        System.out.println("buildInsertSql->" + sql);
        return sql.toString();
    }
    
    //等待处理
    public static String buildUpdateSql(String resourcePrefix, String mainTable, ZzObject obj){
        if(StringUtil.isBlank(mainTable) || obj == null){
            throw new IllegalArgumentException("the mainTable and zzObject should not be null");
        }
        
        StringBuilder sql = new StringBuilder();
        if(resourcePrefix == null){
            resourcePrefix = "";
        }
        String key = resourcePrefix + ZzConstant.XML_NODE_SPLIT_SIGN + mainTable;
        TableHolder tableHolder = TableHolder.getInstance();
        TableMetaData tmd = tableHolder.getTableMetaData(key);
        if(tmd == null){
            return "";
        }
        
        Collection<String> primaryKeys = tmd.getAllPrimaryKeys();
        if(primaryKeys == null || primaryKeys.size() == 0){
            System.out.println("the pk size is zero!");
            return null;
        }
        StringBuilder sqlWhere = new StringBuilder();
        boolean isFirst = true;
        for(String aliasPkName : primaryKeys){
            TableColumn tableColumn = getColumnInfoByAliasName(resourcePrefix,mainTable,aliasPkName);
            if(!isFirst){
                sqlWhere.append(",");
            }
            else{
                isFirst = false;
            }
            
            String value = dataTypeFormat(obj.getData(aliasPkName), tableColumn.getDataType());
            sqlWhere.append(" and " + tableColumn.getColumnName() + " = " + value);
        }
        
        sql.append("update " + mainTable + " set ");
        Map<String, Object> values = obj.getData();
        Set<String> aliasNames = values.keySet();
        isFirst = true;
        for(String aliasName : aliasNames){
            TableColumn tableColumn = getColumnInfoByAliasName(resourcePrefix,mainTable,aliasName);
            if(tableColumn == null || tableColumn.isAutoIncrement() || tableColumn.isIdentifier()){
                continue;
            }
            if(!isFirst){
                sql.append(",");
            }
            else{
                isFirst = false;
            }
            
            String value = dataTypeFormat(obj.getData(aliasName), tableColumn.getDataType());
            sql.append(" " + tableColumn.getColumnName() + " = " + value);
        }
        sql.append(" where 1=1 ");
        sql.append(sqlWhere);
        
        System.out.println("buildUpdateSql->" + sql);
        return sql.toString();
    }
    
    public static String buildDeleteSql(String resourcePrefix, String mainTable, ZzObject obj){
        return null;
    }
    
    public static String buildSelectSql(String resourcePrefix, String mainTable, ZzObject obj){
        if(StringUtil.isBlank(mainTable) || obj == null){
            throw new IllegalArgumentException("the mainTable and zzObject should not be null");
        }
        
        StringBuilder sql = new StringBuilder();
        sql.append("select * from " + mainTable + " where 1=1 ");
        
        List<PropertyFilter> filters = buildPropertyFilters(obj);
        if(filters != null && filters.size() > 0){
            for(PropertyFilter filter : filters){
                String aliasName = filter.getPropertyName();
                TableColumn tableColumn = SqlBuilderUtil.getColumnInfoByAliasName(resourcePrefix, mainTable, aliasName);
                if(tableColumn != null){
                    sql.append(buildPropertySql(tableColumn, filter.getPropertyValue(), filter.getMatchType(), ""));
                }
            }
        }
        
        System.out.println("buildSelectSql->" + sql);
        return sql.toString();
    }
    
    /**
     * 根据按PropertyFilter命名规则的Request参数,创建PropertyFilter列表.
     * 默认Filter属性名前缀为filter_.
     * 
     * @see #buildPropertyFilters(HttpServletRequest, String)
     */
    public static List<PropertyFilter> buildPropertyFilters(final ZzObject zzObject) {
        return buildPropertyFilters(zzObject, "filter_");
    }

    /**
     * 根据按PropertyFilter命名规则的Request参数,创建PropertyFilter列表.
     * PropertyFilter命名规则为Filter属性前缀_比较类型属性类型_属性名.
     * 
     * eg.
     * filter_EQS_name
     * filter_LIKES_name_OR_email
     */
    public static List<PropertyFilter> buildPropertyFilters(final ZzObject zzObject, final String filterPrefix) {
        List<PropertyFilter> filterList = new ArrayList<PropertyFilter>();

        //从request中获取含属性前缀名的参数,构造去除前缀名后的参数Map.
        Map<String, Object> filterParamMap = zzObject.getData();

        //分析参数Map,构造PropertyFilter列表
        for (Map.Entry<String, Object> entry : filterParamMap.entrySet()) {
            String filterName = entry.getKey();
            Object valueObj = entry.getValue();
            String value = "";
            if(valueObj != null){
                value = valueObj.toString();
            }
            //如果value值为空,则忽略此filter.
            if (StringUtils.isNotBlank(value)) {
                PropertyFilter filter = new PropertyFilter(filterName, value);
                filterList.add(filter);
            }
        }
        return filterList;
    }
    /**
     * 按属性条件参数创建条件语句,辅助函数.
     */
    protected static String buildPropertySql(TableColumn tableColumn, Object propertyValue, MatchType matchType, String prefix) {
        StringBuilder sb = new StringBuilder();
        if(prefix == null){
            prefix = "";
        }
        
        //根据MatchType构造criterion
        if (MatchType.EQ.equals(matchType)) {
            String value = dataTypeFormat(propertyValue, tableColumn.getDataType());
            sb.append(" and " + prefix + tableColumn.getColumnName());
            sb.append(" = " + value);
        } 
        else if (MatchType.LIKE.equals(matchType)) {
            String value = dataTypeFormat(propertyValue, tableColumn.getDataType());
            sb.append(" and " + prefix + tableColumn.getColumnName());
            sb.append(" like " + value);
        } 
        else if (MatchType.LE.equals(matchType)) {
            String value = dataTypeFormat(propertyValue, tableColumn.getDataType());
            sb.append(" and " + prefix + tableColumn.getColumnName());
            sb.append(" <= " + value);
        } 
        else if (MatchType.LT.equals(matchType)) {
            String value = dataTypeFormat(propertyValue, tableColumn.getDataType());
            sb.append(" and " + prefix + tableColumn.getColumnName());
            sb.append(" < " + value);
        } 
        else if (MatchType.GE.equals(matchType)) {
            String value = dataTypeFormat(propertyValue, tableColumn.getDataType());
            sb.append(" and " + prefix + tableColumn.getColumnName());
            sb.append(" >= " + value);
        } 
        else if (MatchType.GT.equals(matchType)) {
            String value = dataTypeFormat(propertyValue, tableColumn.getDataType());
            sb.append(" and " + prefix + tableColumn.getColumnName());
            sb.append(" > " + value);
        }
        else if(MatchType.IN.equals(matchType)){
            String str = String.valueOf(propertyValue);
            if(StringUtils.isBlank(str)){
                str = "";
            }
            sb.append(" and " + prefix + tableColumn.getColumnName() + "in (");
            String [] strArray = str.split(ZzConstant.COMMA_SIGN_SPLIT_NAME);
            boolean isFirst = true;
            for(String val : strArray){
                if(!isFirst){
                    sb.append(",");
                    isFirst = false;
                }
                else{
                    isFirst = true;
                }
                String value = dataTypeFormat(val, tableColumn.getDataType());
                sb.append(value);
            }
            sb.append(") ");
        }
        else if(MatchType.ISNOTNULL.equals(matchType)){
            sb.append(" and " + prefix + tableColumn.getColumnName() + " is not null");
        }
        else if(MatchType.ISNULL.equals(matchType)){
            sb.append(" and " + prefix + tableColumn.getColumnName() + " is null");
        }
        return sb.toString();
    }
    
    protected static TableColumn getColumnInfoByAliasName(String resourcePrefix, String mainTable, String aliasName){
        if(StringUtil.isBlank(aliasName) || StringUtil.isBlank(mainTable)){
            return null;
        }
        
        if(resourcePrefix == null){
            resourcePrefix = "";
        }
        String key = resourcePrefix + ZzConstant.XML_NODE_SPLIT_SIGN + mainTable;
        TableHolder tableHolder = TableHolder.getInstance();
        TableMetaData tmd = tableHolder.getTableMetaData(key);
        if(tmd == null){
            return null;
        }
        
        Collection<TableColumn> coll = tmd.getAllColumns();
        for(TableColumn tableColumn : coll){
            if(aliasName.equalsIgnoreCase(tableColumn.getAliasName())){
                return tableColumn;
            }
        }
        
        return null;
    }
    
    //TODO
    protected static String dataTypeFormat(Object src, String type){
        if(src == null){
            return "''";
        }
      
        return "'" + src.toString() +"'";
    }
}
