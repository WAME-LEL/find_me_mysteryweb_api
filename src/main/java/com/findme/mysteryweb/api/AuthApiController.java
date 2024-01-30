package com.findme.mysteryweb.api;


import com.findme.mysteryweb.domain.Member;
import com.findme.mysteryweb.service.EmailService;
import com.findme.mysteryweb.service.MemberService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.security.SecureRandom;
import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = {"https://detectivesnight.com", "https://www.detectivesnight.com", "http://detectivesnight.com", "http://www.detectivesnight.com"})
public class AuthApiController {
    private final EmailService emailService;
    private final MemberService memberService;

    @PostMapping("/api/email/authentication")
    public ResponseEntity<?> emailAuthentication(@AuthenticationPrincipal UserDetails userDetails, @RequestBody EmailAuthenticationRequest request){
        if (memberService.findOneByEmail(request.email) != null){
            return ResponseEntity.ok(false);
        }

        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < 6; i++) {
            sb.append(random.nextInt(10)); // 0에서 9까지의 숫자를 생성
        }

        String AuthenticationCode = sb.toString();

        String content = "탐정의 밤 계정 이메일 인증 입니다.\n인증코드: " + AuthenticationCode;
        Member member = memberService.findOneByUsername(userDetails.getUsername());
        emailService.sendEMailAuthentication(request.email, "탐정의 밤 계정 이메일 인증", content);

        memberService.saveAuthenticationCode(member.getId(), request.email, AuthenticationCode);

        return ResponseEntity.ok("send email completed");
    }

    @PostMapping("/api/email/verify")
    public ResponseEntity<?> verifyEmail(@AuthenticationPrincipal UserDetails userDetails, @RequestBody VerifyEmailRequest request) {
        Member member = memberService.findOneByUsername(userDetails.getUsername());
        if (member != null &&
                member.getAuthenticationCode().equals(request.getCode()) &&
                member.getAuthenticationCodeExpireTime().isAfter(LocalDateTime.now())) {
            memberService.activateMember(member.getId());
            return ResponseEntity.ok("Email successfully verified.");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid or expired verification code.");
        }
    }

    @GetMapping("/api/nickname/duplicate")
    public ResponseEntity<?> duplicateNicknameTest(@ModelAttribute DuplicateNicknameTestRequest request){
        if (memberService.duplicateNicknameTest(request.nickname)){
            return ResponseEntity.ok(false);
        }else{
            return ResponseEntity.ok(true);
        }
    }

    @GetMapping("/api/username/duplicate")
    public ResponseEntity<?> duplicateUsernameTest(@ModelAttribute DuplicateUsernameTestRequest request){
        if (memberService.duplicateUsernameTest(request.username)){
            return ResponseEntity.ok(false);
        }else{
            return ResponseEntity.ok(true);
        }
    }


    //== DTO ==//

    @Data
    static class EmailAuthenticationRequest{
        private String email;
    }

    @Data
    static class VerifyEmailRequest{
        private String code;
    }

    @Data
    static class DuplicateNicknameTestRequest{
        private String nickname;
    }

    @Data
    static class DuplicateUsernameTestRequest{
        private String username;
    }

}
