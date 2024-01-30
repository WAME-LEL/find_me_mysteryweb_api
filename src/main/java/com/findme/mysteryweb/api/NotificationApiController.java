package com.findme.mysteryweb.api;


import com.findme.mysteryweb.service.NotificationService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = {"https://detectivesnight.com", "https://www.detectivesnight.com", "http://detectivesnight.com", "http://www.detectivesnight.com"})
public class NotificationApiController {
    private final NotificationService notificationService;

    @GetMapping("/api/notifications/subscribe")
    public SseEmitter subscribe(@RequestParam Long receiverId) {
        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);

        notificationService.addEmitter(receiverId, emitter);

        emitter.onCompletion(() -> notificationService.removeEmitter(receiverId));
        emitter.onTimeout(() -> notificationService.removeEmitter(receiverId));
        emitter.onError((e) -> notificationService.removeEmitter(receiverId));

        return emitter;
    }

    @DeleteMapping("/api/notification")
    public ResponseEntity<?> deleteNotification(@ModelAttribute DeleteNotificationRequest request){
        notificationService.deleteNotification(request.notificationId);

        return ResponseEntity.ok("delete notification complete");
    }


    //==DTO==//
    @Data
    static class DeleteNotificationRequest{
        private Long notificationId;
    }
}
