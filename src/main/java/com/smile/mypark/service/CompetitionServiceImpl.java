package com.smile.mypark.service;

import com.smile.mypark.dto.response.CompetitionResponseDTO;
import com.smile.mypark.repository.CompetitionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CompetitionServiceImpl implements CompetitionService {

    private final CompetitionRepository competitionRepository;

    @Override
    @Transactional(readOnly = true)
    public CompetitionResponseDTO getCompetition(Long userId) {
        return competitionRepository.findCompetitionData(userId);
    }
}