package com.findme.mysteryweb.service;

import com.findme.mysteryweb.domain.Comment;
import com.findme.mysteryweb.domain.Member;
import com.findme.mysteryweb.domain.Post;
import com.findme.mysteryweb.repository.CommentRepository;
import com.findme.mysteryweb.repository.MemberRepository;
import com.findme.mysteryweb.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CommentService {
    private final CommentRepository commentRepository;
    private final MemberRepository memberRepository;
    private final PostRepository postRepository;

    public Long writeComment(Long postId, Long memberId, String comment_content){
        Post post = postRepository.findOne(postId);
        Member member = memberRepository.findOne(memberId);

        Comment comment = Comment.createComment(post, member, comment_content);

        return commentRepository.save(comment);
    }

    @Transactional(readOnly = true)
    public Comment findOne(Long commentId){
        return commentRepository.findOne(commentId);
    }

    @Transactional(readOnly = true)
    public List<Comment> findAll(){
        return commentRepository.findAll();
    }

    public List<Comment> findAllByPostId(Long postId){
        return commentRepository.findAllByPostId(postId);
    }

    public void delete(Long commentId){
        commentRepository.delete(commentId);
    }


}
