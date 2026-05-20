package com.zhihui.notice.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zhihui.common.core.domain.PageResult;
import com.zhihui.notice.entity.Message;

/**
 * 消息服务接口
 */
public interface MessageService extends IService<Message> {

    /**
     * 分页查询消息列表
     */
    PageResult<Message> getMessageList(Long userId, Integer msgType, Integer isRead,
                                        int pageNum, int pageSize);

    /**
     * 统计未读消息数量
     */
    int countUnread(Long userId);

    /**
     * 发送消息
     */
    void sendMessage(Message message);

    /**
     * 标记已读
     */
    void markAsRead(Long messageId);

    /**
     * 标记全部已读
     */
    void markAllAsRead(Long userId);
}
