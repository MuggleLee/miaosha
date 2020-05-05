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
@TableName("promo")
public class PromoPO {
    private Integer id;

    private String promoName;

    private Date startTime;

    private Date endTime;

    private Integer itemId;

    private Double promoItemPrice;

}