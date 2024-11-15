package com.findme.mysteryweb.service;


import com.findme.mysteryweb.domain.Member;
import com.findme.mysteryweb.repository.MemberRepository;
import jakarta.persistence.Tuple;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

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

    public Member findOneByEmail(String email){
        return memberRepository.findOneByEmail(email);
    }

    public Member findOneByPostId(Long postId){
        return memberRepository.findOneByPostId(postId);
    }

    public Member findOneByEmailAndUsername(String email, String username){
        return memberRepository.findOneByEmailAndUsername(email, username);
    }

    public Member findOneByUsername(String username) {
        return memberRepository.findOneByUsername(username);
    }



    public List<Tuple> findAllOrderBySolved(){
        return memberRepository.findAllOrderBySolved();
    }

    public List<Tuple> findAllByCorrectAnswer(Long postId){
        return memberRepository.findAllByCorrectAnswer(postId);
    }

    public List<Tuple> findCountOrderBySolved(int count){
        return memberRepository.findCountOrderBySolved(count);
    }

//    public Member singIn(String username, String password){
//        return memberRepository.findOneByUsernameAndPassword(username, password);
//    }

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

    public boolean duplicateNicknameTest(String nickname){
        return memberRepository.duplicateNicknameTest(nickname);
    }
    public boolean duplicateUsernameTest(String username) {
        return memberRepository.duplicateUsernameTest(username);
    }
    
    @Transactional
    public void updatePassword(Long memberId, String password){
        Member member = memberRepository.findOne(memberId);
        member.setPassword(password);
    }


}
