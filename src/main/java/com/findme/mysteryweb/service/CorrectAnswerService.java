package com.findme.mysteryweb.service;

import com.findme.mysteryweb.domain.CorrectAnswer;
import com.findme.mysteryweb.domain.Member;
import com.findme.mysteryweb.domain.Post;
import com.findme.mysteryweb.repository.CorrectAnswerRepository;
import com.findme.mysteryweb.repository.MemberRepository;
import com.findme.mysteryweb.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CorrectAnswerService {
    private final CorrectAnswerRepository correctAnswerRepository;
    private final MemberRepository memberRepository;
    private final PostRepository postRepository;


    @Transactional
    public void save(Long memberId, Long postId){
        Member member = memberRepository.findOne(memberId);
        Post post = postRepository.findOne(postId);

        CorrectAnswer correctAnswer = CorrectAnswer.createCorrectAnswer(member, post);
        correctAnswerRepository.save(correctAnswer);
    }

    public CorrectAnswer findOne(Long correctAnswerId){
        return correctAnswerRepository.findOne(correctAnswerId);
    }

    public CorrectAnswer findOneByPostAndMember(Long postId, Long memberId){
        return correctAnswerRepository.findOneByPostAndMember(postId, memberId);
    }

    public List<CorrectAnswer> findAllOrderByDatetime(Long memberId){
        return correctAnswerRepository.findAllOrderByDatetime(memberId);
    }

    public List<CorrectAnswer> findAllByMember(Long memberId){
        return correctAnswerRepository.findAllByMember(memberId);
    }



}
