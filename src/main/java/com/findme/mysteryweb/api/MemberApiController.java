package com.findme.mysteryweb.api;


import com.findme.mysteryweb.domain.Member;
import com.findme.mysteryweb.service.MemberService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class MemberApiController {
    private final MemberService memberService;

    @PostMapping("/api/signup")
    public ResponseEntity<?> signUp(@Valid @RequestBody SignUpRequest request, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return ResponseEntity.badRequest().body(bindingResult.getAllErrors());
        }

        Member member = Member.createMember(request.nickname, request.username, request.password);
        memberService.save(member);

        return ResponseEntity.ok("sign up completed");
    }

    //로그인같은 중요한 요청은 쿼리파라미터로 보이지 않게 post로 보냄
    @PostMapping("/api/signin")
    public ResponseEntity<?> signIn(@Valid @RequestBody SignInRequest request, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return ResponseEntity.badRequest().body(bindingResult.getAllErrors());
        }

        Member member = memberService.singIn(request.username, request.password);
        return ResponseEntity.ok(new Result<>(new SignInResponse(member.getId(), member.getNickname(), member.isActivated())));
    }

    @GetMapping("/api/user")
    public ResponseEntity<?> userInfo(@ModelAttribute UserInfoRequest request){
        Member member = memberService.findOne(request.memberId);
        List<PostDto> postList = member.getPosts().stream()
                .map(p -> new PostDto(p.getId(), p.getTitle(), p.getContent(), p.getType(), p.getRecommend(), p.getDataTime()))
                .collect(Collectors.toList());

        List<CommentDto> commentList = member.getComments().stream()
                .map(c -> new CommentDto(c.getId(), c.getContent(), c.getRecommend(), c.getDateTime(), c.getPost().getId()))
                .collect(Collectors.toList());

        UserInfoResponse userInfo = new UserInfoResponse(member.getNickname(), postList, commentList);


        return ResponseEntity.ok(new Result<>(userInfo));
    }


    //==DTO==//

    @Data
    @AllArgsConstructor
    static class Result<T>{
        private T data;
    }

    @Data
    static class SignUpRequest{
        private String nickname;
        private String username;
        private String password;
    }

    @Data
    static class SignInRequest{
        private String username;
        private String password;
    }

    @Data
    @AllArgsConstructor
    static class SignInResponse{
        private Long memberId;
        private String nickname;
        private boolean activated;
    }

    @Data
    static class UserInfoRequest{
        private Long memberId;
    }

    @Data
    @AllArgsConstructor
    static class UserInfoResponse{
        private String nickname;
        private List<PostDto> postList;
        private List<CommentDto> commentList;
    }

    @Data
    @AllArgsConstructor
    static class PostDto{
        private Long Id;
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
        private String content;
        private int recommend;
        private LocalDateTime dateTime;
        private Long postId;
    }



}
