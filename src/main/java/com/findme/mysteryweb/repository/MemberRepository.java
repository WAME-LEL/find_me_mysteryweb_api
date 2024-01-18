package com.findme.mysteryweb.repository;


import com.findme.mysteryweb.domain.Member;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;

@Repository
@RequiredArgsConstructor
public class MemberRepository {

    private final EntityManager em;


    public Long save(Member member) {
        em.persist(member);

        return member.getId();
    }

    public void delete(Long memberId) {
        Member member = findOne(memberId);
        em.remove(member);
    }

    public Member findOne(Long memberId) {
        return em.find(Member.class, memberId);
    }

    public Member findOneByUsernameAndPassword(String username, String password) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        List<Member> memberList = em.createQuery("select m from Member m where username like :username", Member.class)
                .setParameter("username", username)
                .getResultList();


        if (!memberList.isEmpty()) {
            Member member = memberList.get(0);
            if (passwordEncoder.matches(password, member.getPassword())) {
                return member;
            }
        }
        return null;

    }
    public Member findOneByUsername(String username){
        List<Member> memberList = em.createQuery("select m from Member m where m.username like :username", Member.class)
                .setParameter("username", username)
                .getResultList();

        return memberList.isEmpty() ? null : memberList.get(0);
    }

    public Member findOneByEmail(String email){
        List<Member> resultList = em.createQuery("select m from Member m where m.email like :email", Member.class)
                .setParameter("email", email)
                .getResultList();

        if(resultList.isEmpty()){
            return null;
        }else{
            return resultList.get(0);
        }
    }

    public Member findOneByEmailAndUsername(String email, String username){
        List<Member> memberList = em.createQuery("select m from Member m where m.email like :email and m.username like :username", Member.class)
                .setParameter("email", email)
                .setParameter("username", username)
                .getResultList();

        return memberList.isEmpty() ? null : memberList.get(0);
    }

    public List<Member> findAll() {
        return em.createQuery("select m from Member m", Member.class)
                .getResultList();
    }

    public List<Member> findAllOrderBySolved() {
        return em.createQuery("select m from Member m order by m.solved desc", Member.class)
                .getResultList();
    }

    public List<Member> findCountOrderBySolved(int count) {
        return em.createQuery("select m from Member m order by m.solved desc", Member.class)
                .setMaxResults(count)
                .getResultList();
    }

    public boolean duplicateNicknameTest(String nickname) {
        List<Member> memberList = em.createQuery("select m from Member m where m.nickname like :nickname", Member.class)
                .setParameter("nickname", nickname)
                .getResultList();

        return !memberList.isEmpty();
    }

    public boolean duplicateUsernameTest(String username) {
        List<Member> memberList = em.createQuery("select m from Member m where m.username like :username", Member.class)
                .setParameter("username", username)
                .getResultList();

        return !memberList.isEmpty();
    }

    public void clearStore() {
        em.clear();
    }


}
