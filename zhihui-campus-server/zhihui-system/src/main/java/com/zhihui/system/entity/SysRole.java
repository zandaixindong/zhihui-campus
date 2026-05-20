package com.zhihui.system.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.zhihui.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 角色实体
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_role")
public class SysRole extends BaseEntity {

    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    private String roleName;

    private String roleKey;

    private Integer sortOrder;

    private Integer status;

    @TableLogic
    private Integer deleted;
}
