package com.hao.miaosha.po;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("order_info")
public class OrderPO {
    private String id;

    private Integer userId;

    private Integer itemId;

    private Double itemPrice;

    private Integer amount;

    private Double orderPrice;

    private Integer promoId;

    private Date addTime;

    private Date updateTime;

}