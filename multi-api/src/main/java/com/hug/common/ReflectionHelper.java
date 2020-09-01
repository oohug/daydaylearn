package com.hug.common;

import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ReflectionHelper {

    /**
     * 反射调用set方法
     *
     * @param o
     * @param args
     * @param attributeValue
     */
    public static void setField(Object o, String args, Object attributeValue) {
        //判断该属性是否存在
        Field field = ReflectionUtils.findField(o.getClass(), args);
        if (field == null) return;

        // 判断方法setXxx方法是否存在
        String fieldName = "set" + args.substring(0, 1).toUpperCase() + (args.length() > 1 ? args.substring(1) : "");
        Method method = ReflectionUtils.findMethod(o.getClass(), fieldName, attributeValue.getClass());
        if (method == null) return;

        ReflectionUtils.invokeMethod(method, o, attributeValue);
    }

    /**
     * 反射调用set方法
     *
     * @param o
     * @param args
     * @param attributeValue
     */
    public static void setXxx(Object o, String args, Object attributeValue) {
        Class cls = o.getClass();
        //判断该属性是否存在
        Field field = ReflectionUtils.findField(cls, args);
        if (field == null) return;

        // 判断方法setXxx方法是否存在
        String fieldName = "set" + args.substring(0, 1).toUpperCase() + (args.length() > 1 ? args.substring(1) : "");
        try {
            Method method = cls.getMethod(fieldName, attributeValue.getClass());
            method.invoke(o, attributeValue);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}
