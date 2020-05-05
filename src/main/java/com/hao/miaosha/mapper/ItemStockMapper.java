package com.hao.miaosha.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hao.miaosha.po.ItemStockPO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author Muggle Lee
 * @Date: 2020/4/26 17:02
 */
public interface ItemStockMapper extends BaseMapper<ItemStockPO> {

    @Update("update item_stock set stock = (stock-#{amount}) where item_id = #{itemId} and stock > #{amount}")
    int updateStockAmountByItemId(@Param("itemId") Integer itemId, @Param("amount") Integer amount);
}
