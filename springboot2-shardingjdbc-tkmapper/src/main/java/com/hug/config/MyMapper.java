package com.hug.config;

import tk.mybatis.mapper.common.ExampleMapper;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;
import tk.mybatis.mapper.common.RowBoundsMapper;

public interface MyMapper<T> extends Mapper<T>, MySqlMapper<T>, ExampleMapper<T>, RowBoundsMapper<T> {
    //*** 重要提醒：该接口不能被扫描到


    /**
     * tk mybatis update 类型
     *
     * 1.updateByExample：根据给定的example更新所有属性，包括主键id，所有属性都需要有值
     *
     * 2.updateByExampleSelective：根据给定的example更新有值的属性
     *      Example example = new Example(Test.class);
     *      example.createCriteria().andEqualTo("name", "test");
     *      Test record = new Test();
     *      record.setName("hello");
     *      mapper.updateByExampleSelective(record, example);
     *
     * 3.updateByPrimaryKey：根据主键更新所有属性
     *
     * 4.updateByPrimaryKeySelective：根据主键更新有值属性
     *
     */

}