package com.zhihui.student.controller;

import com.zhihui.common.core.domain.PageResult;
import com.zhihui.common.core.domain.R;
import com.zhihui.student.entity.WorkStudy;
import com.zhihui.student.entity.WorkStudyApplication;
import com.zhihui.student.service.WorkStudyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 勤工助学控制器
 */
@Tag(name = "勤工助学管理", description = "勤工助学岗位查询、申请等接口")
@RestController
@RequestMapping("/student/work-study")
@RequiredArgsConstructor
public class WorkStudyController {

    private final WorkStudyService workStudyService;

    /**
     * 分页查询岗位列表
     */
    @Operation(summary = "分页查询岗位列表")
    @GetMapping("/positions")
    public R<PageResult<WorkStudy>> positions(
            @RequestParam(required = false) String positionType,
            @RequestParam(required = false) Integer status,
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize) {
        return R.ok(workStudyService.getPositionList(positionType, status, pageNum, pageSize));
    }

    /**
     * 查询岗位详情
     */
    @Operation(summary = "查询岗位详情")
    @GetMapping("/positions/{positionId}")
    public R<WorkStudy> getPositionInfo(@PathVariable Long positionId) {
        return R.ok(workStudyService.getById(positionId));
    }

    /**
     * 分页查询申请记录
     */
    @Operation(summary = "分页查询申请记录")
    @GetMapping("/applications")
    public R<PageResult<WorkStudyApplication>> applications(
            @RequestParam(required = false) Long studentId,
            @RequestParam(required = false) Integer status,
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize) {
        return R.ok(workStudyService.getApplicationList(studentId, status, pageNum, pageSize));
    }

    /**
     * 提交岗位申请
     */
    @Operation(summary = "提交岗位申请")
    @PostMapping("/applications")
    public R<Void> submitApplication(@RequestBody WorkStudyApplication application) {
        workStudyService.submitApplication(application);
        return R.ok();
    }

    /**
     * 审批岗位申请
     */
    @Operation(summary = "审批岗位申请")
    @PutMapping("/applications/approve/{applicationId}")
    public R<Void> approveApplication(@PathVariable Long applicationId,
                                      @RequestParam boolean approved,
                                      @RequestParam(required = false) String remark) {
        workStudyService.approveApplication(applicationId, approved, remark);
        return R.ok();
    }
}
