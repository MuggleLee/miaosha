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
@TableName("item_stock")
public class ItemStockPO {
    private Integer id;

    private Integer stock;

    private Integer itemId;

    private Date addTime;

    private Date updateTime;
}