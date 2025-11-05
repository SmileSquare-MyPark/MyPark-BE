package com.smile.mypark.service;

import com.smile.mypark.dto.response.TrainingResponseDTO;
import com.smile.mypark.repository.TrainingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TrainingServiceImpl implements TrainingService {

	private final TrainingRepository trainingRepository;

	@Override
	@Transactional(readOnly = true)
	public TrainingResponseDTO getTrainingResults(Long userId) {
		List<TrainingResponseDTO.TrainingShotInfo> trainings = trainingRepository.findTrainingShotsByUserId(userId);
		return TrainingResponseDTO.builder()
				.trainings(trainings)
				.build();
	}
}