package com.findme.mysteryweb.repository;


import com.findme.mysteryweb.domain.Comment;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class CommentRepository {
    private final EntityManager em;

    public Comment save(Comment comment){
        em.persist(comment);

        return comment;
    }

    public Comment findOne(Long commentId){
        return em.find(Comment.class, commentId);
    }

    public List<Comment> findAll(){
        return em.createQuery("select c from Comment c", Comment.class).getResultList();
    }

    public List<Comment> findAllByMemberId(Long memberId){
        return em.createQuery("select c from Comment c where c.member.id = :memberId order by c.datetime desc", Comment.class)
                .setParameter("memberId", memberId)
                .getResultList();

    }

    public List<Comment> findAllByPostId(Long postId){
        return em.createQuery("select c from Comment c where c.post.id = :postId", Comment.class)
                .setParameter("postId", postId)
                .getResultList();
    }

    public void delete(Long commentId){
        em.remove(em.find(Comment.class, commentId));
    }

    public void clearStore (){
        em.clear();
    }


    public List<Comment> findAllTopLevelCommentsByPostId(Long postId) {
        return em.createQuery("SELECT c FROM Comment c WHERE c.post.id = :postId AND c.parent IS NULL", Comment.class)
                .setParameter("postId", postId)
                .getResultList();
    }

    public List<Comment> findRepliesByCommentId(Long commentId) {
        return em.createQuery("SELECT c FROM Comment c WHERE c.parent.id = :commentId", Comment.class)
                .setParameter("commentId", commentId)
                .getResultList();
    }


}
