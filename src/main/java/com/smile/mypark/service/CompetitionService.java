package com.smile.mypark.service;

import com.smile.mypark.dto.response.CompetitionResponseDTO;

public interface CompetitionService {
    CompetitionResponseDTO getCompetition(Long userId);
}
