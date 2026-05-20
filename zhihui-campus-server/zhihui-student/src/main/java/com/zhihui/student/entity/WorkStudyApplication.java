package com.zhihui.student.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 勤工助学申请实体
 */
@Data
@TableName("student_work_study_application")
public class WorkStudyApplication {

    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /** 岗位ID */
    private Long positionId;

    /** 学生ID */
    private Long studentId;

    /** 学生姓名 */
    private String studentName;

    /** 学院 */
    private String collegeName;

    /** 专业班级 */
    private String className;

    /** 联系电话 */
    private String phone;

    /** 申请理由 */
    private String reason;

    /** 相关经验 */
    private String experience;

    /** 审批状态（0待审批1已通过2已拒绝） */
    private Integer status;

    /** 审批人ID */
    private Long approverId;

    /** 审批意见 */
    private String approveRemark;

    /** 审批时间 */
    private LocalDateTime approveTime;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
