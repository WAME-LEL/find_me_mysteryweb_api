package com.findme.mysteryweb.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class CorrectAnswerHistory {
    @Id
    @GeneratedValue
    @Column(name = "correct_answer_id")
    private Long id;

    private LocalDateTime datetime;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    private String answer;

    //==생성 메서드==//
    public static CorrectAnswerHistory createCorrectAnswerHistory (Member member, Post post, String answer){
        CorrectAnswerHistory correctAnswer = new CorrectAnswerHistory();
        correctAnswer.setMember(member);
        correctAnswer.setPost(post);
        correctAnswer.setAnswer(answer);
        correctAnswer.setDatetime(LocalDateTime.now());

        return correctAnswer;
    }

}
