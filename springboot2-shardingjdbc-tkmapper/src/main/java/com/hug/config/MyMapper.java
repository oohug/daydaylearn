package com.hug.config;

import tk.mybatis.mapper.common.ExampleMapper;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;
import tk.mybatis.mapper.common.RowBoundsMapper;

public interface MyMapper<T> extends Mapper<T>, MySqlMapper<T>, ExampleMapper<T>, RowBoundsMapper<T> {
    //*** 重要提醒：该接口不能被扫描到
}