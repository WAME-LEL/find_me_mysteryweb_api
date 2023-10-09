package com.findme.mysteryweb.repository;


import com.findme.mysteryweb.domain.Comment;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class CommentRepository {
    private final EntityManager em;

    public void save(Comment comment){
        em.persist(comment);
    }

    public Comment findOne(Long commentId){
        return em.find(Comment.class, commentId);
    }

    public void delete(Long commentId){
        em.remove(em.find(Comment.class, commentId));
    }


}
