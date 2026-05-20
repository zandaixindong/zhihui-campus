package com.zhihui.notice.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 公告实体
 */
@Data
@TableName("notice_announcement")
public class Announcement {

    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    private String title;

    private String content;

    private Integer noticeType;

    private Long collegeId;

    private Long publishUserId;

    private String publishUserName;

    private Integer isTop;

    private Integer isRead;

    private Integer viewCount;

    private Integer status;

    private LocalDateTime publishTime;

    private LocalDateTime expireTime;

    private String createBy;

    private LocalDateTime createTime;

    private String updateBy;

    private LocalDateTime updateTime;
}
