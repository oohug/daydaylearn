package com.hug.mapper;

import com.hug.config.MyMapper;
import com.hug.entity.TOrder;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

@Mapper
@Component
public interface TOrderMapper extends MyMapper<TOrder> {

}
