package com.zhihui.edu.controller;

import com.zhihui.common.core.domain.R;
import com.zhihui.edu.entity.Exam;
import com.zhihui.edu.service.ExamService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 考试控制器
 */
@Tag(name = "考试管理", description = "考试安排查询接口")
@RestController
@RequestMapping("/edu/exam")
@RequiredArgsConstructor
public class ExamController {

    private final ExamService examService;

    @Operation(summary = "查询考试安排")
    @GetMapping("/list")
    public R<List<Exam>> getExamList(
            @RequestParam Long studentId,
            @RequestParam(required = false) Long semesterId) {
        return R.ok(examService.getExamList(studentId, semesterId));
    }
}
