package com.zhihui.system.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.zhihui.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 字典类型实体
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_dict_type")
public class SysDictType extends BaseEntity {

    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    private String dictName;

    private String dictType;

    private Integer status;
}
