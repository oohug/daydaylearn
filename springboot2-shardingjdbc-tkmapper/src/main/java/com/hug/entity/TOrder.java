package com.hug.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.time.LocalDateTime;

@Data
@Table(name = "t_order")
public class TOrder {

    @Column(name = "id")
    @GeneratedValue(generator = "JDBC")
    private Long id;

    private Long userId;
    private Long orderId;
    private String orderNo;
    private Integer isactive;
    @Transient
    private LocalDateTime insertTime;
    @Transient
    private LocalDateTime updateTime;
}