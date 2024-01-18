package com.findme.mysteryweb.api;


import com.findme.mysteryweb.domain.Comment;
import com.findme.mysteryweb.domain.Member;
import com.findme.mysteryweb.domain.Post;
import com.findme.mysteryweb.jwt.JwtUtil;
import com.findme.mysteryweb.service.CommentService;
import com.findme.mysteryweb.service.MemberService;
import com.findme.mysteryweb.service.PostService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@Slf4j
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class MemberApiController {
    private final MemberService memberService;
    private final PostService postService;
    private final CommentService commentService;

    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @PostMapping("/api/signup")
    public ResponseEntity<?> signUp(@Valid @RequestBody SignUpRequest request, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return ResponseEntity.badRequest().body(bindingResult.getAllErrors());
        }
        String hashedPassword = passwordEncoder.encode(request.password);

        Member member = Member.createMember(request.nickname, request.username, hashedPassword);
        memberService.save(member);

        return ResponseEntity.ok("sign up completed");
    }

    //로그인같은 중요한 요청은 쿼리파라미터로 보이지 않게 post로 보냄
    @PostMapping("/api/signin")
    public ResponseEntity<?> signIn(@Valid @RequestBody SignInRequest request, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return ResponseEntity.badRequest().body(bindingResult.getAllErrors());
        }

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
            );
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Authentication failed");
        }
//        Member member = memberService.singIn(request.username, request.password);

        Member member = memberService.findOneByUsername(request.username);
        String accessToken = jwtUtil.generateAccessToken(request.username);
        String refreshToken = jwtUtil.generateRefreshToken(request.username);

        return ResponseEntity.ok(new Result<>(new SignInResponse(member.getId(), member.getNickname(), member.isActivated(), accessToken, refreshToken)));

    }

    @GetMapping("/api/user/token")
    public ResponseEntity<?> getUserInfoByToken(@RequestHeader("Authorization") String authorizationHeader) {
        String token = authorizationHeader.substring(7);
        String username = jwtUtil.extractUsername(token);

        Member member = memberService.findOneByUsername(username);

        if(member != null){
            return ResponseEntity.ok(new Result<>(new GetUserInfoByToken(member.getId(), member.getNickname(), member.isActivated())));
        }else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }

    }

    @PostMapping("/api/token/refresh")
    public ResponseEntity<?> refreshAccessToken(@RequestBody RefreshTokenRequest refreshTokenRequest) {
        String refreshToken = refreshTokenRequest.getRefreshToken();
        // 리프레시 토큰의 유효성 검사
        if (!jwtUtil.validateRefreshToken(refreshToken)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid refresh token");
        }

        // 리프레시 토큰으로부터 사용자 식별
        String username = jwtUtil.extractUsername(refreshToken);

        // 새로운 액세스 토큰, 리프레시 토큰 발급
        String newAccessToken = jwtUtil.generateAccessToken(username);
        String newRefreshToken = jwtUtil.generateRefreshToken(username);

        return ResponseEntity.ok(new Result<>(new RefreshTokenResponse(newAccessToken, newRefreshToken)));
    }

    @GetMapping("/api/user")
    public ResponseEntity<?> userInfo(@AuthenticationPrincipal UserDetails userDetails){
        Member member = memberService.findOneByUsername(userDetails.getUsername());
        List<Post> postList = postService.findAllByMemberId(member.getId());
        List<Comment> commentList = commentService.findAllByMemberId(member.getId());
        List<PostDto> postListCollect = postList.stream()
                .map(p -> new PostDto(p.getId(), p.getTitle(), p.getContent(), p.getType(), p.getRecommendationCount(), p.getDatetime(), p.getCommentList().size()))
                .collect(Collectors.toList());

        List<CommentDto> commentListCollect = commentList.stream()
                .map(c -> new CommentDto(c.getId(), c.getContent(), c.getRecommend(), c.getDatetime(), c.getPost().getId()))
                .collect(Collectors.toList());


        UserInfoResponse userInfo = new UserInfoResponse(member.getNickname(), member.getEmail(), member.getSolved(), member.getPosts().size(), member.getComments().size(), postListCollect, commentListCollect);


        return ResponseEntity.ok(new Result<>(userInfo));
    }

    @GetMapping("/api/member/rank")
    public ResponseEntity<?> userRank(@RequestParam(value = "count", required = false) Integer count){
        List<Member> orderBySolved;

        if (count != null) {
            orderBySolved = memberService.findFiveOrderBySolved(count);
        } else {
            orderBySolved = memberService.findAllOrderBySolved();
        }

        List<UserRankResponse> collect = orderBySolved.stream()
                .map(m -> new UserRankResponse(m.getNickname(), m.getSolved(), m.getPosts().size(), m.getComments().size()))
                .collect(Collectors.toList());


        return ResponseEntity.ok(new Result<>(collect));
    }

    @GetMapping("/api/find/username")
    public ResponseEntity<?> findUsername(@ModelAttribute FindUsernameRequest request){
        Member member = memberService.findOneByEmail(request.email);

        if(member != null){
            return ResponseEntity.ok(new Result<>(new FindUsernameResponse(member.getUsername())));
        }else{
            return ResponseEntity.ok(false);
        }
    }

    @GetMapping("/api/check/password")
    public ResponseEntity<?> checkUpdatePassword(@ModelAttribute CheckUpdatePasswordRequest request){
        Member member = memberService.findOneByEmailAndUsername(request.email, request.username);

        if(member == null){
            return ResponseEntity.ok(false);
        }else{
            return ResponseEntity.ok(new Result<>(new CheckUpdatePasswordResponse(member.getId())));
        }
    }

    @PutMapping("/api/update/password")
    public ResponseEntity<?> updatePassword(@RequestBody UpdatePasswordRequest request){
        String hashedPassword = passwordEncoder.encode(request.password);

        memberService.updatePassword(request.memberId, hashedPassword);

        return ResponseEntity.ok("Update completed");
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
        private String accessToken;
        private String refreshToken;
    }

    @Data
    static class UserInfoRequest{
        private Long memberId;
    }

    @Data
    @AllArgsConstructor
    static class UserInfoResponse{
        private String nickname;
        private String email;
        private int solved;
        private int postCount;
        private int commentCount;
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
        private int commentCount;
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


    @Data
    @AllArgsConstructor
    static class UserRankResponse{
        private String nickname;
        private int solved;
        private int postCount;
        private int commentCount;
    }

    @Data
    static class FindUsernameRequest{
        private String email;
    }

    @Data
    @AllArgsConstructor
    static class FindUsernameResponse{
        private String username;
    }

    @Data
    static class CheckUpdatePasswordRequest {
        private String email;
        private String username;
    }

    @Data
    @AllArgsConstructor
    static class CheckUpdatePasswordResponse{
        private Long memberId;
    }

    @Data
    static class UpdatePasswordRequest{
        private Long memberId;
        private String password;
    }

    @Data
    @AllArgsConstructor
    static class GetUserInfoByToken{
        private Long memberId;
        private String nickname;
        private boolean activated;
    }

    @Data
    static class RefreshTokenRequest {
        private String refreshToken;
    }

    @Data
    @AllArgsConstructor
    static class RefreshTokenResponse{
        private String accessToken;
        private String refreshToken;
    }



}
