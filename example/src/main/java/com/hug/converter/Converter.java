package com.hug.converter;

import java.io.Serializable;
import java.util.List;

/**
 * @description 把目标T 转换成结果R
 * @version 1.0
 */
public interface Converter {
    <R extends Serializable> R converter(Object t, Class<R> clzR) throws Exception;

    <R extends Serializable, T extends Object> List<R> converterList(List<T> ts, Class<R> clzR) throws Exception;
}