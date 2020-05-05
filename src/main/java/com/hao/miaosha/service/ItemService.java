package com.hao.miaosha.service;

import com.hao.miaosha.bo.ItemBO;
import com.hao.miaosha.bo.UserBO;
import com.hao.miaosha.exception.MyException;
import com.hao.miaosha.vo.ItemVO;

import java.util.List;

/**
 * @author Muggle Lee
 * @Date: 2020/4/26 16:18
 */
public interface ItemService {
    // 创建商品
    ItemVO createItem(ItemBO itemBo) throws MyException;

    // 返回商品列表
    List<ItemVO> itemList();

    // 返回商品详情
    ItemBO getItemById(Integer id) throws MyException;

    // 减库存
    boolean decreaseStock(Integer itemId,Integer amount);

    // 从Redis缓存中获取商品信息
    ItemBO getItemByIdInCache(Integer itemId) throws MyException;
}
