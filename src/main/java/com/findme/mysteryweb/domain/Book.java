package com.findme.mysteryweb.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
public class Book {
    @Id
    @GeneratedValue
    @Column(name = "book_id")
    private Long id;

    @Column(columnDefinition = "TEXT")
    private String title;

    @Column(columnDefinition = "TEXT")
    private String author;

    @Column(columnDefinition = "TEXT")
    private String publisher;

    @Column(columnDefinition = "TEXT")
    private String plot;

    @Column(columnDefinition = "TEXT")
    private String thumbnail;

    private int recommendationCount;

    @OneToMany(mappedBy = "book", cascade = CascadeType.REMOVE)
    private List<Review> reviewList;

    @OneToMany(mappedBy = "book", cascade = CascadeType.REMOVE)
    private List<Recommendation> recommendationList;


    //==생성 메서드==//
    public static Book createBook(String title, String author, String publisher, String plot, String thumbnail){
        Book book = new Book();

        book.setTitle(title);
        book.setAuthor(author);
        book.setPublisher(publisher);
        book.setPlot(plot);
        book.setThumbnail(thumbnail);
        book.setRecommendationCount(0);

        return book;
    }

}
