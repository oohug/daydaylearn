/**
 * AnnotationReflexConverter.java
 */
package com.hug.converter;

import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @version 1.0
 * @description 注解反射传换者
 */
public class AnnotationReflexConverter implements Converter {

    /*
     * (non-Javadoc)
     *
     */
    @Override
    public <R extends Serializable> R converter(Object t, Class<R> clzR) throws Exception {
        if (t == null || clzR == null)
            throw new NullPointerException();
        R r = clzR.newInstance();
        Field[] fs = clzR.getDeclaredFields();
        for (Field f : fs) {
            Conversion a = f.getAnnotation(Conversion.class);
            if (a != null) {
                String t_f_name = a.value();
                if (StringUtils.isEmpty(t_f_name)) {
                    t_f_name = f.getName();
                }
                Field tf = t.getClass().getDeclaredField(t_f_name);
                if (tf != null) {
                    tf.setAccessible(true);
                    f.setAccessible(true);
                    f.set(r, tf.get(t));
                }
            }
        }
        return r;
    }

    /*
     * (non-Javadoc)
     * @see
     */
    @Override
    public <R extends Serializable, T extends Object> List<R> converterList(List<T> ts, Class<R> clzR) throws Exception {
        if (ts == null || clzR == null)
            throw new NullPointerException();
        if (ts.isEmpty())
            return Collections.emptyList();

        List<R> rs = new ArrayList<R>(ts.size() + 1);
        for (T t : ts) {
            rs.add(this.converter(t, clzR));
        }
        return rs;
    }

}
