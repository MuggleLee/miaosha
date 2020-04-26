package com.hao.miaosha.vo;

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
public class UserVO {

    private Integer id;

    private String name;

    private Byte gender;

    private Integer age;

    private String telphone;
}
