package com.findme.mysteryweb.api;


import com.findme.mysteryweb.domain.Member;
import com.findme.mysteryweb.domain.Notification;
import com.findme.mysteryweb.service.MemberService;
import com.findme.mysteryweb.service.NotificationService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = {"https://detectivesnight.com", "https://www.detectivesnight.com", "http://detectivesnight.com", "http://www.detectivesnight.com"})
public class NotificationApiController {
    private final NotificationService notificationService;
    private final MemberService memberService;

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

    @GetMapping("/api/notifications")
    public ResponseEntity<?> getNotifications(@AuthenticationPrincipal UserDetails userDetails, @ModelAttribute DeleteNotificationRequest request){
        Member member = memberService.findOneByUsername(userDetails.getUsername());

        List<Notification> notificationList = notificationService.findAllByReceiverId(member.getId());

        return ResponseEntity.ok(new Result<>(notificationList));
    }



    //==DTO==//

    @Data
    @AllArgsConstructor
    static class Result<T>{
        private T data;
    }
    @Data
    static class DeleteNotificationRequest{
        private Long notificationId;
    }
}
