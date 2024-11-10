package com.findme.mysteryweb.service;

import com.findme.mysteryweb.domain.Book;
import com.findme.mysteryweb.domain.Member;
import com.findme.mysteryweb.domain.Review;
import com.findme.mysteryweb.repository.BookRepository;
import com.findme.mysteryweb.repository.MemberRepository;
import jakarta.transaction.Transactional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;


@SpringBootTest
@Transactional
@ActiveProfiles("test")
class ReviewServiceTest {

    @Autowired
    ReviewService reviewService;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private BookRepository bookRepository;

    @Test
    void save() {
        //given
        Member member = Member.createMember("test", "test", "test");
        Book book = Book.createBook("test", "test", "test", "test", "test");

        //when
        memberRepository.save(member);
        bookRepository.save(book);
        reviewService.save(book.getId(), null, member.getId(), "content");

        //then
        List<Review> reviewList = reviewService.findAll();

        Assertions.assertThat(reviewList.size()).isEqualTo(1);
    }

    @Test
    void findReviewsWithAllRepliesByBookId() {
        //given
        Member member = Member.createMember("test", "test", "test");
        Book book = Book.createBook("test", "test", "test", "test", "test");

        //when
        memberRepository.save(member);
        bookRepository.save(book);
        reviewService.save(book.getId(), null, member.getId(), "content");
        reviewService.save(book.getId(), null, member.getId(), "content");

        //then
        List<Review> reviewList = reviewService.findReviewsWithAllRepliesByBookId(book.getId());

        Assertions.assertThat(reviewList.size()).isEqualTo(2);
    }
}