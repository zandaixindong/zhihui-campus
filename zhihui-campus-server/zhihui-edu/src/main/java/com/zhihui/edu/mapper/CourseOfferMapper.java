package com.zhihui.edu.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhihui.edu.entity.CourseOffer;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface CourseOfferMapper extends BaseMapper<CourseOffer> {

    /**
     * 查询学生课表
     */
    @Select("SELECT co.*, c.course_name, c.course_code, c.credits " +
            "FROM edu_course_offer co " +
            "LEFT JOIN edu_course c ON co.course_id = c.id " +
            "INNER JOIN edu_student_course sc ON co.id = sc.course_offer_id " +
            "WHERE sc.student_id = #{studentId} AND sc.status = 0 AND co.semester_id = #{semesterId}")
    List<CourseOffer> selectStudentSchedule(@Param("studentId") Long studentId, @Param("semesterId") Long semesterId);
}
