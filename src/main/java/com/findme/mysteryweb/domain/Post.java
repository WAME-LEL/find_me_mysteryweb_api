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

    private LocalDateTime post_date;

    private String post_title;

    private String post_content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    //== 생성 메서드 ==//
    public static Post createPost(String post_title, String post_content, Member member){
        Post post = new Post();
        post.setPost_date(LocalDateTime.now());
        post.setPost_title(post_title);
        post.setPost_content(post_content);
        post.setMember(member);

        return post;
    }

    //== 비즈니스 로직 ==//


}
