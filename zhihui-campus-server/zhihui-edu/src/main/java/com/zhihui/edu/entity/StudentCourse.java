package com.zhihui.edu.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.zhihui.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 学生选课记录
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("edu_student_course")
public class StudentCourse extends BaseEntity {

    @TableId(type = IdType.AUTO)
    private Long id;

    /** 学生ID */
    private Long studentId;

    /** 学生姓名 */
    private String studentName;

    /** 开课ID */
    private Long courseOfferId;

    /** 学期ID */
    private Long semesterId;

    /** 选课状态：0已选1已退 */
    private Integer status;
}
