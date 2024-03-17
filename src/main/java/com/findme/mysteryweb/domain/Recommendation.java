package com.findme.mysteryweb.domain;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Recommendation {
    @Id
    @GeneratedValue
    @Column(name = "recommendation_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id")
    private Book book;


    //==생성 메서드==//

    public static Recommendation createRecommendation(Member member, Post post, Book book){
        Recommendation recommendation = new Recommendation();
        recommendation.setMember(member);
        recommendation.setBook(book);
        recommendation.setPost(post);

        return recommendation;
    }

}
