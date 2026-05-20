package com.zhihui.edu.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.zhihui.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

/**
 * 学期实体
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("edu_semester")
public class Semester extends BaseEntity {

    @TableId(type = IdType.AUTO)
    private Long id;

    /** 学期名称，如 2024-2025学年第一学期 */
    private String name;

    /** 学年，如 2024-2025 */
    private String academicYear;

    /** 学期：1第一学期2第二学期 */
    private Integer term;

    /** 开始日期 */
    private LocalDate startDate;

    /** 结束日期 */
    private LocalDate endDate;

    /** 是否当前学期：0否1是 */
    private Integer isCurrent;

    /** 状态：0正常1停用 */
    private Integer status;
}
