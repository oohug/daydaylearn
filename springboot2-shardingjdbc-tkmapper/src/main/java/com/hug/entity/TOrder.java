package com.hug.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Table;
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
    private LocalDateTime insertTime;
    private LocalDateTime updateTime;
}