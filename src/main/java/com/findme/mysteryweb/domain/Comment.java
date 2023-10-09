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

    private LocalDateTime comment_date;
    private String comment_content;
    private int comment_recommend;


    @ManyToOne(fetch = FetchType.LAZY)
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;


    //== 생성 메서드 ==//
    public static Comment createComment(Member member, String comment_content){
        Comment comment = new Comment();
        comment.setComment_date(LocalDateTime.now());
        comment.setComment_recommend(0);
        comment.setComment_content(comment_content);

        return comment;
    }


}
