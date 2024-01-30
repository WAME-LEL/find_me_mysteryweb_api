package com.findme.mysteryweb.domain;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@Slf4j
public class Review {
    @Id
    @GeneratedValue
    @Column(name = "review_id")
    private Long id;

    @Column(columnDefinition = "TEXT")
    private String content;

    private LocalDateTime datetime;

    private int recommendationCount;

//    @OneToMany(mappedBy = "review")
//    private List<Recommendation> recommendationList;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "book_id")
    private Book book;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "parent_id")
    private Review parent;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.REMOVE)
    private List<Review> replies;


    //==생성 메서드==//

    public static Review createReview(String content, Review parent, Book book, Member member){
        Review review = new Review();
        review.setContent(content);
        review.setParent(parent);
        review.setBook(book);
        review.setMember(member);
        review.setDatetime(LocalDateTime.now().plusHours(9));
        review.setRecommendationCount(0);

        return review;
    }

}
