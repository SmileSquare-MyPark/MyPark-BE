package com.smile.mypark.service.impl;

import com.smile.mypark.dto.response.RoundingResponseDTO;
import com.smile.mypark.dto.response.TrainingResponseDTO;
import com.smile.mypark.repository.RoundingRepository;
import com.smile.mypark.repository.TrainingRepository;
import com.smile.mypark.service.ResultService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ResultServiceImpl implements ResultService {

	private final TrainingRepository trainingRepository;
    private final RoundingRepository roundingRepository;

	@Override
	@Transactional(readOnly = true)
	public TrainingResponseDTO getTrainingResults(Long userId) {
		List<TrainingResponseDTO.TrainingShotInfo> trainings = trainingRepository.findTrainingShotsByUserId(userId);
		return TrainingResponseDTO.builder()
				.trainings(trainings)
				.build();
	}

    @Override
    @Transactional(readOnly = true)
    public RoundingResponseDTO getRoundingResults(Long userId) {

        Integer totalRoundCount = roundingRepository.countRoundingByUserId(userId);
        List<RoundingResponseDTO.RoundingInfo> roundings = roundingRepository.findRoundingInfoByUserId(userId);

        return RoundingResponseDTO.builder()
                .totalRoundCount(totalRoundCount)
                .roundings(roundings)
                .build();
    }
}