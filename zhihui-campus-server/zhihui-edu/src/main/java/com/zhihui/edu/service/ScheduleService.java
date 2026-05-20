package com.zhihui.edu.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zhihui.edu.entity.CourseOffer;
import com.zhihui.edu.entity.Semester;
import com.zhihui.edu.mapper.CourseOfferMapper;
import com.zhihui.edu.mapper.SemesterMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 课表服务
 */
@Service
@RequiredArgsConstructor
public class ScheduleService {

    private final CourseOfferMapper courseOfferMapper;
    private final SemesterMapper semesterMapper;
    private final RedisTemplate<String, Object> redisTemplate;

    /**
     * 获取当前学期课表
     */
    public List<CourseOffer> getCurrentSchedule(Long studentId) {
        String cacheKey = "edu:schedule:current:" + studentId;
        List<CourseOffer> cached = (List<CourseOffer>) redisTemplate.opsForValue().get(cacheKey);
        if (cached != null) {
            return cached;
        }

        Semester currentSemester = getCurrentSemester();
        if (currentSemester == null) {
            return List.of();
        }

        List<CourseOffer> schedule = courseOfferMapper.selectStudentSchedule(studentId, currentSemester.getId());
        redisTemplate.opsForValue().set(cacheKey, schedule, 30, TimeUnit.MINUTES);
        return schedule;
    }

    /**
     * 获取指定学期课表
     */
    public List<CourseOffer> getSemesterSchedule(Long studentId, Long semesterId) {
        return courseOfferMapper.selectStudentSchedule(studentId, semesterId);
    }

    /**
     * 获取当前学期
     */
    public Semester getCurrentSemester() {
        return semesterMapper.selectOne(
                new LambdaQueryWrapper<Semester>()
                        .eq(Semester::getIsCurrent, 1)
                        .eq(Semester::getStatus, 0)
                        .last("LIMIT 1")
        );
    }
}
