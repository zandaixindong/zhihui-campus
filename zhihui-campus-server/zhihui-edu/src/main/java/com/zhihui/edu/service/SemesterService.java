package com.zhihui.edu.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zhihui.edu.entity.Semester;
import com.zhihui.edu.mapper.SemesterMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 学期服务
 */
@Service
@RequiredArgsConstructor
public class SemesterService {

    private final SemesterMapper semesterMapper;

    /**
     * 获取所有学期
     */
    public List<Semester> getAllSemesters() {
        return semesterMapper.selectList(
                new LambdaQueryWrapper<Semester>()
                        .eq(Semester::getStatus, 0)
                        .orderByDesc(Semester::getStartDate)
        );
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
