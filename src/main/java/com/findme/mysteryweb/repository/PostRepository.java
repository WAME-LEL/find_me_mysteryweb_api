package com.findme.mysteryweb.repository;

import com.findme.mysteryweb.domain.Post;
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

    public List<Post> findAllByMemberId(Long memberId){
        return em.createQuery("select p from Post p where p.member.id = :memberId order by p.datetime desc", Post.class)
                .setParameter("memberId", memberId)
                .getResultList();

    }

    public List<Post> findAllByType(String type){
        return em.createQuery("select p from Post p where p.type like :type order by p.datetime desc", Post.class)
                .setParameter("type", type)
                .getResultList();
    }

    public List<Post> findAllByTitle(String title){
        return em.createQuery("select p from Post p where p.title like :title order by p.datetime desc", Post.class)
                .setParameter("title", "%" + title + "%")
                .getResultList();
    }

    public List<Post> findAllByAuthor(String author){
        return em.createQuery("select p from Post p where p.member.nickname like :author order by  p.datetime desc", Post.class)
                .setParameter("author", "%" + author + "%")
                .getResultList();
    }

    public List<Post> findAllByTypeAndTitle(String type, String title){
        return em.createQuery("select p from Post p where p.title like :title and p.type like :type order by p.datetime desc", Post.class)
                .setParameter("title", "%" + title + "%")
                .setParameter("type", type)
                .getResultList();
    }

    public List<Post> findAllByTypeAndTitleOrContent(String type, String title, String content){
        return em.createQuery("select p from Post p where p.type like :type and (p.title like :title or p.content like :content) order by p.datetime desc", Post.class)
                .setParameter("type", type)
                .setParameter("title", "%" + title + "%")
                .setParameter("content", "%" + content + "%")
                .getResultList();

    }

    public List<Post> findAllByTypeAndAuthor(String type, String author){
        return em.createQuery("select p from Post p where p.member.nickname like :author and p.type like :type order by p.datetime desc", Post.class)
                .setParameter("author", "%" + author + "%")
                .setParameter("type", type)
                .getResultList();
    }

    public List<Post> findAllOrderByViewCount(String custom, String cold){
        return em.createQuery("select p from Post p where p.type =:custom or p.type =:cold order by p.viewCount desc", Post.class)
                .setParameter("custom", custom)
                .setParameter("cold", cold)
                .getResultList();
    }

    public List<Post> findAllOrderByRecommendationCount(String custom, String cold){
        return em.createQuery("select p from Post p where p.type =:custom or p.type = :cold order by p.recommendationCount desc", Post.class)
                .setParameter("custom", custom)
                .setParameter("cold", cold)
                .getResultList();
    }


    public List<Post> findCountOrderByRecommendationCount(int count){
        return em.createQuery("select p from Post p order by p.recommendationCount desc", Post.class)
                .setMaxResults(count)
                .getResultList();
    }

    public List<Post> findCountByTypeOrderByRecommendationCount(String type, int count){
        return em.createQuery("select p from Post p where p.type =:type order by p.recommendationCount desc", Post.class)
                .setParameter("type", type)
                .setMaxResults(count)
                .getResultList();
    }

    public List<Post> findCountOrderByDatetime(String type, int count){
        return em.createQuery("select p from Post p where p.type like :type order by p.datetime desc", Post.class)
                .setParameter("type", type)
                .setMaxResults(count)
                .getResultList();
    }

    public void delete(Long id){
        em.remove(em.find(Post.class, id));
    }

    public void clearStore(){
        em.clear();
    }


}
