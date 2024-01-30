package com.findme.mysteryweb.repository;


import com.findme.mysteryweb.domain.CorrectAnswer;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class CorrectAnswerRepository {
    private final EntityManager em;

    public void save(CorrectAnswer correctAnswer){
        em.persist(correctAnswer);
    }

    public CorrectAnswer findOne(Long correctAnswerId){
        return em.find(CorrectAnswer.class, correctAnswerId);
    }

    public CorrectAnswer findOneByPostAndMember(Long postId, Long memberId){
        List<CorrectAnswer> resultList = em.createQuery("select ca from CorrectAnswer ca where ca.post.id =:postId and ca.member.id =:memberId", CorrectAnswer.class)
                .setParameter("postId", postId)
                .setParameter("memberId", memberId)
                .getResultList();

        return resultList.isEmpty() ? null : resultList.get(0);
    }

    public List<CorrectAnswer> findAllOrderByDatetime(Long memberId){
        return em.createQuery("select ca from CorrectAnswer ca where ca.member.id = :memberId order by ca.datetime", CorrectAnswer.class)
                .setParameter("memberId", memberId)
                .getResultList();
    }


    public List<CorrectAnswer> findAllByMember(Long memberId){
        return em.createQuery("select ca from CorrectAnswer ca where ca.member.id = :memberId", CorrectAnswer.class)
                .setParameter("memberId", memberId)
                .getResultList();
    }



}
