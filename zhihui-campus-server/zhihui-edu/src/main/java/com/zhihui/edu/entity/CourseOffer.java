package com.zhihui.edu.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.zhihui.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 开课信息（教学班）
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("edu_course_offer")
public class CourseOffer extends BaseEntity {

    @TableId(type = IdType.AUTO)
    private Long id;

    /** 课程ID */
    private Long courseId;

    /** 学期ID */
    private Long semesterId;

    /** 教师ID */
    private Long teacherId;

    /** 教师姓名 */
    private String teacherName;

    /** 上课教室 */
    private String classroom;

    /** 星期几(1-7) */
    private Integer dayOfWeek;

    /** 节次ID */
    private Integer sectionId;

    /** 周次范围，如 1-16 */
    private String weekRange;

    /** 容量 */
    private Integer capacity;

    /** 已选人数 */
    private Integer selectedCount;

    /** 状态：0正常1已满2停开 */
    private Integer status;

    /** 课程信息（非数据库字段） */
    @TableField(exist = false)
    private Course course;
}
