package com.smile.mypark.service.impl;

import com.smile.mypark.dto.response.RoundingResponseDTO;
import com.smile.mypark.dto.response.ScoreStatisticsResponseDTO;
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

	private static final int RECENT_ROUNDS_COUNT = 3;
	private static final double DECIMAL_SCALE = 10.0;
	private static final int DEFAULT_AVERAGE_VALUE = 0;
	private static final double DEFAULT_RATE_VALUE = 0.0;

	private final TrainingRepository trainingRepository;
	private final RoundingRepository roundingRepository;

	@Override
	@Transactional(readOnly = true)
	public TrainingResponseDTO getTrainingResults(Long userId, int page, int size) {
		List<TrainingRepository.TrainingShotInfoWithDate> trainingsWithDate =
				trainingRepository.findTrainingShotsWithDateByUserId(userId, page, size);

		Map<LocalDate, List<TrainingResponseDTO.TrainingShotInfo>> trainingsByDate = trainingsWithDate.stream()
				.collect(Collectors.groupingBy(
						TrainingRepository.TrainingShotInfoWithDate::date,
						Collectors.mapping(
								TrainingRepository.TrainingShotInfoWithDate::info,
								Collectors.toList()
						)
				));

		List<TrainingResponseDTO.DateTrainingGroup> dateTrainingGroups = trainingsByDate.entrySet().stream()
				.map(entry -> TrainingResponseDTO.DateTrainingGroup.builder()
						.trainingDate(entry.getKey())
						.trainingList(entry.getValue())
						.build())
				.sorted((a, b) -> b.getTrainingDate().compareTo(a.getTrainingDate()))
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

	@Override
	@Transactional(readOnly = true)
	public ScoreStatisticsResponseDTO getScoreStatistics(Long userId) {
		return roundingRepository.findScoreStatistics(userId);
	}

	/**
	 * 라운딩 데이터를 바탕으로 요약 정보를 계산합니다.
	 *
	 * @param roundings 라운딩 정보 리스트
	 * @return 계산된 요약 정보
	 */
	private RoundingResponseDTO.Summary calculateSummary(List<RoundingResponseDTO.RoundingInfo> roundings) {
		if (roundings == null || roundings.isEmpty()) {
			return createEmptySummary();
		}

		int averageScore = calculateAverageScore(roundings);
		int recentAverageScore = calculateRecentAverageScore(roundings);
		int averageDistance = calculateAverageDistance(roundings);
		double averageGreenInRegulation = calculateAverageGreenInRegulation(roundings);
		double averagePuttingRate = calculateAveragePuttingRate(roundings);

		return RoundingResponseDTO.Summary.builder()
				.score(RoundingResponseDTO.ScoreInfo.builder()
						.average(averageScore)
						.recentAverage(recentAverageScore)
						.build())
				.distance(RoundingResponseDTO.DistanceInfo.builder()
						.average(averageDistance)
						.build())
				.greenInRegulation(RoundingResponseDTO.GreenInRegulationInfo.builder()
						.averageRate(roundToOneDecimal(averageGreenInRegulation))
						.build())
				.putting(RoundingResponseDTO.PuttingInfo.builder()
						.averageRate(roundToOneDecimal(averagePuttingRate))
						.build())
				.build();
	}

	/**
	 * 데이터가 없을 때 반환할 기본 요약 정보를 생성합니다.
	 *
	 * @return 기본값으로 채워진 요약 정보
	 */
	private RoundingResponseDTO.Summary createEmptySummary() {
		return RoundingResponseDTO.Summary.builder()
				.score(RoundingResponseDTO.ScoreInfo.builder()
						.average(DEFAULT_AVERAGE_VALUE)
						.recentAverage(DEFAULT_AVERAGE_VALUE)
						.build())
				.distance(RoundingResponseDTO.DistanceInfo.builder()
						.average(DEFAULT_AVERAGE_VALUE)
						.build())
				.greenInRegulation(RoundingResponseDTO.GreenInRegulationInfo.builder()
						.averageRate(DEFAULT_RATE_VALUE)
						.build())
				.putting(RoundingResponseDTO.PuttingInfo.builder()
						.averageRate(DEFAULT_RATE_VALUE)
						.build())
				.build();
	}

	/**
	 * 전체 라운드의 평균 타수를 계산합니다.
	 *
	 * @param roundings 라운딩 정보 리스트
	 * @return 평균 타수
	 */
	private int calculateAverageScore(List<RoundingResponseDTO.RoundingInfo> roundings) {
		return roundings.stream()
				.filter(rounding -> rounding.getScore() != null)
				.mapToInt(RoundingResponseDTO.RoundingInfo::getScore)
				.average()
				.stream()
				.mapToInt(avg -> (int) avg)
				.findFirst()
				.orElse(DEFAULT_AVERAGE_VALUE);
	}

	/**
	 * 최근 라운드의 평균 타수를 계산합니다.
	 *
	 * @param roundings 라운딩 정보 리스트
	 * @return 최근 평균 타수
	 */
	private int calculateRecentAverageScore(List<RoundingResponseDTO.RoundingInfo> roundings) {
		return roundings.stream()
				.limit(RECENT_ROUNDS_COUNT)
				.filter(rounding -> rounding.getScore() != null)
				.mapToInt(RoundingResponseDTO.RoundingInfo::getScore)
				.average()
				.stream()
				.mapToInt(avg -> (int) avg)
				.findFirst()
				.orElse(DEFAULT_AVERAGE_VALUE);
	}

	/**
	 * 평균 티샷 비거리를 계산합니다.
	 *
	 * @param roundings 라운딩 정보 리스트
	 * @return 평균 비거리
	 */
	private int calculateAverageDistance(List<RoundingResponseDTO.RoundingInfo> roundings) {
		return roundings.stream()
				.filter(rounding -> rounding.getAverageDistance() != null)
				.mapToDouble(RoundingResponseDTO.RoundingInfo::getAverageDistance)
				.average()
				.stream()
				.mapToInt(avg -> (int) avg)
				.findFirst()
				.orElse(DEFAULT_AVERAGE_VALUE);
	}

	/**
	 * 평균 그린 안착률을 계산합니다.
	 *
	 * @param roundings 라운딩 정보 리스트
	 * @return 평균 그린 안착률
	 */
	private double calculateAverageGreenInRegulation(List<RoundingResponseDTO.RoundingInfo> roundings) {
		return roundings.stream()
				.filter(rounding -> rounding.getGreenInRegulation() != null)
				.mapToDouble(RoundingResponseDTO.RoundingInfo::getGreenInRegulation)
				.average()
				.orElse(DEFAULT_RATE_VALUE);
	}

	/**
	 * 평균 퍼팅 수를 계산합니다.
	 *
	 * @param roundings 라운딩 정보 리스트
	 * @return 평균 퍼팅 수
	 */
	private double calculateAveragePuttingRate(List<RoundingResponseDTO.RoundingInfo> roundings) {
		return roundings.stream()
				.filter(rounding -> rounding.getPuttingRate() != null)
				.mapToDouble(RoundingResponseDTO.RoundingInfo::getPuttingRate)
				.average()
				.orElse(DEFAULT_RATE_VALUE);
	}

	/**
	 * 값을 소수점 첫째 자리까지 반올림합니다.
	 *
	 * @param value 반올림할 값
	 * @return 소수점 첫째 자리까지 반올림된 값
	 */
	private double roundToOneDecimal(double value) {
		return Math.round(value * DECIMAL_SCALE) / DECIMAL_SCALE;
	}
}