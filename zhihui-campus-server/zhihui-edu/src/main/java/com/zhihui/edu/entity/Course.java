package com.zhihui.edu.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.zhihui.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * 课程实体
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("edu_course")
public class Course extends BaseEntity {

    @TableId(type = IdType.AUTO)
    private Long id;

    /** 课程代码 */
    private String courseCode;

    /** 课程名称 */
    private String courseName;

    /** 英文名称 */
    private String englishName;

    /** 学分 */
    private BigDecimal credits;

    /** 总学时 */
    private Integer totalHours;

    /** 课程类型：1必修2选修3公选 */
    private Integer courseType;

    /** 所属学院ID */
    private Long collegeId;

    /** 课程简介 */
    private String description;

    /** 状态：0正常1停开 */
    private Integer status;
}
