package com.youthor.zz.common.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ObjectUtil {

    public static Object invokeMethod(final Object object, final String methodName, final Class<?>[] parameterTypes,
            final Object[] parameters) {
        Method method = getDeclaredMethod(object, methodName, parameterTypes);
        if (method == null) {
            throw new IllegalArgumentException("Could not find method [" + methodName + "] on target [" + object + "]");
        }
        method.setAccessible(true);
        try {
        
            return method.invoke(object, parameters);
        } 
        catch (Exception e) {
            throw convertReflectionExceptionToUnchecked(e);
        }
    }

    protected static Method getDeclaredMethod(Object object, String methodName, Class<?>[] parameterTypes) {
        for (Class<?> superClass = object.getClass(); superClass != Object.class; superClass = superClass.getSuperclass()) {
            try {
                return superClass.getDeclaredMethod(methodName, parameterTypes);
            } 
            catch (NoSuchMethodException e) {
                //NOSONAR, Method不在当前类定义,继续向上转型
            }
        }
        return null;
    }

    public static boolean isMethodExist(Object object, String methodName, Class<?>[] parameterTypes){
        Method method = getDeclaredMethod(object,methodName,parameterTypes);
        if(method == null){
            return false;
        }
        else{
            return true;
        }
    }
    
    public static RuntimeException convertReflectionExceptionToUnchecked(Exception e) {
        if (e instanceof IllegalAccessException || e instanceof IllegalArgumentException || e instanceof NoSuchMethodException) {
            return new IllegalArgumentException("Reflection Exception.", e);
        } 
        else if (e instanceof InvocationTargetException) {
            return new RuntimeException("Reflection Exception.", ((InvocationTargetException) e).getTargetException());
        } 
        else if (e instanceof RuntimeException) {
            return (RuntimeException) e;
        }
        return new RuntimeException("Unexpected Checked Exception.", e);
    }

    public static Object getObject(String targetClassName) {
        Class<?> targetClass = null;
        Object targetObject = null;
        try {
            targetClass = Class.forName(targetClassName);
            targetObject = targetClass.newInstance();
        } 
        catch (Exception e) {
            throw convertReflectionExceptionToUnchecked(e);
        }
        return targetObject;
    }
}
