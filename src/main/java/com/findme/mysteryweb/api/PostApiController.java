package com.findme.mysteryweb.api;


import com.amazonaws.services.s3.AmazonS3;
import com.findme.mysteryweb.domain.AnswerType;
import com.findme.mysteryweb.domain.Comment;
import com.findme.mysteryweb.domain.Post;
import com.findme.mysteryweb.service.CommentService;
import com.findme.mysteryweb.service.PostService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class PostApiController {
    private final PostService postService;
    private final CommentService commentService;

    private final AmazonS3 amazonS3;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @GetMapping("/api/post")
    public ResponseEntity<?> getPost(@ModelAttribute GetPostRequest request){
        Post post = postService.findOne(request.postId);

        PostDto postDto = new PostDto(post.getId(), post.getMember().getNickname(), post.getTitle(), post.getContent(), post.getType(), post.getRecommend(), post.getDataTime());

        List<Comment> commentList = commentService.findAllByPostId(post.getId());

        List<CommentDto> collect = commentList.stream()
                .map(c -> new CommentDto(c.getId(), c.getMember().getId(), c.getMember().getNickname(), c.getContent(), c.getRecommend(), c.getDateTime()))
                .collect(Collectors.toList());


        return ResponseEntity.ok(new Result<>(new GetPostResponse(postDto, collect)));
    }

    @GetMapping("/api/posts")
    public ResponseEntity<?> postList(@ModelAttribute PostListRequest request) {
        List<Post> postList;

        String type = request.type;
        String title = request.title;

        if (type != null && !type.isEmpty() && request.getTitle() != null && !request.getTitle().isEmpty()) {
            // 여기서는 type과 title이 모두 제공되었을 때의 처리 방식을 정의합니다.
            // 예를 들어, 두 조건을 모두 만족하는 Post를 찾을 수 있는 서비스 메소드를 호출할 수 있습니다.
            postList = postService.findAllByTypeAndTitle(type, title);
        } else if (type != null && !type.isEmpty()) {
            postList = postService.findAllByType(type);
        } else if (title != null && !title.isEmpty()) {
            postList = postService.findAllByTitle(title);
        } else {
            postList = postService.findAll();
        }

        List<PostListResponse> collect = postList.stream()
                .map(p -> new PostListResponse(p.getId(), p.getTitle(), p.getContent(), p.getMember().getId(), p.getMember().getNickname(), p.getType(), p.getDataTime(), p.getRecommend()))
                .collect(Collectors.toList());

        return ResponseEntity.ok(new Result<>(collect));
    }


    @PostMapping("/api/post")
    public ResponseEntity<?> posting(@RequestBody @Valid PostingRequest request, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(bindingResult.getAllErrors());
        }

        postService.posting(request.memberId, request.title, request.content, request.type, request.answer, request.answerType);

        return ResponseEntity.ok("Posting completed");

    }

    @DeleteMapping("/api/post")
    public ResponseEntity<?> postDelete(@ModelAttribute PostDeleteRequest request){
        Post post = postService.findOne(request.postId);
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
        private String nickname;
        private String title;
        private String content;
        private String type;
        private int recommend;
        private LocalDateTime dataTime;
    }

    @Data
    @AllArgsConstructor
    static class CommentDto{
        private Long commentId;
        private Long memberId;
        private String nickname;
        private String content;
        private int recommend;
        private LocalDateTime dateTime;
    }



    @Data
    static class PostingRequest {
        private Long memberId;
        private String title;
        private String content;
        private String answer;
        private String type;
        private AnswerType answerType;
    }

    @Data
    static class PostListRequest{
        private String type;
        private String title;
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
        private LocalDateTime dateTime;
        private Integer recommend;
    }

    @Data
    static class PostDeleteRequest{
        private Long postId;
    }


}
