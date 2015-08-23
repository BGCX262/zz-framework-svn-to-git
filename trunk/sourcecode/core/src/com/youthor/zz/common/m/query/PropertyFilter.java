package com.youthor.zz.common.m.query;

import java.util.Date;

import org.apache.commons.lang.StringUtils;

import com.youthor.zz.common.util.AssertUtil;
import com.youthor.zz.common.util.ReflectionUtil;


public class PropertyFilter {
    /** 多个属性间OR关系的分隔符. */
    public static final String OR_SEPARATOR = "_OR_";

    /** 属性比较类型. 目前IN 只支持字符串的类型 */
    public enum MatchType {
        EQ, LIKE, LT, GT, LE, GE, ISNULL, ISNOTNULL, IN;
    }

    /** 属性数据类型. */
    public enum PropertyType {
        S(String.class), I(Integer.class), L(Long.class), N(Double.class), D(Date.class), B(Boolean.class);
        
        private Class<?> clazz;

        PropertyType(Class<?> clazz) {
            this.clazz = clazz;
        }

        public Class<?> getValue() {
            return clazz;
        }
    }

    private String[] propertyNames = null;
    private Class<?> propertyType = null;
    private Object propertyValue = null;
    private MatchType matchType = null;

    public PropertyFilter() {
        
    }

    public PropertyFilter(MatchType matchType, PropertyType propertyType,String fieldName, String value){
        this((matchType.name() + propertyType.name() + "_" + fieldName), value);
    }
    
    /**
     * @param filterName 比较属性字符串,含待比较的比较类型、属性值类型及属性列表. 
     *                   eg. LIKES_NAME_OR_LOGIN_NAME
     * @param value 待比较的值.
     */
    public PropertyFilter(final String filterName, final String value) {

        String matchTypeStr = StringUtils.substringBefore(filterName, "_");
        String matchTypeCode = StringUtils.substring(matchTypeStr, 0, matchTypeStr.length() - 1);
        String propertyTypeCode = StringUtils.substring(matchTypeStr, matchTypeStr.length() - 1, matchTypeStr.length());
        
        try {
            matchType = Enum.valueOf(MatchType.class, matchTypeCode);
        } 
        catch (RuntimeException e) {
            throw new IllegalArgumentException("filter type of " + filterName + " is illegal", e);
        }
        try {
            propertyType = Enum.valueOf(PropertyType.class, propertyTypeCode).getValue();
        } 
        catch (RuntimeException e) {
            throw new IllegalArgumentException("filter property type of " + filterName + " is illegal", e);
        }

        String propertyNameStr = StringUtils.substringAfter(filterName, "_");
        propertyNames = StringUtils.splitByWholeSeparator(propertyNameStr, PropertyFilter.OR_SEPARATOR);

        AssertUtil.isTrue(propertyNames.length > 0, "filter name of " + filterName + "is illegal");
        //按entity property中的类型将字符串转化为实际类型.
        this.propertyValue = ReflectionUtil.convertStringToObject(value, propertyType);
    }

    /**
     * 是否比较多个属性.
     */
    public boolean isMultiProperty() {
        return (propertyNames.length > 1);
    }

    /**
     * 获取比较属性名称列表.
     */
    public String[] getPropertyNames() {
        return propertyNames;
    }

    /**
     * 获取唯一的比较属性名称.
     */
    public String getPropertyName() {
        if (propertyNames.length > 1) {
            throw new IllegalArgumentException("There are not only one property");
        }
        return propertyNames[0];
    }

    /**
     * 获取比较值.
     */
    public Object getPropertyValue() {
        return propertyValue;
    }

    /**
     * 获取比较值的类型.
     */
    public Class<?> getPropertyType() {
        return propertyType;
    }

    /**
     * 获取比较方式类型.
     */
    public MatchType getMatchType() {
        return matchType;
    }
}
