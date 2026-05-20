package com.zhihui.edu.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.zhihui.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * 考试安排实体
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("edu_exam")
public class Exam extends BaseEntity {

    @TableId(type = IdType.AUTO)
    private Long id;

    /** 开课ID */
    private Long courseOfferId;

    /** 课程名称 */
    private String courseName;

    /** 学期ID */
    private Long semesterId;

    /** 考试类型：1期中2期末3补考 */
    private Integer examType;

    /** 考试日期 */
    private LocalDate examDate;

    /** 开始时间 */
    private LocalTime startTime;

    /** 结束时间 */
    private LocalTime endTime;

    /** 考场 */
    private String classroom;

    /** 座位号 */
    private String seatNo;

    /** 学生ID */
    private Long studentId;

    /** 学生姓名 */
    private String studentName;

    /** 状态：upcoming/ongoing/finished */
    private String status;
}
