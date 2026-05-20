package com.zhihui.notice.websocket;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * WebSocket消息推送服务
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class WebSocketService {

    private final SimpMessagingTemplate messagingTemplate;

    /**
     * 发送消息给指定用户
     */
    public void sendToUser(Long userId, String destination, Object message) {
        try {
            messagingTemplate.convertAndSendToUser(
                    userId.toString(),
                    destination,
                    message
            );
            log.info("发送消息给用户: {}, 目的地: {}", userId, destination);
        } catch (Exception e) {
            log.error("发送WebSocket消息失败", e);
        }
    }

    /**
     * 发送消息给所有订阅者
     */
    public void sendToAll(String destination, Object message) {
        try {
            messagingTemplate.convertAndSend(destination, message);
            log.info("发送广播消息, 目的地: {}", destination);
        } catch (Exception e) {
            log.error("发送WebSocket广播消息失败", e);
        }
    }

    /**
     * 发送通知给用户
     */
    public void sendNotification(Long userId, String title, String content) {
        Map<String, Object> notification = Map.of(
                "title", title,
                "content", content,
                "timestamp", System.currentTimeMillis()
        );
        sendToUser(userId, "/notification", notification);
    }
}
