package com.findme.mysteryweb.service;


import com.findme.mysteryweb.domain.Book;
import com.findme.mysteryweb.domain.Comment;
import com.findme.mysteryweb.domain.Member;
import com.findme.mysteryweb.domain.Review;
import com.findme.mysteryweb.repository.BookRepository;
import com.findme.mysteryweb.repository.MemberRepository;
import com.findme.mysteryweb.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final MemberRepository memberRepository;
    private final BookRepository bookRepository;


    @Transactional
    public void save(Long bookId, Long parentId, Long memberId, String content) {
        Member member = memberRepository.findOne(memberId);
        Book book = bookRepository.findOne(bookId);

        Review review;

        if(parentId == null){
            review = Review.createReview(content, null, book, member);

        }else{
            Review parent = reviewRepository.findOne(parentId);
            review = Review.createReview(content, parent, book, member);
        }
        reviewRepository.save(review);
    }

    public Review findOne(Long reviewId) {
        return reviewRepository.findOne(reviewId);
    }

    public List<Review> findAll() {
        return reviewRepository.findAll();
    }

    public List<Review> findAllByBookId(Long bookId){
        return reviewRepository.findAllByBookId(bookId);
    }

    @Transactional
    public void delete(Long bookId){
        reviewRepository.delete(bookId);
    }

    public List<Review> findReviewsWithAllRepliesByBookId(Long bookId) {
        List<Review> topLevelComments = reviewRepository.findAllTopLevelCommentsByBookId(bookId);
        topLevelComments.forEach(this::loadRepliesRecursively);
        return topLevelComments;
    }

    @Transactional
    public void loadRepliesRecursively(Review review) {
        List<Review> replies = reviewRepository.findRepliesByReviewId(review.getId());
        review.setReplies(replies);
        replies.forEach(this::loadRepliesRecursively); // 재귀적 호출
    }


}
