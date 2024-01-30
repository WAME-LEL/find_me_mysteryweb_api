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
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = {"https://detectivesnight.com", "https://www.detectivesnight.com", "http://detectivesnight.com", "http://www.detectivesnight.com"})
public class RecommendationApiController {
    private final RecommendationService recommendationService;
    private final MemberService memberService;
    private final PostService postService;
    private final BookService bookService;


    @PostMapping("/api/recommendation")
    public ResponseEntity<?> saveRecommendation(@AuthenticationPrincipal UserDetails userDetails, @RequestBody SaveRecommendationRequest request) {
        Member member = memberService.findOneByUsername(userDetails.getUsername());
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
    public ResponseEntity<?> getRecommendations(@ModelAttribute GetRecommendationRequest request) {
        Recommendation recommendation = recommendationService.findOneByMemberIdAndOtherId(request.memberId, request.postId, request.bookId);

        if(recommendation == null){
            return ResponseEntity.ok(false);
        }else{
            return ResponseEntity.ok(true);
        }
    }

    @GetMapping("/api/recommendations")
    public ResponseEntity<?> getReCommendationList(@ModelAttribute GetRecommendationListRequest request) {
        List<Recommendation> recommendationList;
        if(Objects.equals(request.recommendationType, "book")){
            recommendationList = recommendationService.findAllByMemberIdAtBookOrPost(request.memberId, request.recommendationType);

            List<GetRecommendationBookListResponse> collect = recommendationList.stream()
                    .map(r -> new GetRecommendationBookListResponse(r.getBook().getId())).collect(Collectors.toList());

            return ResponseEntity.ok(new Result<>(collect));
        }else{
            recommendationList = recommendationService.findAllByMemberIdAtBookOrPost(request.memberId, request.recommendationType);

            List<GetRecommendationPostListResponse> collect = recommendationList.stream()
                    .map(r -> new GetRecommendationPostListResponse(r.getPost().getId())).collect(Collectors.toList());

            return ResponseEntity.ok(new Result<>(collect));
        }

    }

    @DeleteMapping("/api/recommendation")
    public ResponseEntity<?> cancelRecommendation(@AuthenticationPrincipal UserDetails userDetails, @ModelAttribute CancelRecommendationRequest request) {
        Member member = memberService.findOneByUsername(userDetails.getUsername());
        if (!Objects.equals(member.getId(), request.memberId)){
            return ResponseEntity.ok("No permission");
        }

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
    static class GetRecommendationListRequest{
        private Long memberId;
        private String recommendationType;
    }
    @Data
    @AllArgsConstructor
    static class GetRecommendationBookListResponse {
        private Long bookId;
    }

    @Data
    @AllArgsConstructor
    static class GetRecommendationPostListResponse {
        private Long postId;
    }

    @Data
    static class CancelRecommendationRequest {
        private Long memberId;
        private Long postId;
        private Long bookId;
    }

}
