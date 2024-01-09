package com.findme.mysteryweb.controller;

import com.findme.mysteryweb.domain.Member;
import com.findme.mysteryweb.domain.Post;
import com.findme.mysteryweb.service.CommentService;
import com.findme.mysteryweb.service.MemberService;
import com.findme.mysteryweb.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequiredArgsConstructor
@Slf4j
public class CommentController {

    private final CommentService commentService;
    private final PostService postService;
    private final MemberService memberService;

    @PostMapping("/comment/save")
    public String CommentSave(@RequestParam("commentTitle") String comment_title, @RequestParam("commentContent") String comment_content, @RequestParam("postId") Long postId){
        Member member = memberService.findOne(1L);
        log.info("postId:"+postId+" memberId:"+ member.getId()+" title:"+ comment_title+" content:"+comment_content);

        commentService.writeComment(postId, member.getId(), comment_content);
        return "redirect:/post/"+postId;
    }



}
