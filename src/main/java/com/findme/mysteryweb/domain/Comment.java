package com.findme.mysteryweb.domain;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class Comment {

    @Id
    @GeneratedValue
    @Column(name = "comment_id")
    private Long id;

    private LocalDateTime dateTime;

    private int recommend;

    @Column(columnDefinition = "TEXT")
    private String content;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "member_id")
    private Member member;


    //== 생성 메서드 ==//
    public static Comment createComment(Post post, Member member, String content){
        Comment comment = new Comment();
        comment.setPost(post);
        comment.setMember(member);
        comment.setDateTime(LocalDateTime.now());
        comment.setRecommend(0);

        comment.setContent(content);

        return comment;
    }


}
