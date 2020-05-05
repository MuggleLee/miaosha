package com.hao.miaosha.bo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.joda.time.DateTime;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author MuggleLee
 * @date 2020/5/1
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PromoBO {
    private Integer id;

    private String promoName;

    private DateTime startDate;

    private DateTime endDate;

    private Integer itemId;

    private BigDecimal promoItemPrice;

    private int status;
}
