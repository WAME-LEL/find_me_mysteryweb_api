package com.findme.mysteryweb.service;

import com.findme.mysteryweb.domain.Comment;
import com.findme.mysteryweb.domain.Member;
import com.findme.mysteryweb.domain.AnswerType;
import jakarta.transaction.Transactional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Transactional      //테스트에 붙으면 롤백
class CommentServiceTest {

    @Autowired
    CommentService commentService;

    @Autowired
    MemberService memberService;

    @Autowired
    PostService postService;

    @Test
    void writeComment() {
        //given

        Long memberId = memberService.save(Member.createMember("test", "test", "test"));
        Long postId = postService.posting(memberId, "test", "test", "test", "test", AnswerType.multiple_answer);

        //when
        Long commentId = commentService.writeComment(postId, memberId, "content");

        //then
        Comment findOne = commentService.findOne(commentId);
        Assertions.assertThat(findOne.getId()).isEqualTo(commentId);

    }

}