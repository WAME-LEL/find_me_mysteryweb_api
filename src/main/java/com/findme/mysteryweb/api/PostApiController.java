package com.findme.mysteryweb.api;


import com.amazonaws.services.s3.AmazonS3;
import com.findme.mysteryweb.domain.Comment;
import com.findme.mysteryweb.domain.Member;
import com.findme.mysteryweb.domain.Post;
import com.findme.mysteryweb.service.CommentService;
import com.findme.mysteryweb.service.MemberService;
import com.findme.mysteryweb.service.PostService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = {"https://detectivesnight.com", "https://www.detectivesnight.com", "http://detectivesnight.com", "http://www.detectivesnight.com"})
public class PostApiController {
    private final PostService postService;
    private final CommentService commentService;
    private final MemberService memberService;

    private final AmazonS3 amazonS3;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @GetMapping("/api/post")
    public ResponseEntity<?> getPost(@ModelAttribute GetPostRequest request){
        Post post = postService.findOne(request.postId);
        postService.increaseViewCount(request.postId);

        PostDto postDto = new PostDto(post.getId(), post.getMember().getId(), post.getMember().getNickname(), post.getTitle(), post.getContent(), post.getType(), post.getDatetime(), post.getViewCount(), post.getRecommendationCount());

        List<Comment> commentList = commentService.findAllByPostId(post.getId());

        List<CommentDto> collect = commentList.stream()
                .map(c -> new CommentDto(c.getId(), c.getMember().getId(), c.getMember().getNickname(), c.getContent(), c.getRecommend(), c.getDatetime()))
                .collect(Collectors.toList());


        return ResponseEntity.ok(new Result<>(new GetPostResponse(postDto, collect)));
    }

    @GetMapping("/api/post/update")
    public ResponseEntity<?> getPostToUpdate(@ModelAttribute GetPostRequest request){
        Post post = postService.findOne(request.postId);
        postService.increaseViewCount(request.postId);

        UpdatePostDto UpdatePostDto = new UpdatePostDto(post.getId(), post.getMember().getId(), post.getMember().getNickname(), post.getTitle(), post.getContent(), post.getType(), post.getAnswer(), post.getExplanation(), post.getDatetime(), post.getViewCount(), post.getRecommendationCount());

        List<Comment> commentList = commentService.findAllByPostId(post.getId());

        List<CommentDto> collect = commentList.stream()
                .map(c -> new CommentDto(c.getId(), c.getMember().getId(), c.getMember().getNickname(), c.getContent(), c.getRecommend(), c.getDatetime()))
                .collect(Collectors.toList());

        return ResponseEntity.ok(new Result<>(new GetPostToUpdateResponse(UpdatePostDto, collect)));
    }

    @GetMapping("/api/posts")
    public ResponseEntity<?> postList(@ModelAttribute PostListRequest request) {
        List<Post> postList;
        Integer count = request.getCount();

        String type = request.type;
        String searchTerm = request.searchTerm;
        String searchBy = request.searchBy;


        if (type != null && !type.isEmpty() && searchTerm != null && !searchTerm.isEmpty()) {
            // 여기서는 type과 title이 모두 제공되었을 때의 처리 방식을 정의합니다.
            // 예를 들어, 두 조건을 모두 만족하는 Post를 찾을 수 있는 서비스 메소드를 호출할 수 있습니다.
            if(Objects.equals(searchBy, "title")){
                postList = postService.findAllByTypeAndTitle(type, searchTerm);
            } else if (Objects.equals(searchBy, "author")) {
                postList = postService.findAllByTypeAndAuthor(type, searchTerm);
            }else if(Objects.equals(searchBy, "titleOrContent")){
                postList = postService.findAllByTypeAndTitleOrContent(type, searchTerm, searchTerm);
            }else{
                postList = postService.findAll();
            }

        } else if (type != null && !type.isEmpty()) {
            postList = postService.findAllByType(type);
        } else if (searchTerm != null && !searchTerm.isEmpty()) {
            if(Objects.equals(searchBy, "title")){
                postList = postService.findAllByTitle(searchTerm);
            }else if(Objects.equals(searchBy, "author")) {
                postList = postService.findAllByAuthor(searchTerm);
            }else{
                postList = postService.findAll();
            }

        } else {
            if(count != null){
                postList = postService.findCountOrderByRecommendationCount(count);
            }else{
                postList = postService.findAll();
            }

        }

        List<PostListResponse> collect = postList.stream()
                .map(p -> new PostListResponse(p.getId(), p.getTitle(), p.getContent(), p.getMember().getId(), p.getMember().getNickname(), p.getType(), p.getDatetime(), p.getCommentList().size(), p.getViewCount(), p.getRecommendationCount()))
                .collect(Collectors.toList());

        return ResponseEntity.ok(new Result<>(collect));
    }

    @GetMapping("/api/post/home")
    public ResponseEntity<?> homePostList(@ModelAttribute PostListRequest request) {
        List<Post> postList = postService.findCountOrderByDatetime(request.type, request.count);

        List<PostListResponse> collect = postList.stream()
                .map(p -> new PostListResponse(p.getId(), p.getTitle(), p.getContent(), p.getMember().getId(), p.getMember().getNickname(), p.getType(), p.getDatetime(), p.getCommentList().size(), p.getViewCount(), p.getRecommendationCount()))
                .collect(Collectors.toList());

        return ResponseEntity.ok(new Result<>(collect));
    }



