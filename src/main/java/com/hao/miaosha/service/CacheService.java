package com.hao.miaosha.service;

/**
 * 封装本地缓存操作类
 * @author MuggleLee
 * @date 2020/5/5
 */
public interface CacheService {

    void setCommonCache(String key,Object value);

    Object getFromCommonCache(String key);
}
