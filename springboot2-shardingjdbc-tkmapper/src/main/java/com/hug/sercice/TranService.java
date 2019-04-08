package com.hug.sercice;

import com.hug.entity.TOrder;
import com.hug.entity.TUser;
import com.hug.mapper.TOrderMapper;
import com.hug.mapper.TUserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Date;

@Service
public class TranService {

    @Autowired
    private TOrderMapper orderMapper;
    @Autowired
    private TUserMapper tUserMapper;

    @Transactional(rollbackFor = Exception.class)
    public void testTran() {
        TOrder entity10 = new TOrder();
        entity10.setOrderId(10000L);
        entity10.setOrderNo("No1000000");
//        entity10.setUserId(102333001L); // db1.t_order_
        entity10.setUserId(102333000L); // db1.t_order_
        entity10.setInsertTime(LocalDateTime.now());
        entity10.setUpdateTime(LocalDateTime.now());
        entity10.setIsactive(99);
        orderMapper.insertSelective(entity10);

        // db0.t_user
        TUser user = new TUser();
        user.setUserId(1001L);
        user.setCityId(102);
        user.setCreateTime(new Date());
        user.setEmail("xx@qq.com");
        user.setName("张三");
        user.setPhone("1756654874");
        tUserMapper.insert(user);

        System.out.println(1/0);

    }

}
