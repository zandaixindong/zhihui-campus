package com.zhihui.edu.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.zhihui.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * 成绩实体
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("edu_score")
public class Score extends BaseEntity {

    @TableId(type = IdType.AUTO)
    private Long id;

    /** 学生ID */
    private Long studentId;

    /** 学生姓名 */
    private String studentName;

    /** 开课ID */
    private Long courseOfferId;

    /** 课程代码 */
    private String courseCode;

    /** 课程名称 */
    private String courseName;

    /** 学分 */
    private BigDecimal credits;

    /** 成绩 */
    private BigDecimal score;

    /** 绩点 */
    private BigDecimal gradePoint;

    /** 考试类型：1正常2补考3重修 */
    private Integer examType;

    /** 学期 */
    private String semester;

    /** 状态：0有效1无效 */
    private Integer status;
}
