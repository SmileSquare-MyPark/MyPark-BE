package com.smile.mypark.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
@Schema(description = "라운딩 결과 응답 DTO")
public class RoundingResponseDTO {

	@Schema(description = "총 라운드 수", example = "5")
	private Integer totalRoundCount;

	@Schema(description = "상단 요약 정보")
	private Summary summary;

	@Schema(description = "그래프 데이터 목록")
	private List<RoundingInfo> roundings;

	@Getter
	@Builder
	@Schema(description = "상단 요약 정보")
	public static class Summary {

		@Schema(description = "타수 정보")
		private ScoreInfo score;

		@Schema(description = "티샷 비거리 정보")
		private DistanceInfo distance;

		@Schema(description = "그린 안착률 정보")
		private GreenInRegulationInfo greenInRegulation;

		@Schema(description = "평균 퍼팅 정보")
		private PuttingInfo putting;
	}

	@Getter
	@Builder
	@Schema(description = "타수 정보")
	public static class ScoreInfo {

		@Schema(description = "평균 타수", example = "53")
		private Integer average;

		@Schema(description = "최근 평균 타수", example = "51")
		private Integer recentAverage;
	}

	@Getter
	@Builder
	@Schema(description = "티샷 비거리 정보")
	public static class DistanceInfo {

		@Schema(description = "평균 비거리", example = "123")
		private Integer average;
	}

	@Getter
	@Builder
	@Schema(description = "그린 안착률 정보")
	public static class GreenInRegulationInfo {

		@Schema(description = "평균 안착률", example = "48.5")
		private Double averageRate;
	}

	@Getter
	@Builder
	@Schema(description = "평균 퍼팅 정보")
	public static class PuttingInfo {

		@Schema(description = "평균 퍼팅", example = "48.5")
		private Double averageRate;
	}

	@Getter
	@Builder
	@Schema(description = "라운딩 정보")
	public static class RoundingInfo {

		@Schema(description = "라운딩 날짜", example = "2025-01-01T00:00:00")
		private LocalDateTime roundingDate;

		@Schema(description = "타수", example = "72")
		private Integer score;

		@Schema(description = "평균 티샷 비거리", example = "230.5")
		private Double averageDistance;

		@Schema(description = "그린 안착률", example = "55.5")
		private Double greenInRegulation;

		@Schema(description = "평균 퍼팅수", example = "1.8")
		private Double puttingRate;
	}
}
