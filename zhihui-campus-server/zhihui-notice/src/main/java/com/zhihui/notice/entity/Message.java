package com.zhihui.notice.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 消息实体
 */
@Data
@TableName("notice_message")
public class Message {

    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    private Long userId;

    private String title;

    private String content;

    private Integer msgType;

    private String bizType;

    private Long bizId;

    private Integer isRead;

    private LocalDateTime readTime;

    private LocalDateTime createTime;
}
