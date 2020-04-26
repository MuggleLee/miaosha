package com.hao.miaosha.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hao.miaosha.bo.ItemBo;
import com.hao.miaosha.exception.MyException;
import com.hao.miaosha.mapper.ItemMapper;
import com.hao.miaosha.mapper.ItemStockMapper;
import com.hao.miaosha.po.ItemPO;
import com.hao.miaosha.po.ItemStockPO;
import com.hao.miaosha.service.ItemService;
import com.hao.miaosha.validator.ValidatorImpl;
import com.hao.miaosha.validator.ValidatorResult;
import com.hao.miaosha.vo.ItemVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

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

    /**
     * 添加商品
     * @param itemBo
     * @return 返回VO层的商品信息
     * @throws MyException
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public ItemVO createItem(ItemBo itemBo) throws MyException {
        // 校验参数
        validator.validator(itemBo);

        // 保存数据到item表
        ItemPO itemPO = ItemPO.builder()
                .imgUrl(itemBo.getImgUrl())
                .title(itemBo.getTitle())
                .price(itemBo.getPrice().doubleValue())
                .sales(itemBo.getSales())
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

    @Override
    public List<ItemBo> itemList() {
        return null;
    }

    /**
     * 通过商品id获取商品详细信息
     * @param id 商品id
     * @return
     */
    @Override
    public ItemBo getItemById(int id) {
        ItemPO itemPO = itemMapper.selectOne(new QueryWrapper<ItemPO>().eq("id", id));
        ItemStockPO itemStockPO = itemStockMapper.selectOne(new QueryWrapper<ItemStockPO>().eq("id", id));
        // 组装为业务层的model
        ItemBo itemBo = ItemBo.builder()
                .id(id)
                .description(itemPO.getDescription())
                .imgUrl(itemPO.getImgUrl())
                .price(new BigDecimal(itemPO.getPrice()))
                .sales(itemPO.getSales())
                .title(itemPO.getTitle())
                .stock(itemStockPO.getStock())
                .build();
        return itemBo;
    }
}
