/**
 * ApacheConverter.java
 */
package com.hug.converter;

import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @version 1.0
 * @description Apache传换者
 */
public final class ApacheConverter implements Converter {

    public static final ApacheConverter getInstance() {
        return new ApacheConverter();
    }

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

    public <R extends Serializable, T extends Object> List<R> converterList(List<T> ts, Class<R> clzR) throws Exception {
        if (ts == null || clzR == null)
            throw new NullPointerException();
        if (ts.isEmpty())
            return Collections.emptyList();

        List<R> result = new ArrayList<R>();
        R o;
        for (T e : ts) {
            o = null;
            o = this.converter(e, clzR);
            if (o != null)
                result.add(o);
        }

        return result;
    }
}
