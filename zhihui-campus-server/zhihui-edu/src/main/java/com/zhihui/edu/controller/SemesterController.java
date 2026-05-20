package com.zhihui.edu.controller;

import com.zhihui.common.core.domain.R;
import com.zhihui.edu.entity.Semester;
import com.zhihui.edu.service.SemesterService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 学期控制器
 */
@Tag(name = "学期管理", description = "学期查询接口")
@RestController
@RequestMapping("/edu/semester")
@RequiredArgsConstructor
public class SemesterController {

    private final SemesterService semesterService;

    @Operation(summary = "获取所有学期")
    @GetMapping("/list")
    public R<List<Semester>> getSemesterList() {
        return R.ok(semesterService.getAllSemesters());
    }

    @Operation(summary = "获取当前学期")
    @GetMapping("/current")
    public R<Semester> getCurrentSemester() {
        return R.ok(semesterService.getCurrentSemester());
    }
}
