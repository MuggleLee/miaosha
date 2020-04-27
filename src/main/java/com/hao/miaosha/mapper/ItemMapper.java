package com.hao.miaosha.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hao.miaosha.po.ItemPO;
import com.hao.miaosha.vo.ItemVO;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author Muggle Lee
 * @Date: 2020/4/26 16:21
 */
public interface ItemMapper  extends BaseMapper<ItemPO> {

    @Select("select * from item  inner join item_stock on item.id = item_stock.item_id")
    List<ItemVO> selectAllItemInfo();

}
