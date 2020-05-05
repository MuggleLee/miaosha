package com.hao.miaosha.service;

import com.hao.miaosha.bo.PromoBO;
import com.hao.miaosha.po.PromoPO;
import org.springframework.stereotype.Service;

/**
 * @author MuggleLee
 * @date 2020/5/1
 */
public interface PromoService {

    /**
     * 通过商品Id获取其秒杀信息
     * @param itemId
     * @return
     */
    PromoBO getPromoInfoByItemId(Integer itemId);
}
