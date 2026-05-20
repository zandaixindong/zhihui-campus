package com.zhihui.student.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 奖惩记录实体
 */
@Data
@TableName("student_reward_punishment")
public class RewardPunishment {

    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /** 学生ID */
    private Long studentId;

    /** 学生姓名 */
    private String studentName;

    /** 类型（1奖励2惩罚） */
    private Integer type;

    /** 级别（1国家级2省级3市级4校级5院级） */
    private Integer level;

    /** 名称 */
    private String title;

    /** 描述 */
    private String description;

    /** 金额（奖励金额或罚款金额） */
    private java.math.BigDecimal amount;

    /** 日期 */
    private LocalDate eventDate;

    /** 文件编号 */
    private String documentNo;

    /** 附件 */
    private String attachments;

    /** 录入人ID */
    private Long recorderId;

    /** 状态（0草稿1已发布） */
    private Integer status;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
