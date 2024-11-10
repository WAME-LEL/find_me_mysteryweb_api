package com.findme.mysteryweb.repository;


import com.findme.mysteryweb.domain.CorrectAnswerHistory;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class CorrectAnswerHistoryRepository {
    private final EntityManager em;

    public void save(CorrectAnswerHistory correctAnswerHistory) {
        em.persist(correctAnswerHistory);
    }

    public CorrectAnswerHistory findOne(Long memberId, Long correctAnswerHistoryId) {
        return em.createQuery("select cah from CorrectAnswerHistory cah where cah.member.id =:memberId and cah.id =:correctAnswerHistoryId", CorrectAnswerHistory.class)
                .setParameter("memberId", memberId)
                .setParameter("correctAnswerHistoryId", correctAnswerHistoryId)
                .getSingleResult();
    }

    public List<CorrectAnswerHistory> findAll() {
        return em.createQuery("select cah from CorrectAnswerHistory cah", CorrectAnswerHistory.class)
                .getResultList();
    }


}
