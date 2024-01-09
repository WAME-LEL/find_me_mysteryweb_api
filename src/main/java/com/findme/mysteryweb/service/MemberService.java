package com.findme.mysteryweb.service;


import com.findme.mysteryweb.domain.Member;
import com.findme.mysteryweb.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {

    private final MemberRepository memberRepository;

    @Transactional
    public Long save(Member member){
        return memberRepository.save(member);
    }

    public Member findOne(Long memberId){
        return memberRepository.findOne(memberId);
    }

    public Member singIn(String username, String password){
        return memberRepository.findOneByUsernameAndPassword(username, password);
    }

    @Transactional
    public void delete(Long memberId){
        memberRepository.delete(memberId);
    }

    @Transactional
    public void saveAuthenticationCode(Long memberId, String email, String authenticationCode){
        Member member = memberRepository.findOne(memberId);
        member.setAuthenticationCode(authenticationCode);
        member.setEmail(email);
        member.setAuthenticationCodeExpireTime(LocalDateTime.now().plusHours(24));
    }

    @Transactional
    public void activateMember(Long memberId){
        Member member = memberRepository.findOne(memberId);
        member.setActivated(true);
    }

}
