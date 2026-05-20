package com.zhihui.student.controller;

import com.zhihui.common.core.domain.PageResult;
import com.zhihui.common.core.domain.R;
import com.zhihui.student.entity.RewardPunishment;
import com.zhihui.student.service.RewardPunishmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 奖惩管理控制器
 */
@Tag(name = "奖惩管理", description = "奖惩记录查询、录入等接口")
@RestController
@RequestMapping("/student/reward-punishment")
@RequiredArgsConstructor
public class RewardPunishmentController {

    private final RewardPunishmentService recordService;

    /**
     * 分页查询奖惩记录
     */
    @Operation(summary = "分页查询奖惩记录")
    @GetMapping("/list")
    public R<PageResult<RewardPunishment>> list(
            @RequestParam(required = false) Long studentId,
            @RequestParam(required = false) Integer type,
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize) {
        return R.ok(recordService.getRecordList(studentId, type, pageNum, pageSize));
    }

    /**
     * 查询详情
     */
    @Operation(summary = "查询详情")
    @GetMapping("/{id}")
    public R<RewardPunishment> getInfo(@PathVariable Long id) {
        return R.ok(recordService.getById(id));
    }

    /**
     * 添加奖惩记录
     */
    @Operation(summary = "添加奖惩记录")
    @PostMapping
    public R<Void> add(@RequestBody RewardPunishment record) {
        recordService.addRecord(record);
        return R.ok();
    }

    /**
     * 修改奖惩记录
     */
    @Operation(summary = "修改奖惩记录")
    @PutMapping
    public R<Void> update(@RequestBody RewardPunishment record) {
        recordService.updateRecord(record);
        return R.ok();
    }

    /**
     * 删除奖惩记录
     */
    @Operation(summary = "删除奖惩记录")
    @DeleteMapping("/{id}")
    public R<Void> delete(@PathVariable Long id) {
        recordService.deleteRecord(id);
        return R.ok();
    }
}
