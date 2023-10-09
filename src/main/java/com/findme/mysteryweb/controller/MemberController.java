package com.findme.mysteryweb.controller;


import com.findme.mysteryweb.domain.Member;
import com.findme.mysteryweb.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    @GetMapping("/signup")
    public String signupPage(){

        return "signupForm";
    }

    @PostMapping("/signup/new")
    public String signupNew(@RequestParam("nickname") String nickname,
                            @RequestParam("username") String username,
                            @RequestParam("password") String password){
        Member member = Member.createMember(nickname, username, password);

        memberService.save(member);

        return "redirect:/";

    }


}
