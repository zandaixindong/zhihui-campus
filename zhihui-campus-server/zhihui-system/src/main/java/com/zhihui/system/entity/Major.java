package com.zhihui.system.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 专业实体
 */
@Data
@TableName("major")
public class Major {

    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    private Long collegeId;

    private String majorCode;

    private String majorName;

    private String majorEnName;

    private String degreeType;

    private Integer sortOrder;

    private Integer status;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
