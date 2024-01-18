package com.findme.mysteryweb.repository;


import com.findme.mysteryweb.domain.Comment;
import com.findme.mysteryweb.domain.Review;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ReviewRepository {
    private final EntityManager em;


    public void save(Review review) {
        em.persist(review);
    }

    public Review findOne(Long reviewId){
        return em.find(Review.class, reviewId);
    }

    public List<Review> findAll(){
        return em.createQuery("select r from Review r order by r.datetime desc", Review.class)
                .getResultList();
    }

    public List<Review> findAllByBookId(Long bookId) {
        return em.createQuery("select r from Review r where r.book.id =:bookId order by r.datetime desc", Review.class)
                .setParameter("bookId", bookId)
                .getResultList();
    }

    public void delete(Long reviewId){
        em.remove(em.find(Review.class, reviewId));
    }


    public List<Review> findAllTopLevelCommentsByBookId(Long bookId) {
        return em.createQuery("SELECT r FROM Review r WHERE r.book.id = :bookId AND r.parent IS NULL", Review.class)
                .setParameter("bookId", bookId)
                .getResultList();
    }

    public List<Review> findRepliesByReviewId(Long reviewId) {
        return em.createQuery("SELECT r FROM Review r WHERE r.parent.id = :reviewId", Review.class)
                .setParameter("reviewId", reviewId)
                .getResultList();
    }



}
