package com.findme.mysteryweb.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class CorrectAnswer {
    @Id
    @GeneratedValue
    @Column(name = "correct_answer_id")
    private Long id;

    private LocalDateTime datetime;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    //==생성 메서드==//
    public static CorrectAnswer createCorrectAnswer (Member member, Post post){
        CorrectAnswer correctAnswer = new CorrectAnswer();
        correctAnswer.setMember(member);
        correctAnswer.setPost(post);
        correctAnswer.setDatetime(LocalDateTime.now().plusHours(9));

        return correctAnswer;
    }

}
