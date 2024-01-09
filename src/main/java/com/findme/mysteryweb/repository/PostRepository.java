package com.findme.mysteryweb.repository;

import com.findme.mysteryweb.domain.Member;
import com.findme.mysteryweb.domain.Post;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class PostRepository {

    private final EntityManager em;

    public Long save(Post post){
        em.persist(post);

        return post.getId();
    }

    public Post findOne(Long id){
         return em.find(Post.class, id);
    }

    public List<Post> findAll(){
        return em.createQuery("select p from Post p", Post.class)
                .getResultList();
    }

    public List<Post> findAllByType(String type){
        return em.createQuery("select p from Post p where p.type like :type", Post.class)
                .setParameter("type", type)
                .getResultList();
    }

    public List<Post> findAllByTitle(String title){
        return em.createQuery("select p from Post p where p.title like :title", Post.class)
                .setParameter("title", title)
                .getResultList();
    }

    public List<Post> findAllByTypeAndTitle(String type, String title){
        return em.createQuery("select p from Post p where p.title like :title and p.type like :type", Post.class)
                .setParameter("title", title)
                .setParameter("type", type)
                .getResultList();
    }

    public void delete(Long id){
        em.remove(em.find(Post.class, id));
    }

    public void clearStore(){
        em.clear();
    }


}
