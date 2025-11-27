package com.smile.mypark.service;

import com.smile.mypark.dto.response.CompetitionResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CompetitionService {
    Page<CompetitionResponseDTO> getCompetitions(Long userId, Pageable pageable);
}
