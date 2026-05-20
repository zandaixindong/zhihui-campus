package com.zhihui.student.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * 心理咨询预约实体
 */
@Data
@TableName("student_counseling_appointment")
public class CounselingAppointment {

    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /** 学生ID */
    private Long studentId;

    /** 学生姓名 */
    private String studentName;

    /** 联系电话 */
    private String phone;

    /** 咨询师ID */
    private Long counselorId;

    /** 咨询师姓名 */
    private String counselorName;

    /** 预约日期 */
    private LocalDate appointmentDate;

    /** 预约时间 */
    private LocalTime appointmentTime;

    /** 咨询类型（1学业压力2人际关系3情感问题4职业规划5其他） */
    private Integer counselingType;

    /** 问题描述 */
    private String description;

    /** 是否紧急（0否1是） */
    private Integer isUrgent;

    /** 审批状态（0待确认1已确认2已取消3已完成） */
    private Integer status;

    /** 咨询师备注 */
    private String counselorRemark;

    /** 咨询记录 */
    private String counselingRecord;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
