package com.findme.mysteryweb.domain;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
public class Member {

    @Id
    @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    private String nickname;
    private String username;
    private String password;

    //== 생성 메서드 ==//
    public static Member createMember(String nickname, String username, String password){
        Member member = new Member();
        member.setNickname(nickname);
        member.setUsername(username);
        member.setPassword(password);


        return member;
    }


}
