package com.findme.mysteryweb.controller;


import com.findme.mysteryweb.domain.Member;
import com.findme.mysteryweb.domain.Post;
import com.findme.mysteryweb.repository.MemberRepository;
import com.findme.mysteryweb.service.MemberService;
import com.findme.mysteryweb.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;
    private final MemberService memberService;



    @PostMapping("/post/save")
    public String post(@RequestParam("postTitle") String postTitle, @RequestParam("postContent") String postContent){
        //더미 멤버
        Member member = Member.createMember("park", "park12", "park34");

        memberService.save(member);

        postService.posting(member.getId(), postTitle, postContent);

        return "redirect:/post";
    }

    @GetMapping("/post")
    public String postList(Model model){
        List<Post> postList = postService.findAll();

        for (Post post: postList) {
            System.out.println(post.getPost_title());
        }


        model.addAttribute("postList", postList);

        return "board";
    }

    @PostMapping("/post/{postId}/delete")
    public String postDelete(@PathVariable("postId") Long postId){
        postService.delete(postId);

        return "redirect:/post";
    }

}
