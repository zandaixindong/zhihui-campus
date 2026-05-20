package com.zhihui.edu.controller;

import com.zhihui.common.core.domain.R;
import com.zhihui.edu.entity.CourseOffer;
import com.zhihui.edu.service.CourseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 课程控制器
 */
@Tag(name = "课程管理", description = "选课、已选课程查询接口")
@RestController
@RequestMapping("/edu/course")
@RequiredArgsConstructor
public class CourseController {

    private final CourseService courseService;

    @Operation(summary = "查询已选课程")
    @GetMapping("/selected")
    public R<List<CourseOffer>> getSelectedCourses(
            @RequestParam Long studentId,
            @RequestParam(required = false) Long semesterId) {
        return R.ok(courseService.getSelectedCourses(studentId, semesterId));
    }

    @Operation(summary = "选课")
    @PostMapping("/select")
    public R<Void> selectCourse(
            @RequestParam Long studentId,
            @RequestParam String studentName,
            @RequestParam Long courseOfferId) {
        courseService.selectCourse(studentId, studentName, courseOfferId);
        return R.ok();
    }

    @Operation(summary = "退课")
    @PostMapping("/drop")
    public R<Void> dropCourse(
            @RequestParam Long studentId,
            @RequestParam Long courseOfferId) {
        courseService.dropCourse(studentId, courseOfferId);
        return R.ok();
    }
}
