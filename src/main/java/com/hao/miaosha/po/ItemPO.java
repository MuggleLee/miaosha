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
@TableName("item")
public class ItemPO {
    private Integer id;

    private String title;

    private Double price;

    private String description;

    private Integer sales;

    private String imgUrl;

    private Date addTime;

    private Date updateTime;
}