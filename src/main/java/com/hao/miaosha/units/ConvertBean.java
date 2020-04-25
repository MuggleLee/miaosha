package com.hao.miaosha.units;

import org.springframework.beans.BeanUtils;
import org.springframework.util.StringUtils;

/**
 * @author MuggleLee
 * @date 2020/4/25
 */
public class ConvertBean {
    public static Object convertFromDataObject(Object fromObj, Object toObj) {
        if (StringUtils.isEmpty(fromObj) || StringUtils.isEmpty(toObj)) {
            return null;
        }
        BeanUtils.copyProperties(fromObj, toObj);
        return toObj;
    }
}
