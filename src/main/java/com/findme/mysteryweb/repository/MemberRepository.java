package com.findme.mysteryweb.repository;


import com.findme.mysteryweb.domain.Member;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Tuple;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Repository;

import java.util.List;

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

    public Member findOneByPostId(Long postId){
        List<Member> memberList = em.createQuery("select m from Member m left join Post p on m.id = p.member.id where p.id =:postId", Member.class)
                .setParameter("postId", postId)
                .getResultList();


        return memberList.isEmpty() ? null : memberList.get(0);
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

    public List<Tuple> findAllOrderBySolved() {
        return em.createQuery("select m, count(ca) from Member m left join m.correctAnswerList ca group by m order by count(ca) desc", Tuple.class)
                .getResultList();
    }

    public List<Tuple> findAllByCorrectAnswer(Long postId){
        return em.createQuery("select m, ca from Member m left join CorrectAnswer ca on m.id = ca.member.id where ca.post.id =:postId order by ca.datetime asc", Tuple.class)
                .setParameter("postId", postId)
                .getResultList();
    }

    public List<Tuple> findCountOrderBySolved(int count) {
        return em.createQuery("select m, count(ca) from Member m left join m.correctAnswerList ca group by m order by count(ca) desc", Tuple.class)
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
