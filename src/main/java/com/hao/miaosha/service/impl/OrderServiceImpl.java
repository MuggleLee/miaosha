package com.hao.miaosha.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hao.miaosha.bo.ItemBO;
import com.hao.miaosha.bo.UserBO;
import com.hao.miaosha.exception.EmError;
import com.hao.miaosha.exception.MyException;
import com.hao.miaosha.mapper.ItemMapper;
import com.hao.miaosha.mapper.OrderMapper;
import com.hao.miaosha.mapper.SequenceMapper;
import com.hao.miaosha.po.OrderPO;
import com.hao.miaosha.po.SequencePO;
import com.hao.miaosha.service.ItemService;
import com.hao.miaosha.service.OrderService;
import com.hao.miaosha.service.UserService;
import com.hao.miaosha.units.PromoStatus;
import com.hao.miaosha.vo.ItemVO;
import com.hao.miaosha.vo.OrderVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * @author Muggle Lee
 * @Date: 2020/4/27 14:29
 */
@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private ItemService itemService;

    @Autowired
    private UserService userService;

    @Autowired
    private SequenceMapper sequenceMapper;

    @Autowired
    private ItemMapper itemMapper;

    @Transactional
    @Override
    public OrderVO createOrder(Integer userId, Integer itemId, Integer promoId, Integer amount, String stockLogId) throws MyException {
        // 校验参数合法性
        ItemBO itemBO = itemService.getItemByIdInCache(itemId);
//        ItemBO itemBO = itemService.getItemById(itemId);
        if (StringUtils.isEmpty(itemBO)) {
            throw new MyException(EmError.PARAMETER_VALIDATION_ERROR, "商品信息不存在");
        }

        UserBO userBO = userService.getUserByIdInCache(userId);
//        UserBO userBO = userService.getByUserId(userId);
        if (StringUtils.isEmpty(userBO)) {
            throw new MyException(EmError.PARAMETER_VALIDATION_ERROR, "用户信息不存在");
        }

        if (amount <= 0 || amount > 99) {
            throw new MyException(EmError.PARAMETER_VALIDATION_ERROR, "商品数量异常");
        }

        if (promoId != null) {
            // 校验对应活动是否存在这个适用商品
            if (promoId != itemBO.getPromoBO().getId()) {
                throw new MyException(EmError.PARAMETER_VALIDATION_ERROR, "活动信息不正确");
            } else if (itemBO.getPromoBO().getStatus() != PromoStatus.PROMO_STARTING.getPromoStatus()) {
                // 校验活动是否正在进行中
                throw new MyException(EmError.PARAMETER_VALIDATION_ERROR, "活动信息还未开始");
            }
        }

        //落单减库存
        boolean isDecrease = itemService.decreaseStock(itemId, amount);
        if (!isDecrease) {
            throw new MyException(EmError.STOCK_NOT_ENOUGH);
        }

        //订单入库
        OrderPO orderPO = OrderPO.builder()
                .id(generateOrderId())
                .userId(userId)
                .itemId(itemId)
                .amount(amount)
                .itemPrice(itemBO.getPrice().doubleValue())
                .orderPrice(new BigDecimal(amount).multiply(itemBO.getPrice()).doubleValue())
                .addTime(new Date())
                .updateTime(new Date())
                .build();
        orderMapper.insert(orderPO);

        // 增加商品销量
        itemMapper.increaseItemSales(itemId, amount);

        //返回前端
        return OrderVO.builder()
                .orderId(orderPO.getId())
                .title(itemBO.getTitle())
                .orderPrice(new BigDecimal(orderPO.getOrderPrice()))
                .build();
    }

    /**
     * 生成订单ID
     *
     * @return
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public String generateOrderId() {
        //订单号有16位
        StringBuilder builder = new StringBuilder();
        LocalDateTime time = LocalDateTime.now();
        String nowDate = time.format(DateTimeFormatter.ISO_DATE).replace("-", "");
        //前8位为时间信息，年月日
        builder.append(nowDate);

        SequencePO sequencePO = sequenceMapper.getSequenceByName("order_info");
        String sequenceId = sequencePO.getCurrentValue().toString();
        sequencePO.setCurrentValue(sequencePO.getCurrentValue() + sequencePO.getStep());
        sequenceMapper.update(sequencePO,new QueryWrapper<>());
        for (int i = 0; i < 6 - sequenceId.length(); i++) {
            builder.append(0);
        }
        //中间6位为自增序列
        builder.append(sequenceId);

        //最后2位为分库分表位,暂时写死
        builder.append("00");
        return builder.toString();
    }
}
