package com.findme.mysteryweb.api;


import com.findme.mysteryweb.domain.Book;
import com.findme.mysteryweb.domain.Comment;
import com.findme.mysteryweb.domain.Member;
import com.findme.mysteryweb.domain.Review;
import com.findme.mysteryweb.service.BookService;
import com.findme.mysteryweb.service.MemberService;
import com.findme.mysteryweb.service.ReviewService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = {"https://detectivesnight.com", "https://www.detectivesnight.com", "http://detectivesnight.com", "http://www.detectivesnight.com"})
public class ReviewApiController {
    private final ReviewService reviewService;
    private final MemberService memberService;

    @PostMapping("/api/review")
    public ResponseEntity<?> writeReview(@AuthenticationPrincipal UserDetails userDetails, @RequestBody WriteReviewRequest request) {
        Member member = memberService.findOneByUsername(userDetails.getUsername());
        reviewService.save(request.bookId, request.parentId, member.getId(), request.content);

        return ResponseEntity.ok("Write review completed");
    }

    @GetMapping("/api/review")
    public ResponseEntity<?> reviewList(@ModelAttribute ReviewListRequest request){
        List<Review> reviewList = reviewService.findAllByBookId(request.bookId);

        List<ReviewListResponse> collect = reviewList.stream()
                .map(r -> new ReviewListResponse(r.getContent(), r.getMember().getNickname(), r.getDatetime()))
                .collect(Collectors.toList());

        return ResponseEntity.ok(new CommentApiController.Result<>(collect));
    }

    @DeleteMapping("/api/review")
    public ResponseEntity<?> deleteReview(@AuthenticationPrincipal UserDetails userDetails, @ModelAttribute DeleteReviewRequest request){
        Member member = memberService.findOneByUsername(userDetails.getUsername());
        Review review = reviewService.findOne(request.reviewId);
        if(!Objects.equals(member.getId(), review.getMember().getId())){
            return ResponseEntity.ok("No permission");
        }

        reviewService.delete(request.reviewId);

        return ResponseEntity.ok("Delete review completed");
    }

    @GetMapping("/api/reviews")
    public ResponseEntity<?> getReviewsWithAllRepliesByBookId(@ModelAttribute GetReviewsWithAllRepliesRequest request) {
        List<Review> reviewList = reviewService.findReviewsWithAllRepliesByBookId(request.bookId);

        List<ReviewDto> collect = reviewList.stream()
                .map(r -> toDto(r))
                .collect(Collectors.toList());

        return ResponseEntity.ok(new Result<>(collect));
    }


    //==DTO==//
    @Data
    @AllArgsConstructor
    static class Result<T>{
        private T data;
    }


    @Data
    static class WriteReviewRequest{
        private Long bookId;
        private Long parentId;
        private String content;
    }

    @Data
    static class ReviewListRequest{
        private Long bookId;
    }

    @Data
    @AllArgsConstructor
    static class ReviewListResponse{
        private String content;
        private String nickname;
        private LocalDateTime dateTime;
    }

    @Data
    static class DeleteReviewRequest{
        private Long reviewId;
    }

    @Data
    static class GetReviewsWithAllRepliesRequest{
        private Long bookId;
    }

    @Data
    @AllArgsConstructor
    static class ReviewDto {
        private Long memberId;
        private Long reviewId;
        private String nickname;
        private String content;
        private LocalDateTime datetime;
        private List<ReviewDto> replies;
    }


    //==변환 메서드==//
    private ReviewDto toDto(Review review) {
        List<ReviewDto> replies = review.getReplies().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
        return new ReviewDto(review.getMember().getId(), review.getId(), review.getMember().getNickname(), review.getContent(), review.getDatetime(), replies);
    }



}
