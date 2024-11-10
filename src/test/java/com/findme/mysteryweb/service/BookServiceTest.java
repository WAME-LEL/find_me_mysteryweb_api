package com.findme.mysteryweb.service;

import com.findme.mysteryweb.domain.Book;
import com.findme.mysteryweb.repository.BookRepository;
import jakarta.transaction.Transactional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
class BookServiceTest {

    @Autowired
    BookService bookService;

    @Autowired
    BookRepository bookRepository;

    @AfterEach
    void clearStore(){
        bookRepository.clearStore();
    }

    @Test
    void increaseRecommendCount() {
        //given
        Book book = Book.createBook("test", "test", "test", "test", "test");

        //when
        bookService.save(book);

        bookService.increaseRecommendCount(book.getId());

        //then
        Book findBook = bookService.findOne(book.getId());

        assertThat(findBook.getRecommendationCount()).isEqualTo(1);
    }

    @Test
    void decreaseRecommendCount() {
        //given
        Book book = Book.createBook("test", "test", "test", "test", "test");

        //when
        bookService.save(book);

        bookService.decreaseRecommendCount(book.getId());

        //then
        Book findBook = bookService.findOne(book.getId());

        assertThat(findBook.getRecommendationCount()).isEqualTo(-1);
    }


}