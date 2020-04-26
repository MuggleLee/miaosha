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
@TableName("stock_log")
public class StockLogPO {
    private String stockLogId;

    private Integer itemId;

    private Integer amount;

    private Integer status;

    private Date addTime;

    private Date updateTime;
}