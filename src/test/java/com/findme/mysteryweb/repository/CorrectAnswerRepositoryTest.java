package com.findme.mysteryweb.Repository;

import com.findme.mysteryweb.domain.CorrectAnswer;
import com.findme.mysteryweb.domain.Member;
import com.findme.mysteryweb.domain.Post;
import com.findme.mysteryweb.repository.CorrectAnswerRepository;
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
import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@Transactional
@ActiveProfiles("test")
class CorrectAnswerRepositoryTest {

    @Autowired
    CorrectAnswerRepository correctAnswerRepository;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    PostRepository postRepository;

    @AfterEach
    void clearStore(){
        correctAnswerRepository.clearStore();
        memberRepository.clearStore();
        postRepository.clearStore();
    }

    @Test
    void save() {
        //given
        Member member = Member.createMember("test", "test", "test");
        Post post = Post.createPost("test", "test", "test", "test","test", member);
        CorrectAnswer correctAnswer = CorrectAnswer.createCorrectAnswer(member, post);

        //when
        correctAnswerRepository.save(correctAnswer);

        //then
        CorrectAnswer findCorrectAnswer = correctAnswerRepository.findOne(correctAnswer.getId());

        assertThat(findCorrectAnswer).isEqualTo(correctAnswer);
    }

    @Test
    void findOne() {
        //given
        Member member = Member.createMember("test", "test", "test");
        Post post = Post.createPost("test", "test", "test", "test","test", member);
        CorrectAnswer correctAnswer = CorrectAnswer.createCorrectAnswer(member, post);

        //when
        correctAnswerRepository.save(correctAnswer);

        //then
        CorrectAnswer findCorrectAnswer = correctAnswerRepository.findOne(correctAnswer.getId());

        assertThat(findCorrectAnswer).isEqualTo(correctAnswer);
    }

    @Test
    void findOneByPostAndMember() {
        //given
        Member member = Member.createMember("test", "test", "test");
        Post post = Post.createPost("test", "test", "test", "test","test", member);
        CorrectAnswer correctAnswer = CorrectAnswer.createCorrectAnswer(member, post);

        //when
        memberRepository.save(member);
        postRepository.save(post);
        correctAnswerRepository.save(correctAnswer);

        //then
        CorrectAnswer findCorrectAnswer = correctAnswerRepository.findOneByPostAndMember(post.getId(), member.getId());

        assertThat(findCorrectAnswer).isEqualTo(correctAnswer);
    }

    @Test
    void findAllByMember() {
        //given
        Member member = Member.createMember("test", "test", "test");
        Post post1 = Post.createPost("test", "test", "test", "test","test", member);
        Post post2 = Post.createPost("test", "test", "test", "test","test", member);

        CorrectAnswer correctAnswer1 = CorrectAnswer.createCorrectAnswer(member, post1);
        CorrectAnswer correctAnswer2 = CorrectAnswer.createCorrectAnswer(member, post2);

        //when
        memberRepository.save(member);
        postRepository.save(post1);
        postRepository.save(post2);

        correctAnswerRepository.save(correctAnswer1);
        correctAnswerRepository.save(correctAnswer2);

        //then
        List<CorrectAnswer> correctAnswers = correctAnswerRepository.findAllByMember(member.getId());

        assertThat(correctAnswers).hasSize(2);

    }
}