package com.hao.miaosha.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hao.miaosha.po.SequencePO;
import org.apache.ibatis.annotations.Select;

/**
 * @author MuggleLee
 * @date 2020/4/30
 */
public interface SequenceMapper extends BaseMapper<SequencePO> {

    @Select("select * from sequence_info where name = #{order_info} for update ")
    SequencePO getSequenceByName(String order_info);
}
