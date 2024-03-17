package com.findme.mysteryweb.domain;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter @Setter
public class Notification {
    @Id
    @GeneratedValue
    @Column(name = "notification_id")
    private Long id;


    private Long senderId;

    private Long receiverId;

    private Long postId;

    private String message;

    private LocalDateTime datetime;


    //==생성 메서드==//

    public static Notification createNotification(Long senderId, Long receiverId, Long postId, String message){
        Notification notification = new Notification();

        notification.setSenderId(senderId);
        notification.setReceiverId(receiverId);
        notification.setPostId(postId);
        notification.setMessage(message);
        notification.setDatetime(LocalDateTime.now());


        return notification;
    }


}
