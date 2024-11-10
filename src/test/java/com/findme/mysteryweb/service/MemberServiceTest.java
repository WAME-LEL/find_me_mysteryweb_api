package com.findme.mysteryweb.service;

import com.findme.mysteryweb.domain.Member;
import com.findme.mysteryweb.repository.MemberRepository;
import jakarta.transaction.Transactional;
import org.aspectj.lang.annotation.After;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
class MemberServiceTest {

    @Autowired
    MemberService memberService;
    @Autowired
    MemberRepository memberRepository;

    @AfterEach
    void clearStore(){
        memberRepository.clearStore();
    }

    @Test
    void saveAuthenticationCode() {
        //given
        Member member = Member.createMember("test", "test", "test");

        //when
        memberRepository.save(member);
        memberService.saveAuthenticationCode(member.getId(), "test", "test");

        //then
        Member findMember = memberRepository.findOne(member.getId());

        assertThat(findMember.getEmail()).isEqualTo("test");
    }

    @Test
    void activateMember() {
        //given
        Member member = Member.createMember("test", "test", "test");

        //when
        memberRepository.save(member);
        memberService.activateMember(member.getId());

        //then
        Member findMember = memberRepository.findOne(member.getId());

        assertThat(findMember.isActivated()).isTrue();
    }

    @Test
    void updatePassword() {
        //given
        Member member = Member.createMember("test", "test", "test");

        //when
        memberRepository.save(member);
        memberService.updatePassword(member.getId(), "update");

        //then
        Member findMember = memberRepository.findOne(member.getId());

        assertThat(findMember.getPassword()).isEqualTo("update");
    }
}