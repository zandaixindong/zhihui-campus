package com.zhihui.student.controller;

import com.zhihui.common.core.domain.PageResult;
import com.zhihui.common.core.domain.R;
import com.zhihui.student.entity.CounselingAppointment;
import com.zhihui.student.service.CounselingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 心理咨询控制器
 */
@Tag(name = "心理咨询管理", description = "心理咨询预约、查询等接口")
@RestController
@RequestMapping("/student/counseling")
@RequiredArgsConstructor
public class CounselingController {

    private final CounselingService counselingService;

    /**
     * 分页查询预约记录
     */
    @Operation(summary = "分页查询预约记录")
    @GetMapping("/appointments")
    public R<PageResult<CounselingAppointment>> appointments(
            @RequestParam(required = false) Long studentId,
            @RequestParam(required = false) Integer status,
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize) {
        return R.ok(counselingService.getAppointmentList(studentId, status, pageNum, pageSize));
    }

    /**
     * 查询可用时间段
     */
    @Operation(summary = "查询可用时间段")
    @GetMapping("/available-slots")
    public R<List<CounselingAppointment>> availableSlots(
            @RequestParam Long counselorId,
            @RequestParam String date) {
        return R.ok(counselingService.getAvailableSlots(counselorId, date));
    }

    /**
     * 提交预约申请
     */
    @Operation(summary = "提交预约申请")
    @PostMapping("/appointments")
    public R<Void> submitAppointment(@RequestBody CounselingAppointment appointment) {
        counselingService.submitAppointment(appointment);
        return R.ok();
    }

    /**
     * 确认预约
     */
    @Operation(summary = "确认预约")
    @PutMapping("/appointments/confirm/{appointmentId}")
    public R<Void> confirmAppointment(@PathVariable Long appointmentId,
                                      @RequestParam(required = false) String remark) {
        counselingService.confirmAppointment(appointmentId, remark);
        return R.ok();
    }

    /**
     * 取消预约
     */
    @Operation(summary = "取消预约")
    @PutMapping("/appointments/cancel/{appointmentId}")
    public R<Void> cancelAppointment(@PathVariable Long appointmentId) {
        counselingService.cancelAppointment(appointmentId);
        return R.ok();
    }

    /**
     * 完成咨询
     */
    @Operation(summary = "完成咨询")
    @PutMapping("/appointments/complete/{appointmentId}")
    public R<Void> completeAppointment(@PathVariable Long appointmentId,
                                       @RequestParam(required = false) String record) {
        counselingService.completeAppointment(appointmentId, record);
        return R.ok();
    }
}
