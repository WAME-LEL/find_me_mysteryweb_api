package com.findme.mysteryweb.service;

import com.findme.mysteryweb.domain.Comment;
import com.findme.mysteryweb.domain.Member;
import com.findme.mysteryweb.domain.Post;
import com.findme.mysteryweb.repository.CommentRepository;
import com.findme.mysteryweb.repository.MemberRepository;
import com.findme.mysteryweb.repository.PostRepository;
import jakarta.transaction.Transactional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
class CommentServiceTest {

    @Autowired
    CommentService commentService;
    @Autowired
    CommentRepository commentRepository;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    PostRepository postRepository;

    @AfterEach
    void clearStore(){
        commentRepository.clearStore();
        memberRepository.clearStore();
        postRepository.clearStore();
    }

    @Test
    void writeComment() {
        //given
        Member member = Member.createMember("test", "test", "test");
        Post post = Post.createPost("test", "test", "test", "test","test", member);

        //when
        memberRepository.save(member);
        postRepository.save(post);

        Comment comment = commentService.writeComment(post.getId(), null, member.getId(), "content");

        //then
        Comment findComment = commentService.findOne(comment.getId());
        assertThat(findComment).isEqualTo(comment);

    }

    @Test
    void findCommentsWithAllRepliesByPostId() {
        //given
        Member member = Member.createMember("test", "test", "test");
        Post post = Post.createPost("test", "test", "test", "test","test", member);

        //when
        memberRepository.save(member);
        postRepository.save(post);

        Comment comment1 = commentService.writeComment(post.getId(), null, member.getId(), "content");
        Comment comment2 = commentService.writeComment(post.getId(), comment1.getId(), member.getId(), "content");

        //then
        List<Comment> commentList = commentService.findCommentsWithAllRepliesByPostId(post.getId());

        assertThat(commentList).hasSize(1);
    }



}