package com.findme.mysteryweb.controller;


import com.findme.mysteryweb.domain.Post;
import com.findme.mysteryweb.repository.MemberRepository;
import com.findme.mysteryweb.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@Slf4j
@RequiredArgsConstructor
public class HomeController {

    private final MemberRepository memberRepository;
    private final PostRepository postRepository;


    @RequestMapping
    public String Home(Model model){
        log.info("home page");
        List<Post> postList = postRepository.findAll();
        model.addAttribute("postList", postList);

        return "home";
    }


}
