package com.hao.miaosha.service.impl;

import com.hao.miaosha.bo.OrderBO;
import com.hao.miaosha.exception.MyException;
import com.hao.miaosha.mapper.OrderMapper;
import com.hao.miaosha.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Muggle Lee
 * @Date: 2020/4/27 14:29
 */
@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderMapper orderMapper;

    @Override
    public OrderBO createOrder(Integer userId, Integer itemId, Integer promoId, Integer amount, String stockLogId) throws MyException {
        // 校验参数合法性

        //落单减库存

        //订单入库


        return null;
    }
}
