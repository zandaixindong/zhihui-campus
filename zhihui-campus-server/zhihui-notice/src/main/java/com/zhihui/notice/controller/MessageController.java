package com.zhihui.notice.controller;

import com.zhihui.common.core.domain.PageResult;
import com.zhihui.common.core.domain.R;
import com.zhihui.notice.entity.Message;
import com.zhihui.notice.service.MessageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 消息控制器
 */
@Tag(name = "消息管理", description = "消息查询、已读标记等接口")
@RestController
@RequestMapping("/notice/message")
@RequiredArgsConstructor
public class MessageController {

    private final MessageService messageService;

    /**
     * 分页查询消息列表
     */
    @Operation(summary = "分页查询消息列表")
    @GetMapping("/list")
    public R<PageResult<Message>> list(
            @RequestParam Long userId,
            @RequestParam(required = false) Integer msgType,
            @RequestParam(required = false) Integer isRead,
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize) {
        return R.ok(messageService.getMessageList(userId, msgType, isRead, pageNum, pageSize));
    }

    /**
     * 统计未读消息数量
     */
    @Operation(summary = "统计未读消息数量")
    @GetMapping("/unread/count")
    public R<Integer> countUnread(@RequestParam Long userId) {
        return R.ok(messageService.countUnread(userId));
    }

    /**
     * 发送消息
     */
    @Operation(summary = "发送消息")
    @PostMapping
    public R<Void> send(@RequestBody Message message) {
        messageService.sendMessage(message);
        return R.ok();
    }

    /**
     * 标记已读
     */
    @Operation(summary = "标记已读")
    @PutMapping("/read/{messageId}")
    public R<Void> markAsRead(@PathVariable Long messageId) {
        messageService.markAsRead(messageId);
        return R.ok();
    }

    /**
     * 标记全部已读
     */
    @Operation(summary = "标记全部已读")
    @PutMapping("/read/all")
    public R<Void> markAllAsRead(@RequestParam Long userId) {
        messageService.markAllAsRead(userId);
        return R.ok();
    }
}
