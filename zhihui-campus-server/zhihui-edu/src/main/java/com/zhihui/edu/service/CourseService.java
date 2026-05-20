package com.zhihui.edu.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zhihui.edu.entity.CourseOffer;
import com.zhihui.edu.entity.StudentCourse;
import com.zhihui.edu.mapper.CourseOfferMapper;
import com.zhihui.edu.mapper.StudentCourseMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 课程服务
 */
@Service
@RequiredArgsConstructor
public class CourseService {

    private final StudentCourseMapper studentCourseMapper;
    private final CourseOfferMapper courseOfferMapper;

    /**
     * 查询已选课程
     */
    public List<CourseOffer> getSelectedCourses(Long studentId, Long semesterId) {
        return courseOfferMapper.selectStudentSchedule(studentId, semesterId);
    }

    /**
     * 选课
     */
    @Transactional
    public void selectCourse(Long studentId, String studentName, Long courseOfferId) {
        // 检查是否已选
        Long count = studentCourseMapper.selectCount(
                new LambdaQueryWrapper<StudentCourse>()
                        .eq(StudentCourse::getStudentId, studentId)
                        .eq(StudentCourse::getCourseOfferId, courseOfferId)
                        .eq(StudentCourse::getStatus, 0)
        );
        if (count > 0) {
            throw new RuntimeException("已选该课程");
        }

        // 检查容量
        CourseOffer offer = courseOfferMapper.selectById(courseOfferId);
        if (offer == null || offer.getSelectedCount() >= offer.getCapacity()) {
            throw new RuntimeException("课程已满");
        }

        // 选课
        StudentCourse sc = new StudentCourse();
        sc.setStudentId(studentId);
        sc.setStudentName(studentName);
        sc.setCourseOfferId(courseOfferId);
        sc.setSemesterId(offer.getSemesterId());
        sc.setStatus(0);
        studentCourseMapper.insert(sc);

        // 更新已选人数
        offer.setSelectedCount(offer.getSelectedCount() + 1);
        courseOfferMapper.updateById(offer);
    }

    /**
     * 退课
     */
    @Transactional
    public void dropCourse(Long studentId, Long courseOfferId) {
        StudentCourse sc = studentCourseMapper.selectOne(
                new LambdaQueryWrapper<StudentCourse>()
                        .eq(StudentCourse::getStudentId, studentId)
                        .eq(StudentCourse::getCourseOfferId, courseOfferId)
                        .eq(StudentCourse::getStatus, 0)
        );
        if (sc == null) {
            throw new RuntimeException("未选该课程");
        }

        sc.setStatus(1);
        studentCourseMapper.updateById(sc);

        CourseOffer offer = courseOfferMapper.selectById(courseOfferId);
        if (offer != null && offer.getSelectedCount() > 0) {
            offer.setSelectedCount(offer.getSelectedCount() - 1);
            courseOfferMapper.updateById(offer);
        }
    }
}
