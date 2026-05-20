package com.zhihui.system.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 学院实体
 */
@Data
@TableName("college")
public class College {

    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    private String collegeCode;

    private String collegeName;

    private String collegeEnName;

    private Integer sortOrder;

    private Integer status;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
