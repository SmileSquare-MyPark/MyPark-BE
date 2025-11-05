package com.smile.mypark.service;

import com.smile.mypark.dto.response.TrainingResponseDTO;

public interface TrainingService {
	TrainingResponseDTO getTrainingResults(Long userId);
}
