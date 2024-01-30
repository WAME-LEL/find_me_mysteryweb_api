package com.findme.mysteryweb.service;

import com.findme.mysteryweb.domain.Comment;
import com.findme.mysteryweb.domain.Member;
import com.findme.mysteryweb.domain.Notification;
import com.findme.mysteryweb.domain.Post;
import com.findme.mysteryweb.repository.CommentRepository;
import com.findme.mysteryweb.repository.MemberRepository;
import com.findme.mysteryweb.repository.NotificationRepository;
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
    private final NotificationRepository notificationRepository;

    public Comment writeComment(Long postId, Long parentId, Long senderId, Long receiverId, String comment_content){
        Post post = postRepository.findOne(postId);
        Member member = memberRepository.findOne(senderId);
        Comment parent = null;
        Comment comment;
        Notification notification;
        if (parentId == null){
            comment = Comment.createComment(post, parent,  member, comment_content);
//            notification = Notification.createNotification(senderId, receiverId, post.getTitle() + " 글에 댓글이 달렸습니다");
        }else{
            parent = commentRepository.findOne(parentId);
            comment = Comment.createComment(post, parent,  member, comment_content);
//            notification = Notification.createNotification(senderId, parent.getMember().getId(), post.getTitle() + " 댓글에 대댓글이 달렸습니다");
        }
//        notificationRepository.save(notification);

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
