package com.hao.miaosha.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hao.miaosha.po.ItemPO;
import com.hao.miaosha.vo.ItemVO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * @author Muggle Lee
 * @Date: 2020/4/26 16:21
 */
public interface ItemMapper extends BaseMapper<ItemPO> {

    /**
     * 读取所有的商品信息
     * @return
     */
    @Select("select * from item  inner join item_stock on item.id = item_stock.item_id")
    List<ItemVO> selectAllItemInfo();

    /**
     * 加上商品销量
     * @param itemId
     * @param amount
     * @return
     */
    @Update("update item set sales = (sales + #{amount}) where id = #{itemId}")
    int increaseItemSales(@Param("itemId") Integer itemId,@Param("amount") Integer amount);
}
