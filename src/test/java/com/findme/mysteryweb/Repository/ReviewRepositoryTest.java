package com.findme.mysteryweb.Repository;

import com.findme.mysteryweb.domain.Book;
import com.findme.mysteryweb.domain.Member;
import com.findme.mysteryweb.domain.Review;
import com.findme.mysteryweb.repository.BookRepository;
import com.findme.mysteryweb.repository.ReviewRepository;
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
class ReviewRepositoryTest {

    @Autowired
    ReviewRepository reviewRepository;
    @Autowired
    BookRepository bookRepository;

    @AfterEach
    void clearStore(){
        reviewRepository.clearStore();
        bookRepository.clearStore();
    }

    @Test
    void save() {
        //given
        Review review = Review.createReview("test", new Review(), new Book(), new Member());

        //when
        reviewRepository.save(review);

        //then
        Review findReview = reviewRepository.findOne(review.getId());

        assertThat(findReview).isEqualTo(review);
    }

    @Test
    void findOne() {
        //given
        Review review = Review.createReview("test", new Review(), new Book(), new Member());

        //when
        reviewRepository.save(review);

        //then
        Review findReview = reviewRepository.findOne(review.getId());

        assertThat(findReview.getContent()).isEqualTo(review.getContent());
    }

    @Test
    void findAll() {
        //given
        Review review1 = Review.createReview("test", null, new Book(), new Member());
        Review review2 = Review.createReview("test", null, new Book(), new Member());

        //when
        reviewRepository.save(review1);
        reviewRepository.save(review2);

        //then
        List<Review> reviewList = reviewRepository.findAll();

        assertThat(reviewList.size()).isEqualTo(2);
    }

    @Test
    void findAllByBookId() {
        //given
        Book book = Book.createBook("test", "test", "test", "test", "test");

        Review review1 = Review.createReview("test", null, book, new Member());
        Review review2 = Review.createReview("test", null, book, new Member());

        //when
        bookRepository.save(book);

        reviewRepository.save(review1);
        reviewRepository.save(review2);

        //then
        List<Review> reviewList = reviewRepository.findAllByBookId(book.getId());

        assertThat(reviewList.size()).isEqualTo(2);
    }

    @Test
    void delete() {
        //given
        Book book = Book.createBook("test", "test", "test", "test", "test ");
        Review review = Review.createReview("test", null, book, new Member());

        //when
        bookRepository.save(book);
        reviewRepository.save(review);

        reviewRepository.delete(review.getId());

        //then
        Review findReview = reviewRepository.findOne(review.getId());

        assertThat(findReview).isNull();
    }

    @Test
    void findAllTopLevelCommentsByBookId() {
        //given
        Book book = Book.createBook("test", "test", "test", "test", "test");

        Review review1 = Review.createReview("test", null, book, new Member());
        Review review2 = Review.createReview("test", review1, book, new Member());

        //when
        bookRepository.save(book);

        reviewRepository.save(review1);
        reviewRepository.save(review2);

        //then
        List<Review> reviewList = reviewRepository.findAllTopLevelCommentsByBookId(book.getId());

        assertThat(reviewList).hasSize(1);
    }

    @Test
    void findRepliesByReviewId() {
        //given
        Book book = Book.createBook("test", "test", "test", "test", "test");

        Review review1 = Review.createReview("test", null, book, new Member());
        Review review2 = Review.createReview("test", review1, book, new Member());

        //when
        bookRepository.save(book);

        reviewRepository.save(review1);
        reviewRepository.save(review2);

        //then
        List<Review> reviewList = reviewRepository.findRepliesByReviewId(review1.getId());

        assertThat(reviewList).hasSize(1);
    }
}