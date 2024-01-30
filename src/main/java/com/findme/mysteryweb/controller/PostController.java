package com.findme.mysteryweb.controller;


import com.findme.mysteryweb.domain.Comment;
import com.findme.mysteryweb.domain.Post;
import com.findme.mysteryweb.service.CommentService;
import com.findme.mysteryweb.service.MemberService;
import com.findme.mysteryweb.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;
    private final MemberService memberService;
    private final CommentService commentService;


    @GetMapping("/post/new")
    public String postingForm(){

        return "postForm";
    }

//    @PostMapping("/post/save")
//    public String post(
//            @RequestParam("postTitle") String postTitle,
//            @RequestParam("postContent") String postContent,
//            @RequestParam("Post_type") String post_type,
//            @RequestParam("answer") String answer,
//            @RequestParam("explanation") String explanation,
//            @RequestParam("nickname") String nickname,
//            @RequestParam("multiple_answer") AnswerType multiple_answer,
//            @RequestParam("short_answer") AnswerType short_answer
//
//    ){
//        //더미 멤버
//        Member member = Member.createMember("park", "park12", "park34");
//
//        memberService.save(member);
//
//        postService.posting(member.getId(), postTitle, postContent, post_type, answer, explanation, short_answer);
//
//        return "redirect:/";
//    }

    @GetMapping("/post/{postId}")
    public String postList(@PathVariable Long postId, Model model){
        List<Post> postList = postService.findAll();
        Post post = postService.findOne(postId);
        List<Comment> commentList = commentService.findAllByPostId(postId);


        model.addAttribute("post", post);

        model.addAttribute("commentList", commentList);

        return "board";
    }

    @PostMapping("/post/{postId}/delete")
    public String postDelete(@PathVariable("postId") Long postId){
        postService.delete(postId);

        return "redirect:/post";
    }

//    @GetMapping("/post/{postId}/update")
//    public String postUpdateForm(@PathVariable("postId") Long postId, Model model){
//        Post findPost = postService.findOne(postId);
//
//        model.addAttribute("post", findPost);
//        return "updateForm";
//    }

//    @PostMapping("/post/{postId}/update")
//    public String postUpdate(@PathVariable("postId") Long postId,
//                             @RequestParam("updatePostTitle") String updatePostTitle,
//                             @RequestParam("updatePostContent") String updatePostContent){
//
//        postService.update(postId, updatePostTitle, updatePostContent);
//
//
//        return "redirect:/post";
//    }

}
