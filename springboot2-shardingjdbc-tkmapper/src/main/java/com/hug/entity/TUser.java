package com.hug.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Table;
import java.util.Date;

@Data
@Table(name = "t_user")
public class TUser {

    @Column(name = "id")
    @GeneratedValue(generator = "JDBC")
    private Long id;//主键

    private Long userId;

    private String name;//姓名

    private String phone;//手机号

    private String email;//电子邮件

    private String password;//经过MD5加密的密码

    private Integer cityId; //城市id

    private Date createTime;//创建时间

    private Integer sex;//性别

}