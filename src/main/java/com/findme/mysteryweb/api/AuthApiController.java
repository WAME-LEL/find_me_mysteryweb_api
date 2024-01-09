package com.findme.mysteryweb.api;


import com.findme.mysteryweb.domain.Member;
import com.findme.mysteryweb.service.EmailService;
import com.findme.mysteryweb.service.MemberService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.security.SecureRandom;
import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class AuthApiController {
    private final EmailService emailService;
    private final MemberService memberService;

    @PostMapping("/api/email/authentication")
    public ResponseEntity<?> emailAuthentication(@RequestBody EmailAuthenticationRequest request){
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < 6; i++) {
            sb.append(random.nextInt(10)); // 0에서 9까지의 숫자를 생성
        }

        String AuthenticationCode = sb.toString();

        String content = "탐정의 밤 계정 이메일 인증 입니다.\n인증코드: " + AuthenticationCode;
        emailService.sendEMailAuthentication(request.email, "탐정의 밤 계정 이메일 인증", content);

        memberService.saveAuthenticationCode(request.memberId, request.email, AuthenticationCode);

        return ResponseEntity.ok("send email completed");
    }

    @PostMapping("/api/email/verify")
    public ResponseEntity<?> verifyEmail(@RequestBody VerifyEmailRequest request) {
        Member member = memberService.findOne(request.memberId);
        if (member != null &&
                member.getAuthenticationCode().equals(request.getCode()) &&
                member.getAuthenticationCodeExpireTime().isAfter(LocalDateTime.now())) {
            memberService.activateMember(request.memberId);
            return ResponseEntity.ok("Email successfully verified.");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid or expired verification code.");
        }
    }



    //== DTO ==//

    @Data
    static class EmailAuthenticationRequest{
        private Long memberId;
        private String email;
    }

    @Data
    static class VerifyEmailRequest{
        private Long memberId;
        private String code;
    }


}
