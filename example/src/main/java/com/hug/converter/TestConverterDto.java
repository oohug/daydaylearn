/**
 * TestDtoAnn.java
 */
package com.hug.converter;

import java.util.ArrayList;
import java.util.List;

/**
 * @version 1.0
 */
public class TestConverterDto {

    public static void main(String[] args) {
        List<BizOrderItem> lst = new ArrayList<>();

        BizOrderItem x = new BizOrderItem();
        x.setGoodsId(1L);
        x.setGoodsName("商品Ａ");
        x.setGoodsNum(22);
        x.setGoodsType(1);
        x.setImgUrl("xxxxxx.jpg");
        x.setOrderId(222L);
        x.setPrePrice(333L);
        x.setUnitPrice(444L);

        lst.add(x);

        BizOrderItem xx = new BizOrderItem();
        xx.setGoodsId(2L);
        xx.setGoodsName("商品Ａ");
        xx.setGoodsNum(22);
        xx.setGoodsType(1);
        xx.setImgUrl("xxxxxx.jpg");
        xx.setOrderId(222L);
        xx.setPrePrice(333L);
        xx.setUnitPrice(444L);

        lst.add(xx);

        AnnotationReflexConverter arc = new AnnotationReflexConverter();
        try {
            List targetList = arc.converterList(lst, ProductDto.class);
            System.out.println(targetList);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
