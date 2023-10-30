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

    private int comment_recommend;

    @Column(columnDefinition = "TEXT")
    private String comment_title;

    @Column(columnDefinition = "TEXT")
    private String comment_content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;


    //== 생성 메서드 ==//
    public static Comment createComment(Post post, Member member, String comment_title, String comment_content){
        Comment comment = new Comment();
        comment.setPost(post);
        comment.setMember(member);
        comment.setComment_date(LocalDateTime.now());
        comment.setComment_recommend(0);
        comment.setComment_title(comment_title);
        comment.setComment_content(comment_content);

        return comment;
    }


}
