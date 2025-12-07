package com.smile.mypark.service;

import com.smile.mypark.dto.response.RoundingResponseDTO;
import com.smile.mypark.dto.response.ScoreStatisticsResponseDTO;
import com.smile.mypark.dto.response.TrainingResponseDTO;

public interface ResultService {
	TrainingResponseDTO getTrainingResults(Long userId, int page, int size);

    RoundingResponseDTO getRoundingResults(Long userId);

	ScoreStatisticsResponseDTO getScoreStatistics(Long userId);
}
