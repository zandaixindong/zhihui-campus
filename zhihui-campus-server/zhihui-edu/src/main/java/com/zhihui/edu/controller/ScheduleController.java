package com.zhihui.edu.controller;

import com.zhihui.common.core.domain.R;
import com.zhihui.edu.entity.CourseOffer;
import com.zhihui.edu.service.ScheduleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 课表控制器
 */
@Tag(name = "课表管理", description = "课表查询接口")
@RestController
@RequestMapping("/edu/schedule")
@RequiredArgsConstructor
public class ScheduleController {

    private final ScheduleService scheduleService;

    @Operation(summary = "获取当前学期课表")
    @GetMapping("/current")
    public R<List<CourseOffer>> getCurrentSchedule(@RequestParam Long studentId) {
        return R.ok(scheduleService.getCurrentSchedule(studentId));
    }

    @Operation(summary = "获取指定学期课表")
    @GetMapping("/semester/{semesterId}")
    public R<List<CourseOffer>> getSemesterSchedule(
            @RequestParam Long studentId,
            @PathVariable Long semesterId) {
        return R.ok(scheduleService.getSemesterSchedule(studentId, semesterId));
    }
}
