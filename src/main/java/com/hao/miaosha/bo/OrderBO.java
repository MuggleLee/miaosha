package com.hao.miaosha.bo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author Muggle Lee
 * @Date: 2020/4/27 14:33
 */
@Data
public class OrderBO {
    //订单ID
    private String id;

    //购买的用户id
    private Integer userId;

    //购买的商品id
    private Integer itemId;

    //若非空，则表示是以秒杀商品方式下单
    private Integer promoId;

    //购买商品的单价,若promoId非空，则表示秒杀商品价格
    private BigDecimal itemPrice;

    //购买数量
    private Integer amount;

    //购买金额,若promoId非空，则表示秒杀商品价格
    private BigDecimal orderPrice;
}
