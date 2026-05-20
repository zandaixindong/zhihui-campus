package com.zhihui.life.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * 场地信息实体
 */
@Data
@TableName("life_venue")
public class Venue {

    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    private String venueName;

    private String venueType;

    private String location;

    private Integer capacity;

    private String facilities;

    private String contactPhone;

    private LocalTime openTime;

    private LocalTime closeTime;

    private Integer status;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
