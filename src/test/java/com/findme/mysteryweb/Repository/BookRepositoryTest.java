package com.findme.mysteryweb.Repository;

import com.findme.mysteryweb.domain.Book;
import com.findme.mysteryweb.repository.BookRepository;
import jakarta.transaction.Transactional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@Transactional
@ActiveProfiles("test")
class BookRepositoryTest {

    @Autowired
    BookRepository bookRepository;


    @AfterEach
    void clearStore(){
        bookRepository.clearStore();
    }

    @Test
    void save() {
        //given
        Book book = Book.createBook("test", "test", "test", "test", "test");

        //when
        bookRepository.save(book);

        //then
        Book findBook = bookRepository.findOne(book.getId());

        assertThat(findBook).isEqualTo(book);
    }

    @Test
    void findOne() {
        //given
        Book book = Book.createBook("test", "test", "test", "test", "test");

        //when
        bookRepository.save(book);

        //then
        Book findBook = bookRepository.findOne(book.getId());

        assertThat(findBook.getTitle()).isEqualTo(book.getTitle());
    }

    @Test
    void findAllByTitle() {
        //given
        Book book1 = Book.createBook("test", "test", "test", "test", "test");
        Book book2 = Book.createBook("test", "test", "test", "test", "test");

        //when
        bookRepository.save(book1);
        bookRepository.save(book2);

        //then
        List<Book> books = bookRepository.findAllByTitle("test");

        assertThat(books).hasSize(2);
    }

    @Test
    void findAllByAuthor() {
        //given
        Book book1 = Book.createBook("test", "test", "test", "test", "test");
        Book book2 = Book.createBook("test", "test", "test", "test", "test");
        Book book3 = Book.createBook("test", "one", "test", "test", "test");

        //when
        bookRepository.save(book1);
        bookRepository.save(book2);
        bookRepository.save(book3);

        //then
        List<Book> books = bookRepository.findAllByAuthor("test");

        assertThat(books).hasSize(2);
    }

    @Test
    void findAllByRecommendCount() {
        //given
        Book book1 = Book.createBook("test", "test", "test", "test", "test");
        Book book2 = Book.createBook("test", "test", "test", "test", "test");

        //when
        bookRepository.save(book1);
        bookRepository.save(book2);

        //then
        List<Book> allByRecommendCount = bookRepository.findAllByRecommendCount();

        assertThat(allByRecommendCount).hasSize(2);
    }

    @Test
    void findCountByRecommendCount() {
        //given
        Book book1 = Book.createBook("test", "test", "test", "test", "test");
        Book book2 = Book.createBook("test", "test", "test", "test", "test");
        Book book3 = Book.createBook("test", "test2", "test", "test", "test");

        //when
        bookRepository.save(book1);
        bookRepository.save(book2);
        bookRepository.save(book3);

        //then
        List<Book> countByRecommendCount = bookRepository.findCountByRecommendCount(2);

        assertThat(countByRecommendCount).hasSize(2);
    }
}