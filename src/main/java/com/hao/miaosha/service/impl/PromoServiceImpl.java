package com.hao.miaosha.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hao.miaosha.bo.PromoBO;
import com.hao.miaosha.mapper.PromoMapper;
import com.hao.miaosha.po.PromoPO;
import com.hao.miaosha.service.PromoService;
import com.hao.miaosha.units.PromoStatus;
import com.hao.miaosha.vo.PromoVO;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author MuggleLee
 * @date 2020/5/1
 */
@Service
public class PromoServiceImpl implements PromoService {


    @Autowired
    private PromoMapper promoMapper;

    /**
     * 通过商品Id获取其秒杀信息
     *
     * @param itemId
     * @return
     */
    @Override
    public PromoBO getPromoInfoByItemId(Integer itemId) {
        PromoPO promoPO = promoMapper.selectOne(new QueryWrapper<PromoPO>().eq("item_id", itemId));
        if (StringUtils.isEmpty(promoPO)) {
            return null;
        }
        PromoBO promoBO = PromoBO.builder()
                .id(promoPO.getId())
                .promoName(promoPO.getPromoName())
                .promoItemPrice(new BigDecimal(promoPO.getPromoItemPrice()))
                .itemId(promoPO.getItemId())
                .startDate(new DateTime(promoPO.getStartTime()))
                .endDate(new DateTime(promoPO.getEndTime()))
                .build();
        if (promoBO.getStartDate().isAfterNow()) {
            promoBO.setStatus(PromoStatus.PROMO_NOT_START.getPromoStatus());
        }else if(promoBO.getEndDate().isBeforeNow()){
            promoBO.setStatus(PromoStatus.PROMO_END.getPromoStatus());
        }else{
            promoBO.setStatus(PromoStatus.PROMO_STARTING.getPromoStatus());
        }
        return promoBO;
    }
}
