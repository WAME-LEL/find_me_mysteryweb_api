package com.findme.mysteryweb.Repository;

import com.findme.mysteryweb.domain.CorrectAnswerHistory;
import com.findme.mysteryweb.domain.Member;
import com.findme.mysteryweb.domain.Post;
import com.findme.mysteryweb.repository.CorrectAnswerHistoryRepository;
import com.findme.mysteryweb.repository.MemberRepository;
import com.findme.mysteryweb.repository.PostRepository;
import jakarta.transaction.Transactional;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@Transactional
@ActiveProfiles("test")
class CorrectAnswerHistoryRepositoryTest {

    @Autowired
    CorrectAnswerHistoryRepository correctAnswerHistoryRepository;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    PostRepository postRepository;


    @Test
    void save(){
        //given
        Member member = Member.createMember("test", "test", "test");
        Post post = Post.createPost("test", "test", "test", "test","test", member);

        CorrectAnswerHistory correctAnswerHistory = CorrectAnswerHistory.createCorrectAnswerHistory(member, post, "test");

        //when
        memberRepository.save(member);
        postRepository.save(post);
        correctAnswerHistoryRepository.save(correctAnswerHistory);

        //then
        CorrectAnswerHistory findOne = correctAnswerHistoryRepository.findOne(member.getId(), correctAnswerHistory.getId());

        Assertions.assertThat(findOne).isEqualTo(correctAnswerHistory);
    }

    @Test
    void findAll(){
        //given
        Member member = Member.createMember("test", "test", "test");
        Post post = Post.createPost("test", "test", "test", "test","test", member);

        CorrectAnswerHistory correctAnswerHistory1 = CorrectAnswerHistory.createCorrectAnswerHistory(member, post, "test");
        CorrectAnswerHistory correctAnswerHistory2 = CorrectAnswerHistory.createCorrectAnswerHistory(member, post, "test");

        //when
        memberRepository.save(member);
        postRepository.save(post);
        correctAnswerHistoryRepository.save(correctAnswerHistory1);
        correctAnswerHistoryRepository.save(correctAnswerHistory2);

        //then
        List<CorrectAnswerHistory> correctAnswerHistoryList = correctAnswerHistoryRepository.findAll();

        Assertions.assertThat(correctAnswerHistoryList.size()).isEqualTo(2);
    }


}