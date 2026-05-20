package com.zhihui.life.controller;

import com.zhihui.common.core.domain.PageResult;
import com.zhihui.common.core.domain.R;
import com.zhihui.life.entity.Venue;
import com.zhihui.life.entity.VenueBooking;
import com.zhihui.life.service.VenueService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

/**
 * 场地预约控制器
 */
@Tag(name = "场地预约管理", description = "场地查询、预约、审批等接口")
@RestController
@RequestMapping("/life/venue")
@RequiredArgsConstructor
public class VenueController {

    private final VenueService venueService;

    /**
     * 查询场地列表
     */
    @Operation(summary = "查询场地列表")
    @GetMapping("/list")
    public R<List<Venue>> list(@RequestParam(required = false) String venueType) {
        return R.ok(venueService.getVenueList(venueType));
    }

    /**
     * 查询场地详情
     */
    @Operation(summary = "查询场地详情")
    @GetMapping("/{venueId}")
    public R<Venue> getInfo(@PathVariable Long venueId) {
        return R.ok(venueService.getById(venueId));
    }

    /**
     * 查询场地可用时间段
     */
    @Operation(summary = "查询场地可用时间段")
    @GetMapping("/{venueId}/available")
    public R<List<VenueBooking>> getAvailableSlots(
            @PathVariable Long venueId,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date) {
        return R.ok(venueService.getAvailableSlots(venueId, date));
    }

    /**
     * 分页查询预约记录
     */
    @Operation(summary = "分页查询预约记录")
    @GetMapping("/booking/list")
    public R<PageResult<VenueBooking>> bookingList(
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false) Integer status,
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize) {
        return R.ok(venueService.getBookingList(userId, status, pageNum, pageSize));
    }

    /**
     * 提交预约申请
     */
    @Operation(summary = "提交预约申请")
    @PostMapping("/booking")
    public R<Void> submitBooking(@RequestBody VenueBooking booking) {
        venueService.submitBooking(booking);
        return R.ok();
    }

    /**
     * 审批预约
     */
    @Operation(summary = "审批预约")
    @PutMapping("/booking/approve/{bookingId}")
    public R<Void> approveBooking(@PathVariable Long bookingId,
                                  @RequestParam Long approverId,
                                  @RequestParam boolean approved,
                                  @RequestParam(required = false) String remark) {
        venueService.approveBooking(bookingId, approverId, approved, remark);
        return R.ok();
    }

    /**
     * 取消预约
     */
    @Operation(summary = "取消预约")
    @PutMapping("/booking/cancel/{bookingId}")
    public R<Void> cancelBooking(@PathVariable Long bookingId) {
        venueService.cancelBooking(bookingId);
        return R.ok();
    }
}
