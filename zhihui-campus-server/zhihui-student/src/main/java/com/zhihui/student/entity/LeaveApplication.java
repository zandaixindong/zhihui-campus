package com.zhihui.student.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 请假申请实体
 */
@Data
@TableName("student_leave_application")
public class LeaveApplication {

    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /** 学生ID */
    private Long studentId;

    /** 学生姓名 */
    private String studentName;

    /** 学院ID */
    private Long collegeId;

    /** 班级 */
    private String className;

    /** 请假类型（1事假2病假3其他） */
    private Integer leaveType;

    /** 开始日期 */
    private LocalDate startDate;

    /** 结束日期 */
    private LocalDate endDate;

    /** 请假天数 */
    private Integer days;

    /** 请假原因 */
    private String reason;

    /** 附件 */
    private String attachments;

    /** 联系电话 */
    private String contactPhone;

    /** 紧急联系人 */
    private String emergencyContact;

    /** 辅导员ID */
    private Long counselorId;

    /** 审批状态（0待审批1辅导员通过2辅导员拒绝3学院通过4学院拒绝） */
    private Integer status;

    /** 辅导员审批意见 */
    private String counselorRemark;

    /** 辅导员审批时间 */
    private LocalDateTime counselorApproveTime;

    /** 学院审批人ID */
    private Long collegeApproverId;

    /** 学院审批意见 */
    private String collegeRemark;

    /** 学院审批时间 */
    private LocalDateTime collegeApproveTime;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
