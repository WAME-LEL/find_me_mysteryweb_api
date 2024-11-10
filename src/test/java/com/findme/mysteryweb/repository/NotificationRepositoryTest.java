package com.findme.mysteryweb.Repository;

import com.findme.mysteryweb.domain.Notification;
import com.findme.mysteryweb.repository.NotificationRepository;
import jakarta.transaction.Transactional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
class NotificationRepositoryTest {
    @Autowired
    NotificationRepository notificationRepository;


    @AfterEach
    void clearStore(){
        notificationRepository.clearStore();
    }

    @Test
    void save() {
        //given
        Notification notification = Notification.createNotification(1L, 1L, 1L, "test");

        //when
        notificationRepository.save(notification);

        //then
        Notification findNotification = notificationRepository.findOne(notification.getId());

        assertThat(findNotification).isEqualTo(notification);
    }

    @Test
    void findOne() {
        //given
        Notification notification = Notification.createNotification(1L, 1L, 1L, "test");

        //when
        notificationRepository.save(notification);

        //then
        Notification findNotification = notificationRepository.findOne(notification.getId());

        assertThat(findNotification).isEqualTo(notification);
    }

    @Test
    void findAllByReceiverId() {
        //given
        Notification notification1 = Notification.createNotification(1L, 1L, 1L, "test");
        Notification notification2 = Notification.createNotification(1L, 1L, 1L, "test");

        //when
        notificationRepository.save(notification1);
        notificationRepository.save(notification2);

        //then
        List<Notification> allByReceiverId = notificationRepository.findAllByReceiverId(1L);

        assertThat(allByReceiverId.size()).isEqualTo(2);
    }

    @Test
    void findOneByReceiverId() {
        //given
        Notification notification1 = Notification.createNotification(1L, 1L, 1L, "test");
        Notification notification2 = Notification.createNotification(1L, 1L, 1L, "test");

        //when
        notificationRepository.save(notification1);
        notificationRepository.save(notification2);

        //then
        Notification findNotification = notificationRepository.findOneByReceiverId(1L);
        assertThat(findNotification).isEqualTo(notification1);
    }

    @Test
    void delete() {
        //given
        Notification notification = Notification.createNotification(1L, 1L, 1L, "test");

        //when
        notificationRepository.save(notification);
        notificationRepository.delete(notification.getId());

        //then
        Notification findNotification = notificationRepository.findOne(notification.getId());
        assertThat(findNotification).isNull();
    }
}