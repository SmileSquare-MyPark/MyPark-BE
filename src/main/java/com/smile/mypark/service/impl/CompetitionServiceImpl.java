package com.smile.mypark.service.impl;

import com.smile.mypark.dto.response.CompetitionResponseDTO;
import com.smile.mypark.repository.CompetitionRepository;
import com.smile.mypark.service.CompetitionService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CompetitionServiceImpl implements CompetitionService {

    private final CompetitionRepository competitionRepository;

    @Override
    @Transactional(readOnly = true)
    public Page<CompetitionResponseDTO> getCompetitions(Long userId, Pageable pageable) {
        return competitionRepository.findCompetitionData(userId, pageable);
    }
}