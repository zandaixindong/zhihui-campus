package com.zhihui.student.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 勤工助学实体
 */
@Data
@TableName("student_work_study")
public class WorkStudy {

    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /** 岗位名称 */
    private String positionName;

    /** 岗位类型 */
    private String positionType;

    /** 工作地点 */
    private String workLocation;

    /** 招聘人数 */
    private Integer recruitCount;

    /** 已录用人数 */
    private Integer hiredCount;

    /** 薪资（元/小时） */
    private BigDecimal hourlyRate;

    /** 工作时间描述 */
    private String workTimeDesc;

    /** 岗位要求 */
    private String requirements;

    /** 联系人 */
    private String contactPerson;

    /** 联系电话 */
    private String contactPhone;

    /** 发布部门 */
    private String department;

    /** 状态（0招聘中1已结束） */
    private Integer status;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
