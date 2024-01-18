package com.findme.mysteryweb.service;

import com.findme.mysteryweb.domain.Book;
import com.findme.mysteryweb.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BookService {
    private final BookRepository bookRepository;

    @Transactional
    public void save(Book book){
        bookRepository.save(book);
    }

    public Book findOne(Long bookId){
        return bookRepository.findOne(bookId);
    }

    public List<Book> findAll(){
        return bookRepository.findAllByRecommendCount();
    }

    public List<Book> findAllByTitle(String title){
        return bookRepository.findAllByTitle(title);
    }

    public List<Book> findAllByAuthor(String author){
        return bookRepository.findAllByAuthor(author);
    }

    public List<Book> findCountByRecommendCount(int count){
        return bookRepository.findCountByRecommendCount(count);
    }

    @Transactional
    public void increaseRecommendCount(Long bookId){
        Book book = bookRepository.findOne(bookId);
        book.setRecommendationCount(book.getRecommendationCount()+1);
    }

    @Transactional
    public void decreaseRecommendCount(Long bookId){
        Book book = bookRepository.findOne(bookId);
        book.setRecommendationCount(book.getRecommendationCount()-1);
    }


}
