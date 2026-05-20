package com.zhihui.life.controller;

import com.zhihui.common.core.domain.PageResult;
import com.zhihui.common.core.domain.R;
import com.zhihui.life.entity.RepairOrder;
import com.zhihui.life.service.RepairService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 报修控制器
 */
@Tag(name = "报修管理", description = "报修工单提交、查询、处理等接口")
@RestController
@RequestMapping("/life/repair")
@RequiredArgsConstructor
public class RepairController {

    private final RepairService repairService;

    /**
     * 分页查询报修工单
     */
    @Operation(summary = "分页查询报修工单")
    @GetMapping("/list")
    public R<PageResult<RepairOrder>> list(
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false) Integer status,
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize) {
        return R.ok(repairService.getRepairList(userId, status, pageNum, pageSize));
    }

    /**
     * 根据ID查询工单详情
     */
    @Operation(summary = "根据ID查询工单详情")
    @GetMapping("/{orderId}")
    public R<RepairOrder> getInfo(@PathVariable Long orderId) {
        return R.ok(repairService.getById(orderId));
    }

    /**
     * 提交报修工单
     */
    @Operation(summary = "提交报修工单")
    @PostMapping
    public R<Void> submit(@RequestBody RepairOrder order) {
        repairService.submitRepair(order);
        return R.ok();
    }

    /**
     * 处理报修工单
     */
    @Operation(summary = "处理报修工单")
    @PutMapping("/handle/{orderId}")
    public R<Void> handle(@PathVariable Long orderId,
                          @RequestParam Long handlerId,
                          @RequestParam String remark) {
        repairService.handleRepair(orderId, handlerId, remark);
        return R.ok();
    }

    /**
     * 完成报修工单
     */
    @Operation(summary = "完成报修工单")
    @PutMapping("/complete/{orderId}")
    public R<Void> complete(@PathVariable Long orderId) {
        repairService.completeRepair(orderId);
        return R.ok();
    }

    /**
     * 取消报修工单
     */
    @Operation(summary = "取消报修工单")
    @PutMapping("/cancel/{orderId}")
    public R<Void> cancel(@PathVariable Long orderId) {
        repairService.cancelRepair(orderId);
        return R.ok();
    }
}
