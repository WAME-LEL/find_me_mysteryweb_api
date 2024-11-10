package com.findme.mysteryweb.Repository;

import com.findme.mysteryweb.domain.Book;
import com.findme.mysteryweb.domain.Member;
import com.findme.mysteryweb.domain.Post;
import com.findme.mysteryweb.domain.Recommendation;
import com.findme.mysteryweb.repository.BookRepository;
import com.findme.mysteryweb.repository.MemberRepository;
import com.findme.mysteryweb.repository.PostRepository;
import com.findme.mysteryweb.repository.RecommendationRepository;
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
class RecommendationRepositoryTest {

    @Autowired
    RecommendationRepository recommendationRepository;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    PostRepository postRepository;
    @Autowired
    BookRepository bookRepository;

    @AfterEach
    void clearStore(){
        recommendationRepository.clearStore();
        memberRepository.clearStore();
        postRepository.clearStore();
        bookRepository.clearStore();
    }


    @Test
    void save() {
        //given
        Recommendation recommendation = Recommendation.createRecommendation(new Member(), new Post(), new Book());

        //when
        recommendationRepository.save(recommendation);

        //then
        Recommendation findRecommendation = recommendationRepository.findOne(recommendation.getId());

        assertThat(findRecommendation).isEqualTo(recommendation);
    }

    @Test
    void findOne() {
        //given
        Recommendation recommendation = Recommendation.createRecommendation(new Member(), new Post(), new Book());

        //when
        recommendationRepository.save(recommendation);

        //then
        Recommendation findRecommendation = recommendationRepository.findOne(recommendation.getId());

        assertThat(findRecommendation).isEqualTo(recommendation);
    }

    @Test
    void findOneByMemberIdAndOtherId() {
        //given
        Member member = Member.createMember("test", "test", "test");
        Post post = Post.createPost("test", "test", "test", "test","test", member);

        Recommendation recommendation = Recommendation.createRecommendation(member, post, null);

        //when
        memberRepository.save(member);
        postRepository.save(post);
        recommendationRepository.save(recommendation);

        //then
        Recommendation findRecommendation = recommendationRepository.findOneByMemberIdAndOtherId(member.getId(), post.getId(), null);

        assertThat(findRecommendation).isEqualTo(recommendation);
    }

    @Test
    void findAll() {
        //given
        Member member = Member.createMember("test", "test", "test");
        Post post = Post.createPost("test", "test", "test", "test","test", member);
        Book book = Book.createBook("test", "test", "test", "test", "test");

        Recommendation recommendation1 = Recommendation.createRecommendation(member, post, null);
        Recommendation recommendation2 = Recommendation.createRecommendation(member, null, book);

        //when
        memberRepository.save(member);
        postRepository.save(post);
        bookRepository.save(book);

        recommendationRepository.save(recommendation1);
        recommendationRepository.save(recommendation2);

        //then
        List<Recommendation> recommendationList = recommendationRepository.findAll();

        assertThat(recommendationList).hasSize(2);
    }

    @Test
    void findAllByMemberId() {
        //given
        Member member = Member.createMember("test", "test", "test");
        Post post = Post.createPost("test", "test", "test", "test","test", member);
        Book book = Book.createBook("test", "test", "test", "test", "test");

        Recommendation recommendation1 = Recommendation.createRecommendation(member, post, null);
        Recommendation recommendation2 = Recommendation.createRecommendation(member, null, book);

        //when
        memberRepository.save(member);
        postRepository.save(post);
        bookRepository.save(book);

        recommendationRepository.save(recommendation1);
        recommendationRepository.save(recommendation2);

        //then
        List<Recommendation> recommendationList = recommendationRepository.findAllByMemberId(member.getId());

        assertThat(recommendationList).hasSize(2);
    }

    @Test
    void findAllByMemberIdAtBookOrPost() {
        //given
        Member member = Member.createMember("test", "test", "test");
        Book book1 = Book.createBook("test", "test", "test", "test", "test");
        Book book2 = Book.createBook("test", "test", "test", "test", "test");

        Recommendation recommendation1 = Recommendation.createRecommendation(member, null, book1);
        Recommendation recommendation2 = Recommendation.createRecommendation(member, null, book2);

        //when
        memberRepository.save(member);
        bookRepository.save(book1);
        bookRepository.save(book2);

        recommendationRepository.save(recommendation1);
        recommendationRepository.save(recommendation2);

        //then
        List<Recommendation> recommendationList = recommendationRepository.findAllByMemberIdAtBookOrPost(member.getId(), "book");

        assertThat(recommendationList).hasSize(2);
    }

    @Test
    void delete() {
        //given
        Member member = Member.createMember("test", "test", "test");
        Book book = Book.createBook("test", "test", "test", "test", "test");
        Recommendation recommendation = Recommendation.createRecommendation(member, null, book);

        //when
        memberRepository.save(member);
        bookRepository.save(book);
        recommendationRepository.save(recommendation);

        recommendationRepository.delete(recommendation.getId());

        //then
        Recommendation findRecommendation = recommendationRepository.findOne(recommendation.getId());

        assertThat(findRecommendation).isNull();

    }
}