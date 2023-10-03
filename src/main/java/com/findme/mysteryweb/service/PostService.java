package com.findme.mysteryweb.service;

import com.findme.mysteryweb.domain.Member;
import com.findme.mysteryweb.domain.Post;
import com.findme.mysteryweb.repository.MemberRepository;
import com.findme.mysteryweb.repository.PostRepository;
import jakarta.persistence.criteria.Order;
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
    public void posting(Long memberId, String post_title, String post_content){
        //엔티티 조회
        Member member = memberRepository.findOne(memberId);

        Post post = Post.createPost(post_title, post_content, member);

        postRepository.save(post);
    }

    public Post findOne(Long postId){
        return postRepository.findOne(postId);
    }

    public List<Post> findAll(){
        return postRepository.findAll();
    }


    @Transactional
    public void delete(Long postId){
        postRepository.delete(postId);
    }

    @Transactional
    public Post update(Long postId, String update_post_title, String update_post_content){
        Post findPost = postRepository.findOne(postId);
        findPost.setPost_title(update_post_title);
        findPost.setPost_content(update_post_content);

        return findPost;
    }




}
