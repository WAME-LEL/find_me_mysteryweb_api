package com.findme.mysteryweb.repository;


import com.findme.mysteryweb.domain.Recommendation;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class RecommendationRepository {
    private final EntityManager em;

    public void save(Recommendation recommendation){
        em.persist(recommendation);
    }

    public Recommendation findOne(Long recommendationId){
        return em.find(Recommendation.class, recommendationId);
    }

    public Recommendation findOneByMemberIdAndOtherId(Long memberId, Long postId, Long bookId){
        List<Recommendation> recommendationList = em.createQuery("select r from Recommendation r where r.member.id =:memberId and " +
                        "(r.post.id =:postId and r.book.id is null) or (r.post.id is null and r.book.id =:bookId)", Recommendation.class)
                .setParameter("memberId", memberId)
                .setParameter("postId", postId)
                .setParameter("bookId", bookId)
                .getResultList();

        return recommendationList.isEmpty() ? null : recommendationList.get(0);
    }

    public List<Recommendation> findAll(){
        return em.createQuery("select r from Recommendation r", Recommendation.class)
                .getResultList();
    }

    public List<Recommendation> findAllByMemberId(Long memberId){
        List<Recommendation> recommendationList = em.createQuery("select r from Recommendation r where r.member.id =:memberId", Recommendation.class)
                .setParameter("memberId", memberId)
                .getResultList();

        return recommendationList.isEmpty() ? Collections.emptyList() : recommendationList;
    }

    public void delete(Long recommendationId){
        em.remove(em.find(Recommendation.class, recommendationId));
    }


}
