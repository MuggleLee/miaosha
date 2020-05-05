package com.hao.miaosha.service.impl;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.hao.miaosha.service.CacheService;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.concurrent.TimeUnit;

/**
 * 本地缓存
 * @author MuggleLee
 * @date 2020/5/5
 */
@Service
public class CacheServiceImpl implements CacheService {

    private Cache<String,Object> cache = null;

    @PostConstruct
    public void init(){
        cache = CacheBuilder.newBuilder()
                // 设置缓存容器存储对象初始值
                .initialCapacity(10)
                // 设置缓存容器存储对象最大值
                .maximumSize(100)
                // 设置写缓存后多少秒过期
                .expireAfterWrite(60,TimeUnit.SECONDS)
                .build();
    }

    @Override
    public void setCommonCache(String key, Object value) {
        cache.put(key,value);
    }

    @Override
    public Object getFromCommonCache(String key) {
        return cache.getIfPresent(key);
    }
}
