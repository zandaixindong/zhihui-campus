package com.zhihui.student.controller;

import com.zhihui.common.core.domain.PageResult;
import com.zhihui.common.core.domain.R;
import com.zhihui.student.entity.LeaveApplication;
import com.zhihui.student.service.LeaveService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 请假管理控制器
 */
@Tag(name = "请假管理", description = "请假申请、审批等接口")
@RestController
@RequestMapping("/student/leave")
@RequiredArgsConstructor
public class LeaveController {

    private final LeaveService leaveService;

    /**
     * 分页查询请假申请
     */
    @Operation(summary = "分页查询请假申请")
    @GetMapping("/list")
    public R<PageResult<LeaveApplication>> list(
            @RequestParam(required = false) Long studentId,
            @RequestParam(required = false) Integer status,
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize) {
        return R.ok(leaveService.getLeaveList(studentId, status, pageNum, pageSize));
    }

    /**
     * 查询请假详情
     */
    @Operation(summary = "查询请假详情")
    @GetMapping("/{leaveId}")
    public R<LeaveApplication> getInfo(@PathVariable Long leaveId) {
        return R.ok(leaveService.getById(leaveId));
    }

    /**
     * 提交请假申请
     */
    @Operation(summary = "提交请假申请")
    @PostMapping
    public R<Void> submit(@RequestBody LeaveApplication leave) {
        leaveService.submitLeave(leave);
        return R.ok();
    }

    /**
     * 辅导员审批
     */
    @Operation(summary = "辅导员审批")
    @PutMapping("/counselor/approve/{leaveId}")
    public R<Void> counselorApprove(@PathVariable Long leaveId,
                                    @RequestParam boolean approved,
                                    @RequestParam(required = false) String remark) {
        leaveService.counselorApprove(leaveId, approved, remark);
        return R.ok();
    }

    /**
     * 学院审批
     */
    @Operation(summary = "学院审批")
    @PutMapping("/college/approve/{leaveId}")
    public R<Void> collegeApprove(@PathVariable Long leaveId,
                                  @RequestParam Long approverId,
                                  @RequestParam boolean approved,
                                  @RequestParam(required = false) String remark) {
        leaveService.collegeApprove(leaveId, approverId, approved, remark);
        return R.ok();
    }

    /**
     * 取消请假申请
     */
    @Operation(summary = "取消请假申请")
    @DeleteMapping("/{leaveId}")
    public R<Void> cancel(@PathVariable Long leaveId) {
        leaveService.cancelLeave(leaveId);
        return R.ok();
    }
}
