package com.findme.mysteryweb.service;

import com.findme.mysteryweb.domain.Member;
import com.findme.mysteryweb.domain.Post;
import com.findme.mysteryweb.domain.AnswerType;
import com.findme.mysteryweb.repository.MemberRepository;
import com.findme.mysteryweb.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostService {

    private final PostRepository postRepository;
    private final MemberRepository memberRepository;


    @Transactional
    public Long posting(Long memberId, String post_title, String post_content, String post_type, String answer, AnswerType answerType){
        //엔티티 조회
        Member member = memberRepository.findOne(memberId);

        Post post = Post.createPost(post_title, post_content, post_type, answer, answerType, member);

        return postRepository.save(post);

    }

    public Post findOne(Long postId){
        return postRepository.findOne(postId);
    }

    public List<Post> findAll(){
        return postRepository.findAll();
    }

    public List<Post> findAllByType(String type){
        return postRepository.findAllByType(type);
    }

    public List<Post> findAllByTitle(String title){
        return postRepository.findAllByTitle(title);
    }

    public List<Post> findAllByTypeAndTitle(String type, String title){
        return postRepository.findAllByTypeAndTitle(type, title);
    }


    @Transactional
    public void delete(Long postId){
        postRepository.delete(postId);
    }

    @Transactional
    public Post update(Long postId, String update_post_title, String update_post_content){
        Post findPost = postRepository.findOne(postId);
        findPost.setTitle(update_post_title);
        findPost.setContent(update_post_content);

        return findPost;
    }




}
