package com.findme.mysteryweb.api;


import com.findme.mysteryweb.domain.Comment;
import com.findme.mysteryweb.service.CommentService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class CommentApiController {
    private final CommentService commentService;

    @GetMapping("/api/comment")
    public ResponseEntity<?> commentList(@ModelAttribute CommentListRequest request){
        List<Comment> commentList = commentService.findAllByPostId(request.postId);

        List<CommentListResponse> collect = commentList.stream()
                .map(c -> new CommentListResponse( c.getContent(), c.getDatetime(), c.getMember().getNickname(), c.getRecommend()))
                .collect(Collectors.toList());

        return ResponseEntity.ok(new Result<>(collect));
    }

    @PostMapping("/api/comment")
    public ResponseEntity<?> commentSave(@RequestBody CommentSaveRequest request){
        commentService.writeComment(request.postId, request.parentId, request.memberId, request.content);

        return ResponseEntity.ok("comment save completed");
    }

    @DeleteMapping("/api/comment")
    public ResponseEntity<?> commentDelete(@ModelAttribute CommentDeleteRequest request){
        commentService.delete(request.commentId);

        return ResponseEntity.ok("comment delete completed");
    }

    @GetMapping("/api/comments")
    public ResponseEntity<?> getCommentsWithAllRepliesByPostId(@ModelAttribute GetCommentsWithAllRepliesRequest request) {
        List<Comment> comments = commentService.findCommentsWithAllRepliesByPostId(request.postId);

        List<CommentDto> collect = comments.stream()
                .map(c -> toDto(c))
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
    static class CommentSaveRequest{
        private Long postId;
        private Long parentId;
        private Long memberId;
        private String content;
    }

    @Data
    static class CommentDeleteRequest{
        private Long commentId;
    }

    @Data
    static class CommentListRequest{
        private Long postId;
    }

    @Data
    @AllArgsConstructor
    static class CommentListResponse{
        private String content;
        private LocalDateTime dateTime;
        private String nickname;
        private Integer recommend;
    }

    @Data
    static class GetCommentsWithAllRepliesRequest{
        private Long postId;
    }


    @Data
    @AllArgsConstructor
    static class CommentDto {
        private Long memberId;
        private Long commentId;
        private String nickname;
        private String content;
        private LocalDateTime datetime;
        private List<CommentDto> replies;
    }

    //==변환 메서드==//
    private CommentDto toDto(Comment comment) {
        List<CommentDto> replies = comment.getReplies().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
        return new CommentDto(comment.getMember().getId(), comment.getId(), comment.getMember().getNickname(), comment.getContent(), comment.getDatetime(), replies);
    }







}
