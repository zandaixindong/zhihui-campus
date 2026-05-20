package com.zhihui.edu.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhihui.edu.entity.Score;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.math.BigDecimal;
import java.util.Map;

@Mapper
public interface ScoreMapper extends BaseMapper<Score> {

    /**
     * 计算学生GPA
     */
    @Select("SELECT " +
            "ROUND(SUM(score * credits) / SUM(credits), 2) as gpa, " +
            "ROUND(AVG(score), 2) as avgScore, " +
            "SUM(credits) as totalCredits, " +
            "COUNT(*) as courseCount " +
            "FROM edu_score WHERE student_id = #{studentId} AND status = 0")
    Map<String, Object> selectGpaStats(@Param("studentId") Long studentId);
}
