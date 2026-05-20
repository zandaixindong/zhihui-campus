package com.zhihui.system.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.zhihui.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 用户实体
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_user")
public class SysUser extends BaseEntity {

    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    private String username;

    private String password;

    private String realName;

    private Integer userType;

    private Long collegeId;

    private Long majorId;

    private String className;

    private String phone;

    private String email;

    private String avatar;

    private Integer sex;

    private Integer status;

    private LocalDateTime lastLoginTime;

    private String lastLoginIp;

    @TableLogic
    private Integer deleted;
}
