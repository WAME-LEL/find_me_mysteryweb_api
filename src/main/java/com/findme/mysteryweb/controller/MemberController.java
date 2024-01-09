package com.findme.mysteryweb.controller;


import com.findme.mysteryweb.domain.Member;
import com.findme.mysteryweb.service.MemberService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.executable.ValidateOnExecution;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    @GetMapping("/signup")
    public String signupPage(){

        return "signupForm";
    }

//    @PostMapping("/signup/new")
//    public String signupNew(@RequestParam("nickname") String nickname,
//                            @RequestParam("username") String username,
//                            @RequestParam("password") String password){
//        Member member = Member.createMember(nickname, username, password);
//
//        memberService.save(member);
//
//        return "redirect:/";
//    }
    @PostMapping("/signup/new")
    public String signupNew(@ModelAttribute @Valid CreateMemberRequest createMemberRequest, BindingResult bindingResult){
        Member member = Member.createMember(createMemberRequest.nickname, createMemberRequest.username, createMemberRequest.password);

        memberService.save(member);

        return "redirect:/";
    }

    @Data
    static class CreateMemberRequest{
        @NotEmpty(message = "nickname은 필수 입력 사항입니다")
        private String nickname;
        @NotEmpty(message = "username은 필수 입력 사항입니다")
        private String username;
        @NotEmpty(message = "password은 필수 입력 사항입니다")
        private String password;

        public CreateMemberRequest(String nickname, String username, String password) {
            this.nickname = nickname;
            this.username = username;
            this.password = password;
        }
    }


}
