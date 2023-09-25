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

    public List<Post> findAll(){
        return postRepository.findAll();
    }


    @Transactional
    public void delete(Long postId){
        postRepository.delete(postId);
    }





}
