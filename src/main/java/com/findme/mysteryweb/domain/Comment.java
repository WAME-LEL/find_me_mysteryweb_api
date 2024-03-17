package com.findme.mysteryweb.domain;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class Comment {

    @Id
    @GeneratedValue
    @Column(name = "comment_id")
    private Long id;

    private LocalDateTime datetime;

    private int recommend;

    @Column(columnDefinition = "TEXT")
    private String content;

    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;


    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Comment parent;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.REMOVE)
    private List<Comment> replies;


    //== 생성 메서드 ==//
    public static Comment createComment(Post post, Comment parent, Member member, String content){
        Comment comment = new Comment();
        comment.setPost(post);
        comment.setParent(parent);
        comment.setMember(member);
        comment.setDatetime(LocalDateTime.now());
        comment.setRecommend(0);

        comment.setContent(content);

        return comment;
    }


}