    @PostMapping("/api/post")
    public ResponseEntity<?> posting(@AuthenticationPrincipal UserDetails userDetails, @RequestBody @Valid PostingRequest request, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(bindingResult.getAllErrors());
        }
        Member member = memberService.findOneByUsername(userDetails.getUsername());

        postService.posting(member.getId(), request.title, request.content, request.type, request.answer, request.explanation);

        return ResponseEntity.ok("Posting completed");

    }
    @PutMapping("/api/post")
    public ResponseEntity<?> editPosting(@AuthenticationPrincipal UserDetails userDetails, @RequestBody @Valid PostingRequest request, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(bindingResult.getAllErrors());
        }
        Member member = memberService.findOneByUsername(userDetails.getUsername());
        Member postMember = memberService.findOneByPostId(request.postId);

        if(Objects.equals(member, postMember)){
            postService.update(request.postId, request.title, request.content, request.answer, request.explanation, request.type);
            return ResponseEntity.ok("Posting completed");
        }else{
            return ResponseEntity.ok("other member");
        }

    }

    @DeleteMapping("/api/post")
    public ResponseEntity<?> postDelete(@AuthenticationPrincipal UserDetails userDetails, @ModelAttribute PostDeleteRequest request){
        Post post = postService.findOne(request.postId);
        Member member = memberService.findOneByUsername(userDetails.getUsername());
        if(!Objects.equals(post.getMember().getId(), member.getId())){
            return ResponseEntity.ok("No permission");
        }

        String content = post.getContent();
        Document doc = Jsoup.parse(content);
        Elements imgElements = doc.select("img");

        try {
            postService.delete(request.postId);
            for (Element img : imgElements) {
                String src = img.attr("src");
                System.out.println("Image Source: " + src);

                String stringToRemove = "https://detectivesnight.s3.ap-northeast-2.amazonaws.com/";
                String fileName = src.replace(stringToRemove, "");
                amazonS3.deleteObject(bucket, fileName);
            }

            return ResponseEntity.ok("post delete completed");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error deleting file");
        }
    }

    @GetMapping("/api/quiz/rank")
    public ResponseEntity<?> quizRank(@ModelAttribute QuizRankRequest request){
        List<Post> postList;

        if (Objects.equals(request.criteria, "조회")){
            postList = postService.findAllOrderByViewCount(request.type);
        }else if(Objects.equals(request.criteria, "추천")){
            postList = postService.findAllOrderByRecommendationCount(request.type);
        }else{
            postList = postService.findAll();
        }

        List<PostDto> collect = postList.stream()
                .map(p -> new PostDto(p.getId(), p.getMember().getId(), p.getMember().getNickname(), p.getTitle(), p.getContent(), p.getType(), p.getDatetime(), p.getViewCount(), p.getRecommendationCount()))
                .collect(Collectors.toList());

        return ResponseEntity.ok(new Result<>(collect));
    }

    @GetMapping("/api/quiz/rank/count")
    public ResponseEntity<?> quizRankByCount(@ModelAttribute QuizRankByCountRequest request){
        List<Post> postList = postService.findCountByTypeOrderByRecommendationCount(request.type, request.count);

        List<PostListResponse> collect = postList.stream()
                .map(p -> new PostListResponse(p.getId(), p.getTitle(), p.getContent(), p.getMember().getId(), p.getMember().getNickname(), p.getType(), p.getDatetime(), p.getCommentList().size(), p.getViewCount(), p.getRecommendationCount()))
                .collect(Collectors.toList());

        return ResponseEntity.ok(new Result<>(collect));
    }



    //==DTO==//

    @Data
    @AllArgsConstructor
    static class Result<T> {
        private T data;
    }

    @Data
    static class GetPostRequest{
        private Long postId;
    }

    @Data
    @AllArgsConstructor
    static class GetPostResponse{
        private PostDto post;
        private List<CommentDto> commentList;
    }

    @Data
    @AllArgsConstructor
    static class PostDto{
        private Long Id;
        private Long memberId;
        private String nickname;
        private String title;
        private String content;
        private String type;
        private LocalDateTime datetime;
        private int viewCount;
        private int recommendationCount;
    }

    @Data
    @AllArgsConstructor
    static class UpdatePostDto{
        private Long Id;
        private Long memberId;
        private String nickname;
        private String title;
        private String content;
        private String type;
        private String answer;
        private String explanation;
        private LocalDateTime datetime;
        private int viewCount;
        private int recommendationCount;
    }

    @Data
    @AllArgsConstructor
    static class GetPostToUpdateResponse{
        private UpdatePostDto post;
        private List<CommentDto> commentList;
    }


    @Data
    @AllArgsConstructor
    static class CommentDto{
        private Long commentId;
        private Long memberId;
        private String nickname;
        private String content;
        private int recommend;
        private LocalDateTime datetime;
    }



    @Data
    static class PostingRequest {
        private Long postId;
        private String title;
        private String content;
        private String answer;
        private String explanation;
        private String type;
    }

    @Data
    static class PostListRequest{
        private String type;
        private String searchTerm;
        private String searchBy;
        private Integer count;
    }

    @Data
    @AllArgsConstructor
    static class PostListResponse{
        private Long postId;
        private String title;
        private String content;
        private Long memberId;
        private String nickname;
        private String type;
        private LocalDateTime datetime;
        private int commentCount;
        private int viewCount;
        private int recommendationCount;
    }

    @Data
    static class PostDeleteRequest{
        private Long postId;
    }

    @Data
    static class QuizRankRequest{
        private String criteria;
        private String type;
    }

    @Data
    static class QuizRankByCountRequest{
        private String type;
        private int count;
    }


}
