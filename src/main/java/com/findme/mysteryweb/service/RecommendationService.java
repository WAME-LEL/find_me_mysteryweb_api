package com.findme.mysteryweb.service;


import com.findme.mysteryweb.domain.Recommendation;
import com.findme.mysteryweb.repository.RecommendationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RecommendationService {
    private final RecommendationRepository recommendationRepository;

    @Transactional
    public void save(Recommendation recommendation){
        recommendationRepository.save(recommendation);
    }

    public Recommendation findOne(Long recommendationId){
        return recommendationRepository.findOne(recommendationId);
    }

    public Recommendation findOneByMemberIdAndOtherId(Long memberId, Long postId, Long bookId){
        return recommendationRepository.findOneByMemberIdAndOtherId(memberId, postId, bookId);

    }

    public List<Recommendation> findAll(){
        return recommendationRepository.findAll();
    }

    public List<Recommendation> findAllByMemberId(Long memberId){
        return recommendationRepository.findAllByMemberId(memberId);
    }

    @Transactional
    public void delete(Long memberId, Long postId, Long bookId){
        Recommendation recommendation = recommendationRepository.findOneByMemberIdAndOtherId(memberId, postId, bookId);
        recommendationRepository.delete(recommendation.getId());
    }


}
