package com.youthor.zz.common.m.active;

import java.io.Serializable;

public class TableColumn implements Serializable {
    
    /**
     * 
     */
    private static final long serialVersionUID = -5931157056053310581L;
    private boolean isIdentifier;
    private String columnName;
    private String aliasName;
    private String dataType;
    private String typeName;
    private boolean nullable;
    private Object defaultValue;
    private String referTableName;
    private int precision;
    private int scale;
    private boolean autoIncrement;
    
    public String getColumnName() {
        return columnName;
    }
    
    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }
    
    public String getAliasName() {
        return aliasName;
    }
    
    public void setAliasName(String aliasName) {
        this.aliasName = aliasName;
    }
    
    public String getDataType() {
        return dataType;
    }
    
    public void setDataType(String dataType) {
        this.dataType = dataType;
    }
    
    public String getTypeName() {
        return typeName;
    }
    
    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }
    
    public boolean isNullable() {
        return nullable;
    }
    
    public void setNullable(boolean nullable) {
        this.nullable = nullable;
    }
    
    public Object getDefaultValue() {
        return defaultValue;
    }
    
    public void setDefaultValue(Object defaultValue) {
        this.defaultValue = defaultValue;
    }
    
    public String getReferTableName() {
        return referTableName;
    }
    
    public void setReferTableName(String referTableName) {
        this.referTableName = referTableName;
    }

    @Override
    public String toString() {
        return "TableColumn [aliasName="
                + aliasName
                + ", columnName="
                + columnName
                + ", dataType="
                + dataType
                + ", defaultValue="
                + defaultValue
                + ", nullable="
                + nullable
                + ", referTableName="
                + referTableName
                + ", typeName="
                + typeName
                + ", precision="
                + precision
                + ", scale="
                + scale
                + ", autoIncrement="
                + autoIncrement
                + ", isIdentifier="
                + isIdentifier
                + "]";
    }

    public boolean isIdentifier() {
        return isIdentifier;
    }
    
    public void setIdentifier(boolean isIdentifier) {
        this.isIdentifier = isIdentifier;
    }

    public int getPrecision() {
        return precision;
    }
    
    public void setPrecision(int precision) {
        this.precision = precision;
    }

    public int getScale() {
        return scale;
    }
    
    public void setScale(int scale) {
        this.scale = scale;
    }

    public boolean isAutoIncrement() {
        return autoIncrement;
    }

    public void setAutoIncrement(boolean autoIncrement) {
        this.autoIncrement = autoIncrement;
    }
}
