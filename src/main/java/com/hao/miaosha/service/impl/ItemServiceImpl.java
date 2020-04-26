package com.hao.miaosha.service.impl;

import com.hao.miaosha.bo.ItemBo;
import com.hao.miaosha.exception.MyException;
import com.hao.miaosha.mapper.ItemMapper;
import com.hao.miaosha.mapper.ItemStockMapper;
import com.hao.miaosha.po.ItemPO;
import com.hao.miaosha.po.ItemStockPO;
import com.hao.miaosha.service.ItemService;
import com.hao.miaosha.validator.ValidatorImpl;
import com.hao.miaosha.validator.ValidatorResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Transactional
    @Override
    public ItemBo createItem(ItemBo itemBo) throws MyException {
        // 校验参数
        validator.validator(itemBo);

        // 保存数据到item表
        ItemPO itemPO = ItemPO.builder()
                .imgUrl(itemBo.getImgUrl())
                .title(itemBo.getTitle())
                .price(itemBo.getPrice().doubleValue())
                .sales(itemBo.getSales())
                .description(itemBo.getDescription())
                .build();
        itemMapper.insert(itemPO);

        // 保存数据到item_stock表
        ItemStockPO itemStockPO = ItemStockPO.builder()
                .itemId(itemPO.getId())
                .stock(itemBo.getStock())
                .build();
        itemStockMapper.insert(itemStockPO);
        // 返回商品id对应的对象
        return this.getItemById(itemPO.getId());
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

        return null;
    }
}
