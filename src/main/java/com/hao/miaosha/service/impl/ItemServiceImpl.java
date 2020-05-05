package com.hao.miaosha.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hao.miaosha.bo.ItemBO;
import com.hao.miaosha.bo.PromoBO;
import com.hao.miaosha.exception.EmError;
import com.hao.miaosha.exception.MyException;
import com.hao.miaosha.mapper.ItemMapper;
import com.hao.miaosha.mapper.ItemStockMapper;
import com.hao.miaosha.po.ItemPO;
import com.hao.miaosha.po.ItemStockPO;
import com.hao.miaosha.po.PromoPO;
import com.hao.miaosha.service.CacheService;
import com.hao.miaosha.service.ItemService;
import com.hao.miaosha.service.PromoService;
import com.hao.miaosha.units.PromoStatus;
import com.hao.miaosha.validator.ValidatorImpl;
import com.hao.miaosha.vo.ItemVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @author Muggle Lee
 * @Date: 2020/4/26 16:19
 */
@Service
public class ItemServiceImpl implements ItemService {

    @Autowired
    private ItemMapper itemMapper;

    @Autowired
    private ItemStockMapper itemStockMapper;

    @Autowired
    private ValidatorImpl validator;

    @Autowired
    private PromoService promoService;

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 添加商品
     *
     * @param itemBo
     * @return 返回VO层的商品信息
     * @throws MyException
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public ItemVO createItem(ItemBO itemBo) throws MyException {
        // 校验参数
        validator.validator(itemBo);

        // 保存数据到item表
        ItemPO itemPO = ItemPO.builder()
                .imgUrl(itemBo.getImgUrl())
                .title(itemBo.getTitle())
                .price(itemBo.getPrice().doubleValue())
                .sales(0)// 新增的商品，售卖的数量为0
                .description(itemBo.getDescription())
                .addTime(new Date())
                .updateTime(new Date())
                .build();
        itemMapper.insert(itemPO);

        // 保存数据到item_stock表
        ItemStockPO itemStockPO = ItemStockPO.builder()
                .itemId(itemPO.getId())
                .stock(itemBo.getStock())
                .addTime(new Date())
                .updateTime(new Date())
                .build();
        itemStockMapper.insert(itemStockPO);

        ItemVO itemVO = ItemVO.builder()
                .id(itemPO.getId())
                .title(itemPO.getTitle())
                .description(itemPO.getDescription())
                .price(new BigDecimal(itemPO.getPrice()))
                .stock(itemStockPO.getStock())
                .imgUrl(itemPO.getImgUrl())
                .sales(itemPO.getSales())
                .build();

        // 返回商品id对应的对象
        return itemVO;
    }

    /**
     * 获取商品列表
     *
     * @return
     */
    @Override
    public List<ItemVO> itemList() {
        List<ItemVO> itemVOList = itemMapper.selectAllItemInfo();
        return itemVOList;
    }

    /**
     * 通过商品id获取商品详细信息
     *
     * @param id 商品id
     * @return
     */
    @Override
    public ItemBO getItemById(Integer id) throws MyException {
        ItemPO itemPO = itemMapper.selectOne(new QueryWrapper<ItemPO>().eq("id", id));
        ItemStockPO itemStockPO = itemStockMapper.selectOne(new QueryWrapper<ItemStockPO>().eq("id", id));
        if (StringUtils.isEmpty(itemPO)) {
            throw new MyException(EmError.PARAMETER_VALIDATION_ERROR, "商品Id异常");
        }
        ItemBO itemBO = ItemBO.builder()
                .id(itemPO.getId())
                .title(itemPO.getTitle())
                .description(itemPO.getDescription())
                .imgUrl(itemPO.getImgUrl())
                .price(new BigDecimal(itemPO.getPrice()))
                .sales(itemPO.getSales())
                .stock(itemStockPO.getStock())
                .build();
        PromoBO promoBO = promoService.getPromoInfoByItemId(id);
        // 如果商品秒杀信息为空，秒杀状态设为0，代表没有秒杀
        if (!StringUtils.isEmpty(promoBO) && promoBO.getStatus() != 3) {
            itemBO.setPromoBO(promoBO);
        }
        return itemBO;
    }

    /**
     * 减少库存
     *
     * @param itemId
     * @param amount
     * @return
     */
    @Override
    @Transactional
    public boolean decreaseStock(Integer itemId, Integer amount) {
        int affectRow = itemStockMapper.updateStockAmountByItemId(itemId, amount);
        if (affectRow < 1) {
            return false;
        }
        return true;
    }

    /**
     * 从Redis缓存中获取商品信息
     * @param itemId
     * @return
     * @throws MyException
     */
    @Override
    public ItemBO getItemByIdInCache(Integer itemId) throws MyException {
        String key = "item_validate_" + itemId;
        ItemBO itemBO = (ItemBO) redisTemplate.opsForValue().get(key);
        if(StringUtils.isEmpty(itemBO)){
            itemBO = this.getItemById(itemId);
            redisTemplate.opsForValue().set(key,itemBO);
            // 设置10分钟过期
            redisTemplate.expire(key,10,TimeUnit.MINUTES);
        }
        return itemBO;
    }

}
