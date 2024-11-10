package com.findme.mysteryweb.service;

import com.findme.mysteryweb.domain.CorrectAnswerHistory;
import com.findme.mysteryweb.domain.Member;
import com.findme.mysteryweb.domain.Post;
import com.findme.mysteryweb.repository.MemberRepository;
import com.findme.mysteryweb.repository.PostRepository;
import jakarta.transaction.Transactional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
class CorrectAnswerHistoryServiceTest {
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    PostRepository postRepository;
    @Autowired
    CorrectAnswerHistoryService correctAnswerHistoryService;

    @Test
    void save() {
        //given
        Member member = Member.createMember("test", "test", "test");
        Post post = Post.createPost("test", "test", "test", "test","test", member);

        //when
        memberRepository.save(member);
        postRepository.save(post);

        correctAnswerHistoryService.save(member.getId(), post.getId(), "test");
        correctAnswerHistoryService.save(member.getId(), post.getId(), "test");

        //then
        List<CorrectAnswerHistory> correctAnswerHistoryList = correctAnswerHistoryService.findAll();

        Assertions.assertThat(correctAnswerHistoryList.size()).isEqualTo(2);

    }
}