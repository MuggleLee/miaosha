package com.hao.miaosha.controller;

import com.hao.miaosha.bo.ItemBO;
import com.hao.miaosha.exception.MyException;
import com.hao.miaosha.response.CommonResonse;
import com.hao.miaosha.service.ItemService;
import com.hao.miaosha.vo.ItemVO;
import org.springframework.beans.factory.annotation.Autowired;
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

    /**
     * 创建商品
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
     * @param id
     * @return
     */
    @RequestMapping(value = "/get",method = {RequestMethod.GET})
    @ResponseBody
    public CommonResonse getItem(@RequestParam(name = "id")Integer id){
        ItemVO itemVO = itemService.getItemById(id);
        return CommonResonse.create(itemVO);
    }

    /**
     * 商品列表页面浏览
     * @return
     */
    @RequestMapping(value = "/list",method = {RequestMethod.GET})
    @ResponseBody
    public CommonResonse listItem(){
        List<ItemVO> itemVOList = itemService.itemList();
        return CommonResonse.create(itemVOList);
    }

}
