package com.hao.miaosha.controller;

import com.hao.miaosha.bo.ItemBO;
import com.hao.miaosha.exception.MyException;
import com.hao.miaosha.response.CommonResonse;
import com.hao.miaosha.service.CacheService;
import com.hao.miaosha.service.ItemService;
import com.hao.miaosha.vo.ItemVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author Muggle Lee
 * @Date: 2020/4/27 9:37
 */
@RestController("Item")
@RequestMapping("/item")
@CrossOrigin(allowCredentials = "true", allowedHeaders = "*")
public class ItemController {

    @Autowired
    private ItemService itemService;

    @Autowired
    CacheService cacheService;

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 创建商品
     *
     * @param title
     * @param description
     * @param price
     * @param stock
     * @param imgUrl
     * @return
     * @throws MyException
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ResponseBody
    public CommonResonse createItem(@RequestParam("title") String title,
                                    @RequestParam(name = "description") String description,
                                    @RequestParam("price") BigDecimal price,
                                    @RequestParam("stock") int stock,
                                    @RequestParam(name = "imgUrl") String imgUrl) throws MyException {
        ItemBO itemBO = ItemBO.builder()
                .title(title)
                .description(description)
                .price(price)
                .stock(stock)
                .imgUrl(imgUrl)
                .build();
        ItemVO itemVO = itemService.createItem(itemBO);
        return CommonResonse.create(itemVO);
    }

    /**
     * 商品详情页浏览
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/get", method = {RequestMethod.GET})
    @ResponseBody
    public CommonResonse getItem(@RequestParam(name = "id") Integer id) throws MyException {

        ItemBO itemBO = null;
        // 查询本地缓存
        ItemBO cacheItem = (ItemBO) cacheService.getFromCommonCache("item_" + id);
        if (StringUtils.isEmpty(cacheItem)) {
            // 查询Redis，如果Redis内有缓存的数据，直接返回Redis内的数据，否则将请求通过Mysql查询，然后写入Redis内
            ItemBO redisItemBO = (ItemBO) redisTemplate.opsForValue().get("item_" + id);
            if (StringUtils.isEmpty(redisItemBO)) {
                itemBO = itemService.getItemById(id);
                redisTemplate.opsForValue().set("item_" + id, itemBO);
            } else {
                itemBO = redisItemBO;
            }
            cacheService.setCommonCache("item_" + id, itemBO);
        } else {
            itemBO = cacheItem;
        }

        ItemVO itemVO = ItemVO.builder()
                .id(itemBO.getId())
                .description(itemBO.getDescription())
                .imgUrl(itemBO.getImgUrl())
                .price(itemBO.getPrice())
                .sales(itemBO.getSales())
                .title(itemBO.getTitle())
                .stock(itemBO.getStock())
                .build();
        if (!StringUtils.isEmpty(itemBO.getPromoBO())) {
            itemVO.setPromoId(itemBO.getPromoBO().getId());
            itemVO.setPromoPrice(itemBO.getPromoBO().getPromoItemPrice());
            itemVO.setPromoStatus(itemBO.getPromoBO().getStatus());
            itemVO.setStartDate(itemBO.getPromoBO().getStartDate().toString());
        }
        return CommonResonse.create(itemVO);
    }

    /**
     * 商品列表页面浏览
     *
     * @return
     */
    @RequestMapping(value = "/list", method = {RequestMethod.GET})
    @ResponseBody
    public CommonResonse listItem() {
        List<ItemVO> itemVOList = itemService.itemList();
        return CommonResonse.create(itemVOList);
    }

}
