package com.findme.mysteryweb.domain;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter @Setter
public class Post {

    @Id
    @GeneratedValue
    @Column(name = "post_id")
    private Long id;

    private LocalDateTime dataTime;

    @Column(columnDefinition = "TEXT")
    private String title;

    @Column(columnDefinition = "TEXT")
    private String content;

    @Column(columnDefinition = "TEXT")
    private String type;

    @Column(columnDefinition = "TEXT")
    private String answer;

    private int recommend;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "member_id")
    private Member member;

    @Enumerated(EnumType.STRING)
    private AnswerType answerType;

    //== 생성 메서드 ==//
    public static Post createPost(String title, String content, String type, String answer, AnswerType answerType, Member member){
        Post post = new Post();
        post.setDataTime(LocalDateTime.now());
        post.setTitle(title);
        post.setContent(content);
        post.setType(type);
        post.setAnswer(answer);
        post.setRecommend(0);
        post.setMember(member);
        post.setAnswerType(answerType);

        return post;
    }

    //== 비즈니스 로직 ==//


}
