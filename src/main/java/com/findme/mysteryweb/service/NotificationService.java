package com.findme.mysteryweb.service;


import com.findme.mysteryweb.domain.Notification;
import com.findme.mysteryweb.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class NotificationService {
    private final NotificationRepository notificationRepository;
    private final Map<Long, SseEmitter> userEmitters = new ConcurrentHashMap<>();

    public void sendNotification(Notification notification) {
        SseEmitter emitter = userEmitters.get(notification.getReceiverId());
        log.error(emitter.toString());

        if (emitter != null) {
            try {
                emitter.send(SseEmitter.event().name("notification").data(notification));
            } catch (IOException e) {
                userEmitters.remove(notification.getReceiverId());
                // 로깅 또는 다른 오류 처리
            }
        }
    }

    public Notification findOneByReceiverId(Long receiverId){
        return notificationRepository.findOneByReceiverId(receiverId);
    }
    public void addEmitter(Long receiverId, SseEmitter emitter) {
        userEmitters.put(receiverId, emitter);
    }

    public void removeEmitter(Long receiverId) {
        userEmitters.remove(receiverId);
    }

    public void deleteNotification(Long notificationId){
        notificationRepository.delete(notificationId);
    }



}
