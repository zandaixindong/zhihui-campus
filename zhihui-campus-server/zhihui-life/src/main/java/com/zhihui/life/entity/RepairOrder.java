package com.zhihui.life.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 报修工单实体
 */
@Data
@TableName("life_repair_order")
public class RepairOrder {

    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    private String orderNo;

    private Long userId;

    private String category;

    private String location;

    private String description;

    private String images;

    private String contactName;

    private String contactPhone;

    private Integer status;

    private Long handlerId;

    private String handleRemark;

    private LocalDateTime handleTime;

    private LocalDateTime completeTime;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
