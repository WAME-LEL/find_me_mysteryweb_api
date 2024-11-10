package com.findme.mysteryweb.service;


import com.findme.mysteryweb.domain.CorrectAnswer;
import com.findme.mysteryweb.domain.CorrectAnswerHistory;
import com.findme.mysteryweb.domain.Member;
import com.findme.mysteryweb.domain.Post;
import com.findme.mysteryweb.repository.CorrectAnswerHistoryRepository;
import com.findme.mysteryweb.repository.MemberRepository;
import com.findme.mysteryweb.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CorrectAnswerHistoryService {
    private final CorrectAnswerHistoryRepository correctAnswerHistoryRepository;
    private final MemberRepository memberRepository;
    private final PostRepository postRepository;


    public void save(Long memberId, Long postId, String answer) {
        Member member = memberRepository.findOne(memberId);
        Post post = postRepository.findOne(postId);

        CorrectAnswerHistory correctAnswer = CorrectAnswerHistory.createCorrectAnswerHistory(member, post, answer);
        correctAnswerHistoryRepository.save(correctAnswer);
    }

    public CorrectAnswerHistory findOne(Long memberId, Long postId) {
        return correctAnswerHistoryRepository.findOne(memberId, postId);
    }

    public List<CorrectAnswerHistory> findAll() {
        return correctAnswerHistoryRepository.findAll();
    }


}
