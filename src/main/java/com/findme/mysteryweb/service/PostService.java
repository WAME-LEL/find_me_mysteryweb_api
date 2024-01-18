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
    public Long posting(Long memberId, String post_title, String post_content, String post_type, String answer, String explanation, AnswerType answerType){
        //엔티티 조회
        Member member = memberRepository.findOne(memberId);

        Post post = Post.createPost(post_title, post_content, post_type, answer, explanation, member);

        return postRepository.save(post);

    }

    public Post findOne(Long postId){
        return postRepository.findOne(postId);
    }

    public List<Post> findAll(){
        return postRepository.findAll();
    }

    public List<Post> findAllByMemberId(Long memberId){
        return postRepository.findAllByMemberId(memberId);
    }
    public List<Post> findAllByType(String type){
        return postRepository.findAllByType(type);
    }

    public List<Post> findAllByTitle(String title){
        return postRepository.findAllByTitle(title);
    }

    public List<Post> findAllByAuthor(String author){
        return postRepository.findAllByAuthor(author);
    }

    public List<Post> findAllByTypeAndTitle(String type, String title){
        return postRepository.findAllByTypeAndTitle(type, title);
    }

    public List<Post> findAllByTypeAndTitleOrContent(String type, String title, String content){
        return postRepository.findAllByTypeAndTitleOrContent(type, title, content);
    }

    public List<Post> findAllByTypeAndAuthor(String type, String author){
        return postRepository.findAllByTypeAndAuthor(type, author);
    }

    public List<Post> findAllOrderByViewCount() {
        return postRepository.findAllOrderByViewCount();
    }

    public List<Post> findAllOrderByRecommendationCount() {
        return postRepository.findAllOrderByRecommendationCount();
    }

    public List<Post> findCountOrderByView(int count){
        return postRepository.findCountOrderByRecommendationCount(count);
    }

    public List<Post> findCountOrderByDatetime(String type, int count){
        return postRepository.findCountOrderByDatetime(type, count);
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

    @Transactional
    public void increaseViewCount(Long postId){
        Post post = postRepository.findOne(postId);
        post.setViewCount(post.getViewCount()+1);
    }

    @Transactional
    public void increaseRecommendCount(Long bookId){
        Post post = postRepository.findOne(bookId);
        post.setRecommendationCount(post.getRecommendationCount()+1);
    }

    @Transactional
    public void decreaseRecommendCount(Long bookId){
        Post post = postRepository.findOne(bookId);
        post.setRecommendationCount(post.getRecommendationCount()-1);
    }




}
