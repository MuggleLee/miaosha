package com.hao.miaosha.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hao.miaosha.bo.ItemBO;
import com.hao.miaosha.exception.MyException;
import com.hao.miaosha.mapper.ItemMapper;
import com.hao.miaosha.mapper.ItemStockMapper;
import com.hao.miaosha.po.ItemPO;
import com.hao.miaosha.po.ItemStockPO;
import com.hao.miaosha.service.ItemService;
import com.hao.miaosha.validator.ValidatorImpl;
import com.hao.miaosha.vo.ItemVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

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
     * @return
     */
    @Override
    public List<ItemVO> itemList() {
        List<ItemVO> itemVOList = itemMapper.selectAllItemInfo();
        return itemVOList;
    }

    /**
     * 通过商品id获取商品详细信息
     * @param id 商品id
     * @return
     */
    @Override
    public ItemVO getItemById(Integer id) {
        ItemPO itemPO = itemMapper.selectOne(new QueryWrapper<ItemPO>().eq("id", id));
        ItemStockPO itemStockPO = itemStockMapper.selectOne(new QueryWrapper<ItemStockPO>().eq("id", id));
        // 组装为视图层VO
        ItemVO itemVO = ItemVO.builder()
                .title(itemPO.getTitle())
                .description(itemPO.getDescription())
                .imgUrl(itemPO.getImgUrl())
                .price(new BigDecimal(itemPO.getPrice()))
                .sales(itemPO.getSales())
                .stock(itemStockPO.getStock())
                .build();
        return itemVO;
    }

    /**
     * 减少库存
     * @param itemId
     * @param amount
     * @return
     */
    @Override
    public boolean decreaseStock(Integer itemId, Integer amount) {
        return false;
    }
}
