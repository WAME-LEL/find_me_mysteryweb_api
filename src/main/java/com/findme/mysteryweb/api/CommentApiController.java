package com.findme.mysteryweb.api;


import com.findme.mysteryweb.domain.Comment;
import com.findme.mysteryweb.domain.Member;
import com.findme.mysteryweb.domain.Notification;
import com.findme.mysteryweb.domain.Post;
import com.findme.mysteryweb.service.CommentService;
import com.findme.mysteryweb.service.MemberService;
import com.findme.mysteryweb.service.NotificationService;
import com.findme.mysteryweb.service.PostService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = {"https://detectivesnight.com", "https://www.detectivesnight.com", "http://detectivesnight.com", "http://www.detectivesnight.com"})
public class CommentApiController {
    private final CommentService commentService;
    private final MemberService memberService;
    private final PostService postService;
    private final NotificationService notificationService;



    @GetMapping("/api/comment")
    public ResponseEntity<?> commentList(@ModelAttribute CommentListRequest request){
        List<Comment> commentList = commentService.findAllByPostId(request.postId);

        List<CommentListResponse> collect = commentList.stream()
                .map(c -> new CommentListResponse( c.getContent(), c.getDatetime(), c.getMember().getNickname(), c.getRecommend()))
                .collect(Collectors.toList());

        return ResponseEntity.ok(new Result<>(collect));
    }

    @PostMapping("/api/comment")
    public ResponseEntity<?> commentSave(@AuthenticationPrincipal UserDetails userDetails, @RequestBody CommentSaveRequest request){
        Member member = memberService.findOneByUsername(userDetails.getUsername());


        Post post = postService.findOne(request.postId);
        Comment comment = commentService.writeComment(request.postId, request.parentId, member.getId(), post.getMember().getId(), request.content);

//        Notification notification;
//
//        log.error(comment.getContent());
//
//        if(request.parentId == null){
//            notification = notificationService.findOneByReceiverId(post.getMember().getId());
//        }else{
//            notification = notificationService.findOneByReceiverId(comment.getParent().getMember().getId());
//        }
//
//
//        notificationService.sendNotification(notification);
//        log.error(notification.getMessage() + " : sender:" +notification.getSenderId() + " receiver:" + notification.getReceiverId());

        return ResponseEntity.ok("comment save completed");
    }

    @DeleteMapping("/api/comment")
    public ResponseEntity<?> commentDelete(@AuthenticationPrincipal UserDetails userDetails, @ModelAttribute CommentDeleteRequest request){
        Member member = memberService.findOneByUsername(userDetails.getUsername());
        Comment comment = commentService.findOne(request.commentId);

        if (!Objects.equals(member.getId(), comment.getMember().getId())) {
            return ResponseEntity.ok("No Permission");
        }
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
