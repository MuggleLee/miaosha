package com.hao.miaosha.validator;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;

/**
 * @author Muggle Lee
 * @Date: 2020/4/26 13:53
 */
@Data
public class ValidatorResult {
    private boolean hasError = false;
    private HashMap<String,String> errorMap = new HashMap<>();

    //实现通用的通过格式化字符串信息获取错误结果的msg方法
    public String getErrorMsg(){
        return StringUtils.join(errorMap.values().toArray(),",");
    }

}
