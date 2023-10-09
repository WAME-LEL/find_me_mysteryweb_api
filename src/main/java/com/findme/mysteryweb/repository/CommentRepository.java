package com.findme.mysteryweb.repository;


import com.findme.mysteryweb.domain.Comment;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

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

    public List<Comment> findAll(){
        return em.createQuery("select c from Comment c", Comment.class).getResultList();
    }

    public void delete(Long commentId){
        em.remove(em.find(Comment.class, commentId));
    }


}
