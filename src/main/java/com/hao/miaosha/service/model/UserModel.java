package com.hao.miaosha.service.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author MuggleLee
 * @date 2020/4/25
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserModel {
    private Integer id;

    private String name;

    private Byte gender;

    private Integer age;

    private String telphone;

    private String registerMode;

    private String thirdPartyId;

    private String encrptPassword;
}
