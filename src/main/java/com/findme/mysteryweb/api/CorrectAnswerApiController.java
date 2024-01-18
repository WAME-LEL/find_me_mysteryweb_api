package com.findme.mysteryweb.api;


import com.findme.mysteryweb.domain.CorrectAnswer;
import com.findme.mysteryweb.domain.Post;
import com.findme.mysteryweb.service.CorrectAnswerService;
import com.findme.mysteryweb.service.MemberService;
import com.findme.mysteryweb.service.PostService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@Slf4j
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class CorrectAnswerApiController {
    private final CorrectAnswerService correctAnswerService;
    private final PostService postService;
    private final MemberService memberService;


    @GetMapping("/api/correct")
    public ResponseEntity<?> correctAnswer(@ModelAttribute CorrectAnswerRequest request){
        CorrectAnswer correctAnswer = correctAnswerService.findOneByPostAndMember(request.postId, request.memberId);
        if(correctAnswer == null){
            return ResponseEntity.ok(new Result<>(new CorrectAnswerResponse("false", "", "")));
        }else{
            String answer = postService.findOne(request.postId).getAnswer();
            String explanation = postService.findOne(request.postId).getExplanation();
            return ResponseEntity.ok(new Result<>(new CorrectAnswerResponse("true", answer, explanation)));
        }
    }

    @GetMapping("/api/corrects")
    public ResponseEntity<?> correctAnswerList(@ModelAttribute CorrectAnswerListRequest request){
        List<CorrectAnswer> correctAnswerList = correctAnswerService.findAllByMember(request.memberId);

        List<CorrectAnswerListResponse> collect = correctAnswerList.stream()
                .map(ca -> new CorrectAnswerListResponse(ca.getPost().getId(), ca.getPost().getTitle()))
                .collect(Collectors.toList());

        return ResponseEntity.ok(new Result<>(collect));
    }

    @PostMapping("/api/correct")
    public ResponseEntity<?> checkCorrectAnswer(@RequestBody checkCorrectAnswerRequest request){
        Post post = postService.findOne(request.postId);

        try{
            if(post.getAnswer().equals(request.answer)){
                correctAnswerService.save(request.memberId, request.postId);
                memberService.increaseSolved(request.memberId);
                return ResponseEntity.ok("true");
            }
            else{
                return ResponseEntity.ok("false");
            }

        }catch(Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }


    }

    //==DTO==//

    @Data
    @AllArgsConstructor
    static class Result<T>{
        private T data;
    }


    @Data
    static class CorrectAnswerListRequest{
        private Long memberId;
    }

    @Data
    @AllArgsConstructor
    static class CorrectAnswerListResponse{
        private Long postId;
        private String postTitle;
    }

    @Data
    static class CorrectAnswerRequest{
        private Long memberId;
        private Long postId;
    }

    @Data
    @AllArgsConstructor
    static class CorrectAnswerResponse{
        private String solved;
        private String correct;
        private String explanation;
    }


    @Data
    static class checkCorrectAnswerRequest{
        private Long postId;
        private Long memberId;
        private String answer;
    }

}
