package com.zhihui.notice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhihui.common.core.domain.PageResult;
import com.zhihui.notice.entity.Message;
import com.zhihui.notice.mapper.MessageMapper;
import com.zhihui.notice.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * 消息服务实现类
 */
@Service
@RequiredArgsConstructor
public class MessageServiceImpl extends ServiceImpl<MessageMapper, Message> implements MessageService {

    private final MessageMapper messageMapper;

    @Override
    public PageResult<Message> getMessageList(Long userId, Integer msgType, Integer isRead,
                                               int pageNum, int pageSize) {
        Page<Message> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<Message> wrapper = new LambdaQueryWrapper<>();

        wrapper.eq(Message::getUserId, userId);
        if (msgType != null) {
            wrapper.eq(Message::getMsgType, msgType);
        }
        if (isRead != null) {
            wrapper.eq(Message::getIsRead, isRead);
        }
        wrapper.orderByDesc(Message::getCreateTime);

        Page<Message> result = messageMapper.selectPage(page, wrapper);
        return new PageResult<>(result.getRecords(), result.getTotal(), pageNum, pageSize);
    }

    @Override
    public int countUnread(Long userId) {
        return messageMapper.countUnread(userId);
    }

    @Override
    @Transactional
    public void sendMessage(Message message) {
        message.setIsRead(0);
        message.setCreateTime(LocalDateTime.now());
        messageMapper.insert(message);

        // TODO: 通过WebSocket推送给用户
    }

    @Override
    @Transactional
    public void markAsRead(Long messageId) {
        Message message = new Message();
        message.setId(messageId);
        message.setIsRead(1);
        message.setReadTime(LocalDateTime.now());
        messageMapper.updateById(message);
    }

    @Override
    @Transactional
    public void markAllAsRead(Long userId) {
        LambdaUpdateWrapper<Message> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(Message::getUserId, userId)
                .eq(Message::getIsRead, 0)
                .set(Message::getIsRead, 1)
                .set(Message::getReadTime, LocalDateTime.now());
        messageMapper.update(null, wrapper);
    }
}
