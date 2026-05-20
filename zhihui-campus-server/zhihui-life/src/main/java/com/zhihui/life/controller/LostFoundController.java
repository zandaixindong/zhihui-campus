package com.zhihui.life.controller;

import com.zhihui.common.core.domain.PageResult;
import com.zhihui.common.core.domain.R;
import com.zhihui.life.entity.LostFound;
import com.zhihui.life.service.LostFoundService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 失物招领控制器
 */
@Tag(name = "失物招领管理", description = "失物招领发布、查询等接口")
@RestController
@RequestMapping("/life/lost-found")
@RequiredArgsConstructor
public class LostFoundController {

    private final LostFoundService lostFoundService;

    /**
     * 分页查询失物招领
     */
    @Operation(summary = "分页查询失物招领")
    @GetMapping("/list")
    public R<PageResult<LostFound>> list(
            @RequestParam(required = false) Integer type,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) Integer status,
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize) {
        return R.ok(lostFoundService.getLostFoundList(type, category, status, pageNum, pageSize));
    }

    /**
     * 根据ID查询详情
     */
    @Operation(summary = "根据ID查询详情")
    @GetMapping("/{id}")
    public R<LostFound> getInfo(@PathVariable Long id) {
        return R.ok(lostFoundService.getById(id));
    }

    /**
     * 发布失物招领
     */
    @Operation(summary = "发布失物招领")
    @PostMapping
    public R<Void> publish(@RequestBody LostFound lostFound) {
        lostFoundService.publishLostFound(lostFound);
        return R.ok();
    }

    /**
     * 标记已解决
     */
    @Operation(summary = "标记已解决")
    @PutMapping("/resolve/{id}")
    public R<Void> resolve(@PathVariable Long id) {
        lostFoundService.resolveLostFound(id);
        return R.ok();
    }
}
