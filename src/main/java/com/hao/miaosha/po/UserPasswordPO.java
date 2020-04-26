package com.hao.miaosha.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("user_password")
public class UserPasswordPO {
    @TableId(type = IdType.AUTO)
    private Integer id;

    private String encrptPassword;

    private Integer userId;

}