package com.findme.mysteryweb.repository;


import com.findme.mysteryweb.domain.Book;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
@RequiredArgsConstructor
public class BookRepository {
    private final EntityManager em;

    public void save(Book book){
        em.persist(book);
    }

    public Book findOne(Long bookId){
        return em.find(Book.class, bookId);
    }

    public List<Book> findAllByTitle(String title){
        return em.createQuery("select b from Book b where b.title like :title order by b.recommendationCount desc, b.id desc", Book.class)
                .setParameter("title", "%" + title + "%")
                .getResultList();
    }

    public List<Book> findAllByAuthor(String author){
        return em.createQuery("select b from Book b where b.author like :author order by b.recommendationCount desc, b.id desc", Book.class)
                .setParameter("author", "%" + author + "%")
                .getResultList();
    }

    public List<Book> findAllByRecommendCount(){
        return em.createQuery("select b from Book b order by b.recommendationCount desc, b.id desc", Book.class)
                .getResultList();
    }

    public List<Book> findCountByRecommendCount(int count){
        return em.createQuery("select b from Book b order by b.recommendationCount desc", Book.class)
                .setMaxResults(count)
                .getResultList();
    }

    public void clearStore() {
        em.clear();
    }



}
