package com.findme.mysteryweb.repository;

import com.findme.mysteryweb.domain.Notification;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class NotificationRepository {
    private final EntityManager em;

    public void save(Notification notification){
        em.persist(notification);
    }

    public Notification findOne(Long notificationId){
        return em.find(Notification.class, notificationId);
    }

    public List<Notification> findAllByReceiverId(Long receiverId){
        return em.createQuery("select n from Notification n where n.receiverId =: receiverId order by n.datetime desc", Notification.class)
                .setParameter("receiverId", receiverId)
                .getResultList();
    }

    public Notification findOneByReceiverId(Long receiverId){
        List<Notification> notificationList = em.createQuery("select n from Notification n where n.receiverId =:receiverId", Notification.class)
                .setParameter("receiverId", receiverId)
                .getResultList();

        return notificationList.isEmpty() ? null : notificationList.get(0);
    }


    public void delete(Long notificationId){
        em.remove(findOne(notificationId));
    }

    public void clearStore(){
        em.clear();
    }

}

