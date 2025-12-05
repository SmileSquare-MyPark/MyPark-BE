package com.smile.mypark.service.impl;

import com.smile.mypark.dto.response.RoundingResponseDTO;
import com.smile.mypark.dto.response.TrainingResponseDTO;
import com.smile.mypark.repository.RoundingRepository;
import com.smile.mypark.repository.TrainingRepository;
import com.smile.mypark.service.ResultService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ResultServiceImpl implements ResultService {

	private final TrainingRepository trainingRepository;
    private final RoundingRepository roundingRepository;

	@Override
	@Transactional(readOnly = true)
	public TrainingResponseDTO getTrainingResults(Long userId, int page, int size) {
		List<TrainingRepository.TrainingShotInfoWithDate> trainingsWithDate =
				trainingRepository.findTrainingShotsWithDateByUserId(userId, page, size);

		Map<LocalDate, List<TrainingResponseDTO.TrainingShotInfo>> groupedByDate = trainingsWithDate.stream()
				.collect(Collectors.groupingBy(
						TrainingRepository.TrainingShotInfoWithDate::date,
						Collectors.mapping(
								TrainingRepository.TrainingShotInfoWithDate::info,
								Collectors.toList()
						)
				));

		List<TrainingResponseDTO.DateTrainingGroup> dateTrainingGroups = groupedByDate.entrySet().stream()
				.map(entry -> TrainingResponseDTO.DateTrainingGroup.builder()
						.trainingDate(entry.getKey())
						.trainingList(entry.getValue())
						.build())
				.sorted((a, b) -> b.getTrainingDate().compareTo(a.getTrainingDate())) // 최신 날짜 순으로 정렬
				.toList();

		return TrainingResponseDTO.builder()
				.trainings(dateTrainingGroups)
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