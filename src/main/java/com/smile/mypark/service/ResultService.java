package com.smile.mypark.service;

import com.smile.mypark.dto.response.RoundingResponseDTO;
import com.smile.mypark.dto.response.TrainingResponseDTO;

public interface ResultService {
	TrainingResponseDTO getTrainingResults(Long userId);

    RoundingResponseDTO getRoundingResults(Long userId);
}
