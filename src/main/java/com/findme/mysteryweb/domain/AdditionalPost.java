package com.findme.mysteryweb.domain;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter @Setter
public class AdditionalPost {

    @Id @GeneratedValue
    @Column(name = "additional_id")
    private Long id;

    private LocalDateTime additionalPost_date;

    private String additionalPost_title;

    private String additionalPost_content;

    @ManyToOne(fetch = FetchType.LAZY)
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;


}
