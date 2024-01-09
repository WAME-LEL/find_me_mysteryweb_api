package com.findme.mysteryweb.service;

import com.findme.mysteryweb.domain.Member;
import com.findme.mysteryweb.domain.Post;
import com.findme.mysteryweb.domain.AnswerType;
import jakarta.transaction.Transactional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
@Transactional
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
        Long memberId = memberService.save(member);

        //when
        Long postId = postService.posting(memberId, "test", "test", "test", "test", AnswerType.multiple_answer);

        //then
        Post findOne = postService.findOne(postId);
        Assertions.assertThat(findOne.getMember()).isEqualTo(member);
    }

    @Test
    void update() {
        //given
        Member member = Member.createMember("test", "test", "test");
        Long memberId = memberService.save(member);
        Long postId = postService.posting(memberId, "test", "test","test", "test",AnswerType.multiple_answer);

        //when
        postService.update(postId, "update", "update");

        //then
        Post findOne = postService.findOne(postId);
        Assertions.assertThat(findOne.getTitle()).isEqualTo("update");
    }
}