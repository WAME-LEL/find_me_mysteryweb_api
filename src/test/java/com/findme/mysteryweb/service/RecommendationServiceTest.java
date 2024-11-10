package com.findme.mysteryweb.service;

import com.findme.mysteryweb.domain.Book;
import com.findme.mysteryweb.domain.Member;
import com.findme.mysteryweb.domain.Post;
import com.findme.mysteryweb.domain.Recommendation;
import com.findme.mysteryweb.repository.BookRepository;
import com.findme.mysteryweb.repository.MemberRepository;
import jakarta.transaction.Transactional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
class RecommendationServiceTest {

    @Autowired
    RecommendationService recommendationService;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    BookRepository bookRepository;

    @Test
    void delete() {
        //given
        Member member = Member.createMember("test", "test", "test");
        Book book = Book.createBook("test", "test", "test", "test", "test");
        Recommendation recommendation = Recommendation.createRecommendation(member, null, book);

        //when
        memberRepository.save(member);
        bookRepository.save(book);
        recommendationService.save(recommendation);

        recommendationService.delete(member.getId(), null, book.getId());

        //then
        Recommendation findRecommendation = recommendationService.findOne(recommendation.getId());

        Assertions.assertThat(findRecommendation).isNull();
    }
}