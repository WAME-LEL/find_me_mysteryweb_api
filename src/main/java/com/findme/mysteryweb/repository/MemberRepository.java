package com.findme.mysteryweb.repository;


import com.findme.mysteryweb.domain.Member;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class MemberRepository {

    private final EntityManager em;


    public Long save(Member member){
        em.persist(member);

        return member.getId();
    }

    public void delete(Long memberId){
        Member member = findOne(memberId);
        em.remove(member);
    }

    public Member findOne(Long memberId){
        return em.find(Member.class, memberId);
    }

    public Member findOneByUsernameAndPassword(String username, String password){
        return em.createQuery("select m from Member m where username like :username and password like :password", Member.class)
                .setParameter("username", username)
                .setParameter("password", password)
                .getSingleResult();

    }

    public List<Member> findAll(){
        return em.createQuery("select m from Member m", Member.class)
                .getResultList();
    }

    public void clearStore(){
        em.clear();
    }

}
