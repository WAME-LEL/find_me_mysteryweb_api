package com.findme.mysteryweb.Repository;

import com.findme.mysteryweb.domain.Member;
import com.findme.mysteryweb.repository.MemberRepository;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@Transactional
class MemberRepositoryTest {
    @Autowired
    MemberRepository memberRepository;


    @AfterEach
    void afterEach(){
        memberRepository.clearStore();
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
}