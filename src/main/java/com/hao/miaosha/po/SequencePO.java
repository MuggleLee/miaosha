package com.hao.miaosha.po;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("sequence_info")
public class SequencePO {
    private String name;

    private Integer currentValue;

    private Integer step;

}