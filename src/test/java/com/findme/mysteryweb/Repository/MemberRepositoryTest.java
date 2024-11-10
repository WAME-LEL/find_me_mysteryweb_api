package com.findme.mysteryweb.Repository;

import com.findme.mysteryweb.domain.CorrectAnswer;
import com.findme.mysteryweb.domain.Member;
import com.findme.mysteryweb.domain.Post;
import com.findme.mysteryweb.repository.CorrectAnswerRepository;
import com.findme.mysteryweb.repository.MemberRepository;
import com.findme.mysteryweb.repository.PostRepository;
import jakarta.persistence.Tuple;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
@Transactional
@ActiveProfiles("test")
class MemberRepositoryTest {

    @Autowired
    MemberRepository memberRepository;
    @Autowired
    PostRepository postRepository;
    @Autowired
    CorrectAnswerRepository correctAnswerRepository;

    @Autowired
    PasswordEncoder passwordEncoder;



    @AfterEach
    void afterEach(){
        memberRepository.clearStore();
        postRepository.clearStore();
        correctAnswerRepository.clearStore();
    }

    @Test
    void save() {
        //given
        Member member = Member.createMember("test", "test", "test");

        //when
        memberRepository.save(member);

        //then
        Member findOne = memberRepository.findOne(member.getId());
        assertThat(findOne).isEqualTo(member);

    }

    @Test
    void delete() {
        //given
        Member member = Member.createMember("test", "test", "test");


        //when
        memberRepository.save(member);
        memberRepository.delete(member.getId());

        //then
        assertThat(memberRepository.findOne(member.getId())).isNull();

    }

    @Test
    void findOne() {
        //given
        Member member = Member.createMember("test", "test", "test");

        //when
        memberRepository.save(member);

        //then
        Member findOne = memberRepository.findOne(member.getId());
        assertThat(findOne).isEqualTo(member);
    }

    @Test
    void findAll(){
        //given
        Member member1 = Member.createMember("test", "test", "test");
        Member member2 = Member.createMember("test", "test", "test");

        //when
        memberRepository.save(member1);
        memberRepository.save(member2);

        //then
        List<Member> findAll = memberRepository.findAll();
        assertThat(findAll.size()).isEqualTo(2);
    }

    @Test
    void findOneByUsername() {
        //given
        Member member = Member.createMember("test", "test", "test");

        //when
        memberRepository.save(member);

        //then
        Member findMember = memberRepository.findOneByUsername(member.getUsername());
        assertThat(findMember).isEqualTo(member);

    }

    @Test
    void findOneByPostId() {
        //given
        Member member = Member.createMember("test", "test", "test");
        Post post = Post.createPost("title", "content", "type", "answer", "test", member);

        //when
        memberRepository.save(member);
        postRepository.save(post);

        //then
        Member findMember = memberRepository.findOneByPostId(post.getId());

        assertThat(findMember).isEqualTo(member);
    }


    @Test
    void findAllOrderBySolved() {
        //given
        Member member = Member.createMember("test", "test", "test");
        Post post = Post.createPost("title", "content", "type", "answer", "test", member);


        //when
        memberRepository.save(member);
        postRepository.save(post);
        correctAnswerRepository.save(CorrectAnswer.createCorrectAnswer(member, post));

        //then
        List<Tuple> solvedList = memberRepository.findAllOrderBySolved();

        assertThat(solvedList.size()).isEqualTo(1);
    }

    @Test
    void findAllByCorrectAnswer() {
        //given
        Member member1 = Member.createMember("test", "test", "test");
        Member member2 = Member.createMember("test", "test", "test");

        Post post = Post.createPost("title", "content", "type", "answer", "test", member1);

        //when
        memberRepository.save(member1);
        memberRepository.save(member2);

        postRepository.save(post);

        correctAnswerRepository.save(CorrectAnswer.createCorrectAnswer(member1, post));
        correctAnswerRepository.save(CorrectAnswer.createCorrectAnswer(member2, post));

        //then
        List<Tuple> solvedList = memberRepository.findAllByCorrectAnswer(post.getId());
        assertThat(solvedList.size()).isEqualTo(2);
    }

    @Test
    void findCountOrderBySolved() {
        //given
        Member member1 = Member.createMember("test", "test", "test");
        Member member2 = Member.createMember("test", "test", "test");

        //when
        memberRepository.save(member1);
        memberRepository.save(member2);

        //then
        List<Tuple> countOrderBySolved = memberRepository.findCountOrderBySolved(5);

        assertThat(countOrderBySolved.size()).isEqualTo(2);
    }

    @Test
    void duplicateNicknameTest() {
        //given
        Member member1 = Member.createMember("test", "test", "test");

        //when
        memberRepository.save(member1);

        //then
        boolean b = memberRepository.duplicateNicknameTest("test");

        assertThat(b).isTrue();
    }

    @Test
    void duplicateUsernameTest() {
        //given
        Member member1 = Member.createMember("test", "test", "test");

        //when
        memberRepository.save(member1);

        //then
        boolean b = memberRepository.duplicateUsernameTest("ttttttt");

        assertThat(b).isFalse();
    }
}