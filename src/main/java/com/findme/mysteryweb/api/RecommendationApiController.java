package com.findme.mysteryweb.api;


import com.findme.mysteryweb.domain.Book;
import com.findme.mysteryweb.domain.Member;
import com.findme.mysteryweb.domain.Post;
import com.findme.mysteryweb.domain.Recommendation;
import com.findme.mysteryweb.service.BookService;
import com.findme.mysteryweb.service.MemberService;
import com.findme.mysteryweb.service.PostService;
import com.findme.mysteryweb.service.RecommendationService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class RecommendationApiController {
    private final RecommendationService recommendationService;
    private final MemberService memberService;
    private final PostService postService;
    private final BookService bookService;


    @PostMapping("/api/recommendation")
    public ResponseEntity<?> saveRecommendation(@RequestBody SaveRecommendationRequest request) {
        Member member = memberService.findOne(request.memberId);
        Post post = null;
        Book book = null;

        if (request.postId != null) {
            post = postService.findOne(request.postId);
            postService.increaseRecommendCount(request.postId);
        }
        if (request.bookId != null) {
            book = bookService.findOne(request.bookId);
            bookService.increaseRecommendCount(request.bookId);
        }

        recommendationService.save(Recommendation.createRecommendation(member, post, book));

        return ResponseEntity.ok("Recommendation completed");
    }

    @GetMapping("/api/recommendation")
    public ResponseEntity<?> getReCommendations(@ModelAttribute GetRecommendationRequest request) {
        Recommendation recommendation = recommendationService.findOneByMemberIdAndOtherId(request.memberId, request.postId, request.bookId);

        if(recommendation == null){
            return ResponseEntity.ok(false);
        }else{
            return ResponseEntity.ok(true);
        }
    }

    @GetMapping("/api/recommendations")
    public ResponseEntity<?> getReCommendationList(@ModelAttribute GetRecommendationListRequest request) {
        List<Recommendation> recommendationList = recommendationService.findAllByMemberId(request.memberId);

        if (recommendationList.isEmpty()) {
            return ResponseEntity.ok(new Result<>(Collections.EMPTY_LIST));
        } else {
            List<GetRecommendationListResponse> collect = recommendationList.stream()
                    .map(r -> {
                        if (r.getPost() != null) {
                            return new GetRecommendationListResponse(r.getPost().getId(), null);
                        } else {
                            return new GetRecommendationListResponse(null, r.getBook().getId());
                        }
                    })
                    .collect(Collectors.toList());

            return ResponseEntity.ok(new Result<>(collect));
        }

    }

    @DeleteMapping("/api/recommendation")
    public ResponseEntity<?> cancelRecommendation(@ModelAttribute CancelRecommendationRequest request) {
        recommendationService.delete(request.memberId, request.postId, request.bookId);
        if (request.postId != null) {
            postService.decreaseRecommendCount(request.postId);
        }
        if (request.bookId != null) {
            bookService.decreaseRecommendCount(request.bookId);
        }

        return ResponseEntity.ok("cancel completed");
    }


    //==DTO==//
    @Data
    @AllArgsConstructor
    static class Result<T> {
        private T data;
    }

    @Data
    static class SaveRecommendationRequest {
        private Long memberId;
        private Long postId;
        private Long bookId;
    }

    @Data
    static class GetRecommendationRequest{
        private Long memberId;
        private Long postId;
        private Long bookId;
    }

    @Data
    static class GetRecommendationListRequest {
        private Long memberId;
    }

    @Data
    @AllArgsConstructor
    static class GetRecommendationListResponse {
        private Long postId;
        private Long bookId;
    }

    @Data
    static class CancelRecommendationRequest {
        private Long memberId;
        private Long postId;
        private Long bookId;
    }

}
