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

        RoundingResponseDTO.Summary summary = calculateSummary(roundings);

        return RoundingResponseDTO.builder()
                .totalRoundCount(totalRoundCount)
                .summary(summary)
                .roundings(roundings)
                .build();
    }

    /**
     * 라운딩 데이터를 바탕으로 요약 정보 계산
     */
    private RoundingResponseDTO.Summary calculateSummary(List<RoundingResponseDTO.RoundingInfo> roundings) {
        if (roundings == null || roundings.isEmpty()) {
            return RoundingResponseDTO.Summary.builder()
                    .score(RoundingResponseDTO.ScoreInfo.builder()
                            .average(0)
                            .recentAverage(0)
                            .build())
                    .distance(RoundingResponseDTO.DistanceInfo.builder()
                            .average(0)
                            .build())
                    .greenInRegulation(RoundingResponseDTO.GreenInRegulationInfo.builder()
                            .averageRate(0.0)
                            .build())
                    .putting(RoundingResponseDTO.PuttingInfo.builder()
                            .averageRate(0.0)
                            .build())
                    .build();
        }

        int avgScore = (int) roundings.stream()
                .filter(r -> r.getScore() != null)
                .mapToInt(RoundingResponseDTO.RoundingInfo::getScore)
                .average()
                .orElse(0);

        int recentAvgScore = (int) roundings.stream()
                .limit(3)
                .filter(r -> r.getScore() != null)
                .mapToInt(RoundingResponseDTO.RoundingInfo::getScore)
                .average()
                .orElse(0);

        int avgDistance = (int) roundings.stream()
                .filter(r -> r.getAverageDistance() != null)
                .mapToDouble(RoundingResponseDTO.RoundingInfo::getAverageDistance)
                .average()
                .orElse(0);

        double avgGreenInRegulation = roundings.stream()
                .filter(r -> r.getGreenInRegulation() != null)
                .mapToDouble(RoundingResponseDTO.RoundingInfo::getGreenInRegulation)
                .average()
                .orElse(0);

        double avgPuttingRate = roundings.stream()
                .filter(r -> r.getPuttingRate() != null)
                .mapToDouble(RoundingResponseDTO.RoundingInfo::getPuttingRate)
                .average()
                .orElse(0);

        return RoundingResponseDTO.Summary.builder()
                .score(RoundingResponseDTO.ScoreInfo.builder()
                        .average(avgScore)
                        .recentAverage(recentAvgScore)
                        .build())
                .distance(RoundingResponseDTO.DistanceInfo.builder()
                        .average(avgDistance)
                        .build())
                .greenInRegulation(RoundingResponseDTO.GreenInRegulationInfo.builder()
                        .averageRate(Math.round(avgGreenInRegulation * 10.0) / 10.0)
                        .build())
                .putting(RoundingResponseDTO.PuttingInfo.builder()
                        .averageRate(Math.round(avgPuttingRate * 10.0) / 10.0)
                        .build())
                .build();
    }
}