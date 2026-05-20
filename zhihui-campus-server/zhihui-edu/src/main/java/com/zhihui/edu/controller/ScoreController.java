package com.zhihui.edu.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhihui.common.core.domain.R;
import com.zhihui.edu.entity.Score;
import com.zhihui.edu.service.ScoreService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 成绩控制器
 */
@Tag(name = "成绩管理", description = "成绩查询接口")
@RestController
@RequestMapping("/edu/score")
@RequiredArgsConstructor
public class ScoreController {

    private final ScoreService scoreService;

    @Operation(summary = "分页查询成绩")
    @GetMapping("/list")
    public R<Page<Score>> getScoreList(
            @RequestParam Long studentId,
            @RequestParam(required = false) Long semesterId,
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "20") int pageSize) {
        return R.ok(scoreService.getScoreList(studentId, semesterId, pageNum, pageSize));
    }

    @Operation(summary = "获取GPA统计")
    @GetMapping("/gpa")
    public R<Map<String, Object>> getGpaStats(@RequestParam Long studentId) {
        return R.ok(scoreService.getGpaStats(studentId));
    }
}
