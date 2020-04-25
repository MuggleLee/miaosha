package com.hao.miaosha.response;

import lombok.Data;

/**
 * 定义返回信息
 * @author MuggleLee
 * @date 2020/4/25
 */
@Data
public class CommonResonse {
    private String status;
    private Object data;

    public static CommonResonse create(Object data){
        return create(data,"success");
    }

    public static CommonResonse create(Object data,String status){
        CommonResonse resonse = new CommonResonse();
        resonse.setData(data);
        resonse.setStatus(status);
        return resonse;
    }
}
