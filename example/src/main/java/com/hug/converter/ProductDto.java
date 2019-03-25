package com.hug.converter;

import lombok.Data;

import java.io.Serializable;

@Data
public class ProductDto implements Serializable {

    @Conversion("goodsId")
    protected long productId;    // 商品spuId
    @Conversion("unitPrice")
    protected long productPrice; // 单价
    @Conversion("goodsNum")
    protected int productNum;   // 数量
    @Conversion("goodsType")
    protected int productType;  // 商品类型
    @Conversion
    protected long prePrice;    // 优惠额度
    @Conversion
    protected String imgUrl;    // 商家小图url （七牛云的图片url完整地址）
    @Conversion("goodsName")
    protected String productName; // 商品名称
    @Conversion
    protected Long skuId;       // 商品skuId
    @Conversion
    protected String skuText;

    protected String categoryId;

    /**
     * 车行id
     *
     * @mbggenerated
     */
    private Long itemId;

    /**
     * 订单id
     */
    private Long orderId;

    /**
     * 来源：web/wap...
     */
    private String orderSource;

    /**
     * 类型
     */
    private String orderDevice;

    /**
     * 渠道类型
     */
    private String orderChannel;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 推荐ID
     */
    private String orderCode;


    /**
     * 类目值
     */
    private Integer categoryLabelValue;

    /**
     * 商品采购价
     */
    private Long purchasePrice;

    /**
     * 0：未点赞，1：点赞
     */
    @Conversion("favourFlag")
    protected Integer favourFlag;

}
