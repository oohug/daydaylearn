/**
 * ReflexConverter.java
 */
package com.hug.converter;

import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @description 反射转换者
 * @version 1.0
 */
public class ReflexConverter implements Converter {

    public <R extends Serializable> R converter(Object t, Class<R> clzR) {
        if (t == null || clzR == null)
            throw new NullPointerException();
        R o = null;
        try {
            o = clzR.newInstance();
            BeanUtils.copyProperties(t, o);

        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        return o;
    }

    public <R extends Serializable, T extends Object> List<R> converterList(
            List<T> ts, Class<R> clzR) throws Exception {
        if (ts == null || clzR == null)
            throw new NullPointerException();
        if (ts.isEmpty())
            return Collections.emptyList();

        List<R> result = new ArrayList<R>();
        R o = null;
        for (T e : ts) {
            try {
                o = clzR.newInstance();
                BeanUtils.copyProperties(e, o);
                result.add(o);
            } catch (InstantiationException e1) {
                e1.printStackTrace();
            } catch (IllegalAccessException e1) {
                e1.printStackTrace();
            }
        }
        return result;
    }

}
