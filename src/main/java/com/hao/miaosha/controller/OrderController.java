package com.hao.miaosha.controller;

import com.hao.miaosha.bo.UserBO;
import com.hao.miaosha.config.CommonConfig;
import com.hao.miaosha.exception.EmError;
import com.hao.miaosha.exception.MyException;
import com.hao.miaosha.response.CommonResonse;
import com.hao.miaosha.service.OrderService;
import com.hao.miaosha.vo.OrderVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * @author MuggleLee
 * @date 2020/4/25
 */
@RestController("Order")
@RequestMapping("/order")
@CrossOrigin(allowCredentials = "true", allowedHeaders = "*")
public class OrderController {

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private HttpServletRequest httpServletRequest;

    @Autowired
    private OrderService orderService;

    @RequestMapping(value = "/createOrder", method = {RequestMethod.POST}, consumes = {CommonConfig.CONTENT_TYPE_FORMED})
    @ResponseBody
    public CommonResonse createOrder(@RequestParam(name = "itemId") Integer itemId,
                                     @RequestParam(name = "amount") Integer amount,
                                     @RequestParam(name = "promoId") Integer promoId) throws MyException {
        String token = httpServletRequest.getParameterMap().get("token")[0];
        if (StringUtils.isEmpty(token)) {
            throw new MyException(EmError.USER_NOT_LOGIN, "用户未登陆，不能下单");
        }
        UserBO userBO = (UserBO) redisTemplate.opsForValue().get(token);
        if (StringUtils.isEmpty(userBO)) {
            throw new MyException(EmError.USER_NOT_LOGIN, "用户未登陆，不能下单");
        }

        // 用户登录成功后才可以下订单
        OrderVO orderVO = orderService.createOrder(userBO.getId(), itemId, promoId, amount, "");

        return CommonResonse.create(orderVO);
    }
}
