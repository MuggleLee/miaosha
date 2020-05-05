package com.hao.miaosha.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @author MuggleLee
 * @date 2020/4/30
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderVO {
    private String orderId;
    private String title;
    private BigDecimal orderPrice;
}
