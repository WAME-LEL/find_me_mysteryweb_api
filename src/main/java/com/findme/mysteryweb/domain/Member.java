package com.findme.mysteryweb.domain;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

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
    private String email;
    private int ranking;

    private String AuthenticationCode;
    private LocalDateTime authenticationCodeExpireTime;
    private boolean activated;



    @OneToMany(mappedBy = "member")
    private List<Post> posts;

    @OneToMany(mappedBy = "member")
    private List<Comment> comments;

    @OneToMany(mappedBy = "member")
    private List<CorrectAnswer> correctAnswerList;

    @OneToMany(mappedBy = "member")
    private List<Recommendation> recommendationList;

    //== 생성 메서드 ==//
    public static Member createMember(String nickname, String username, String password){
        Member member = new Member();
        member.setNickname(nickname);
        member.setUsername(username);
        member.setPassword(password);
        member.setActivated(false);

        return member;
    }

}
