package com.findme.mysteryweb.domain;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter @Setter
public class Post {

    @Id
    @GeneratedValue
    @Column(name = "post_id")
    private Long id;

    private LocalDateTime datetime;



    @Column(columnDefinition = "TEXT")
    private String title;

    @Column(columnDefinition = "TEXT")
    private String content;

    @Column(columnDefinition = "TEXT")
    private String type;

    @Column(columnDefinition = "TEXT")
    private String answer;

    @Column(columnDefinition = "TEXT")
    private String explanation;

    private int recommendationCount;

    private int viewCount;


    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "post", cascade = CascadeType.REMOVE)
    private List<Comment> commentList;

    @OneToMany(mappedBy = "post", cascade = CascadeType.REMOVE)
    private List<CorrectAnswer> correctAnswerList;

    @OneToMany(mappedBy = "post", cascade = CascadeType.REMOVE)
    private List<Recommendation> recommendationList;

    //== 생성 메서드 ==//
    public static Post createPost(String title, String content, String type, String answer, String explanation, Member member){
        Post post = new Post();
        post.setDatetime(LocalDateTime.now().plusHours(9));
        post.setTitle(title);
        post.setContent(content);
        post.setType(type);
        post.setAnswer(answer);
        post.setExplanation(explanation);
        post.setRecommendationCount(0);
        post.setMember(member);
        post.setViewCount(0);

        return post;
    }

    //== 비즈니스 로직 ==//


}
