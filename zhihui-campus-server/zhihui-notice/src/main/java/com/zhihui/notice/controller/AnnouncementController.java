package com.zhihui.notice.controller;

import com.zhihui.common.core.domain.PageResult;
import com.zhihui.common.core.domain.R;
import com.zhihui.notice.entity.Announcement;
import com.zhihui.notice.service.AnnouncementService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 公告控制器
 */
@Tag(name = "公告管理", description = "公告发布、查询等接口")
@RestController
@RequestMapping("/notice/announcement")
@RequiredArgsConstructor
public class AnnouncementController {

    private final AnnouncementService announcementService;

    /**
     * 分页查询公告列表
     */
    @Operation(summary = "分页查询公告列表")
    @GetMapping("/list")
    public R<PageResult<Announcement>> list(
            @RequestParam(required = false) Integer noticeType,
            @RequestParam(required = false) Long collegeId,
            @RequestParam(required = false) Integer status,
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize) {
        return R.ok(announcementService.getAnnouncementList(noticeType, collegeId, status, pageNum, pageSize));
    }

    /**
     * 查询公告详情
     */
    @Operation(summary = "查询公告详情")
    @GetMapping("/{id}")
    public R<Announcement> getInfo(@PathVariable Long id) {
        return R.ok(announcementService.getAnnouncementDetail(id));
    }

    /**
     * 发布公告
     */
    @Operation(summary = "发布公告")
    @PostMapping
    public R<Void> publish(@RequestBody Announcement announcement) {
        announcementService.publishAnnouncement(announcement);
        return R.ok();
    }

    /**
     * 修改公告
     */
    @Operation(summary = "修改公告")
    @PutMapping
    public R<Void> update(@RequestBody Announcement announcement) {
        announcementService.updateAnnouncement(announcement);
        return R.ok();
    }

    /**
     * 删除公告
     */
    @Operation(summary = "删除公告")
    @DeleteMapping("/{id}")
    public R<Void> delete(@PathVariable Long id) {
        announcementService.deleteAnnouncement(id);
        return R.ok();
    }
}
