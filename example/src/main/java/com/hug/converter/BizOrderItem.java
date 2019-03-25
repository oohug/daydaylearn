/**
 * BizOrder.java
 */
package com.hug.converter;

import lombok.Data;

import java.util.Date;

/**
 * @version 1.0
 */
@Data
public class BizOrderItem extends BaseEntity {

    private Long orderId;
    private Long goodsId;
    private Long skuId;
    private String skuText;
    private String goodsName;
    private Integer goodsNum;
    private Integer goodsType;
    private Long unitPrice;
    private Long prePrice;
    private String imgUrl;
    private Date createTime;

    /**
     * 0：未点赞，1：点赞
     */
    private Integer favourFlag;

    /**
     * 1：类目  2：标签
     *
     * @mbggenerated
     */
    private String field1;

    /**
     * 活动Id
     *
     * @mbggenerated
     */
    private String field2;

    /**
     * 推荐人code
     */
    private String field3;
    /**
     * 优惠券
     */
    private String field4;
    private String field5;
    private String field6;

}
