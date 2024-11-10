package com.findme.mysteryweb.service;

import com.findme.mysteryweb.domain.Member;
import com.findme.mysteryweb.domain.Post;
import jakarta.transaction.Transactional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.*;


@SpringBootTest
@Transactional
@ActiveProfiles("test")
class PostServiceTest {

    @Autowired
    PostService postService;

    @Autowired
    CommentService commentService;

    @Autowired
    MemberService memberService;

    @Test
    void posting() {
        //given
        Member member = Member.createMember("test", "test", "test");

        //when
        memberService.save(member);

        Long postId = postService.posting(member.getId(), "test", "Test,", "test", "test", "test");

        //then
        Post post = postService.findOne(postId);

        assertThat(post.getMember()).isEqualTo(member);
    }

    @Test
    void update() {
        //given
        Member member = Member.createMember("test", "test", "test");

        //when
        memberService.save(member);

        Long postId = postService.posting(member.getId(), "test", "Test,", "test", "test", "test");
        postService.update(postId, "update", "update", "update", "update", "update");

        //then
        Post post = postService.findOne(postId);

        assertThat(post.getTitle()).isEqualTo("update");

    }

    @Test
    void increaseViewCount() {
        //given
        Member member = Member.createMember("test", "test", "test");

        //when
        memberService.save(member);

        Long postId = postService.posting(member.getId(), "test", "Test,", "test", "test", "test");
        postService.increaseViewCount(postId);

        //then
        Post post = postService.findOne(postId);
        assertThat(post.getViewCount()).isEqualTo(1);
    }

    @Test
    void increaseRecommendCount() {
        //given
        Member member = Member.createMember("test", "test", "test");

        //when
        memberService.save(member);

        Long postId = postService.posting(member.getId(), "test", "Test,", "test", "test", "test");
        postService.increaseRecommendCount(postId);

        //then
        Post post = postService.findOne(postId);
        assertThat(post.getRecommendationCount()).isEqualTo(1);
    }

    @Test
    void decreaseRecommendCount() {
        //given
        Member member = Member.createMember("test", "test", "test");

        //when
        memberService.save(member);

        Long postId = postService.posting(member.getId(), "test", "Test,", "test", "test", "test");
        postService.decreaseRecommendCount(postId);

        //then
        Post post = postService.findOne(postId);
        assertThat(post.getRecommendationCount()).isEqualTo(-1);
    }
}