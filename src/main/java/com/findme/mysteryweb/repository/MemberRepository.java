package com.findme.mysteryweb.repository;


import com.findme.mysteryweb.domain.Member;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class MemberRepository {

    private final EntityManager em;


    public void save(Member member){
        em.persist(member);
    }

    public void delete(Long memberId){
        Member member = findOne(memberId);
        em.remove(member);
    }

    public Member findOne(Long memberId){
        return em.find(Member.class, memberId);
    }

}
