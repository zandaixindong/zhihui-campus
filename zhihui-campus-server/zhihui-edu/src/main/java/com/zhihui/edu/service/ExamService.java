package com.zhihui.edu.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zhihui.edu.entity.Exam;
import com.zhihui.edu.mapper.ExamMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 考试服务
 */
@Service
@RequiredArgsConstructor
public class ExamService {

    private final ExamMapper examMapper;

    /**
     * 查询考试安排
     */
    public List<Exam> getExamList(Long studentId, Long semesterId) {
        LambdaQueryWrapper<Exam> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Exam::getStudentId, studentId)
                .orderByAsc(Exam::getExamDate)
                .orderByAsc(Exam::getStartTime);

        if (semesterId != null) {
            wrapper.eq(Exam::getSemesterId, semesterId);
        }

        return examMapper.selectList(wrapper);
    }
}
