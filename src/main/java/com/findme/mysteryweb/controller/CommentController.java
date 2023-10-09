package com.findme.mysteryweb.controller;

import com.findme.mysteryweb.domain.Member;
import com.findme.mysteryweb.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping("comment/save")
    public String CommentSave(String comment_content){
        Member member = Member.createMember("park", "park12", "park34");

        commentService.writeComment(member.getId(), comment_content);

        return "redirect:/post";
    }


}
