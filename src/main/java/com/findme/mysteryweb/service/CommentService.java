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

    public Long writeComment(Long postId, Long parentId, Long memberId, String comment_content){
        Post post = postRepository.findOne(postId);
        Member member = memberRepository.findOne(memberId);
        Comment parent = null;
        Comment comment;
        if (parentId == null){
            comment = Comment.createComment(post, parent,  member, comment_content);
        }else{
            parent = commentRepository.findOne(parentId);
            comment = Comment.createComment(post, parent,  member, comment_content);
        }

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

    public List<Comment> findAllByMemberId(Long memberId){
        return commentRepository.findAllByMemberId(memberId);
    }

    public List<Comment> findAllByPostId(Long postId){
        return commentRepository.findAllByPostId(postId);
    }

    public void delete(Long commentId){
        commentRepository.delete(commentId);
    }

    public List<Comment> findCommentsWithAllRepliesByPostId(Long postId) {
        List<Comment> topLevelComments = commentRepository.findAllTopLevelCommentsByPostId(postId);
        topLevelComments.forEach(this::loadRepliesRecursively);
        return topLevelComments;
    }

    private void loadRepliesRecursively(Comment comment) {
        List<Comment> replies = commentRepository.findRepliesByCommentId(comment.getId());
        comment.setReplies(replies);
        replies.forEach(this::loadRepliesRecursively); // 재귀적 호출
    }


}
