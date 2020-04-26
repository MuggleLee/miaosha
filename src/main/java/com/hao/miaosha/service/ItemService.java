package com.hao.miaosha.service;

import com.hao.miaosha.bo.ItemBo;
import com.hao.miaosha.exception.MyException;
import com.hao.miaosha.vo.ItemVO;

import java.util.List;

/**
 * @author Muggle Lee
 * @Date: 2020/4/26 16:18
 */
public interface ItemService {
    // 创建商品
    ItemVO createItem(ItemBo itemBo) throws MyException;

    // 返回商品列表
    List<ItemBo> itemList();

    // 返回商品详情
    ItemBo getItemById(int id);
}
