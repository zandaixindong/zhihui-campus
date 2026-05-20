package com.zhihui.system.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zhihui.common.core.domain.R;
import com.zhihui.system.entity.College;
import com.zhihui.system.entity.Major;
import com.zhihui.system.mapper.CollegeMapper;
import com.zhihui.system.mapper.MajorMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 学院管理控制器
 */
@Tag(name = "学院管理", description = "学院和专业查询接口")
@RestController
@RequestMapping("/system/college")
@RequiredArgsConstructor
public class CollegeController {

    private final CollegeMapper collegeMapper;
    private final MajorMapper majorMapper;

    /**
     * 查询所有学院列表
     */
    @Operation(summary = "查询所有学院列表")
    @GetMapping("/list")
    public R<List<College>> list() {
        List<College> colleges = collegeMapper.selectList(
                new LambdaQueryWrapper<College>()
                        .eq(College::getStatus, 1)
                        .orderByAsc(College::getSortOrder)
        );
        return R.ok(colleges);
    }

    /**
     * 根据学院ID查询学院信息
     */
    @Operation(summary = "根据学院ID查询学院信息")
    @GetMapping("/{collegeId}")
    public R<College> getInfo(@PathVariable Long collegeId) {
        return R.ok(collegeMapper.selectById(collegeId));
    }

    /**
     * 新增学院
     */
    @Operation(summary = "新增学院")
    @PostMapping
    public R<Void> add(@RequestBody College college) {
        return collegeMapper.insert(college) > 0 ? R.ok() : R.fail();
    }

    /**
     * 修改学院
     */
    @Operation(summary = "修改学院")
    @PutMapping
    public R<Void> edit(@RequestBody College college) {
        return collegeMapper.updateById(college) > 0 ? R.ok() : R.fail();
    }

    /**
     * 删除学院
     */
    @Operation(summary = "删除学院")
    @DeleteMapping("/{collegeId}")
    public R<Void> remove(@PathVariable Long collegeId) {
        return collegeMapper.deleteById(collegeId) > 0 ? R.ok() : R.fail();
    }

    /**
     * 根据学院ID查询专业列表
     */
    @Operation(summary = "根据学院ID查询专业列表")
    @GetMapping("/{collegeId}/majors")
    public R<List<Major>> listMajors(@PathVariable Long collegeId) {
        return R.ok(majorMapper.selectByCollegeId(collegeId));
    }
}
