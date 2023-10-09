package com.findme.mysteryweb.service;

import com.findme.mysteryweb.domain.Comment;
import com.findme.mysteryweb.domain.Member;
import com.findme.mysteryweb.repository.CommentRepository;
import com.findme.mysteryweb.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class CommentService {
    private final CommentRepository commentRepository;
    private final MemberRepository memberRepository;

    public void writeComment(Long memberId, String comment_content){
        Member member = memberRepository.findOne(memberId);

        Comment comment = Comment.createComment(member, comment_content);

        commentRepository.save(comment);
    }

    public void delete(Long commentId){
        commentRepository.delete(commentId);
    }


}
