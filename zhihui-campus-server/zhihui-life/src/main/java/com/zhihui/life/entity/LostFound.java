package com.zhihui.life.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 失物招领实体
 */
@Data
@TableName("life_lost_found")
public class LostFound {

    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    private Long userId;

    private Integer type;

    private String title;

    private String description;

    private String category;

    private String location;

    private LocalDateTime lostTime;

    private String images;

    private String contactWay;

    private Integer status;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
