package com.smile.mypark.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@Schema(description = "라운딩 결과 응답 DTO")
public class RoundingResponseDTO {

	@Schema(description = "총 라운드 수", example = "15")
	private Integer totalRoundCount;

	@Schema(description = "라운딩 정보 목록")
	private List<RoundingInfo> roundings;

	@Getter
	@Builder
	@Schema(description = "라운딩 정보")
	public static class RoundingInfo {

		@Schema(description = "타수", example = "72")
		private Integer swingCount;

		@Schema(description = "평균 티샷 비거리", example = "230.5")
		private Double averageDistance;

		@Schema(description = "그린 안착률", example = "55.5")
		private Double greenInRegulation;

		@Schema(description = "평균 퍼팅수", example = "1.8")
		private Double puttingRate;

		@Schema(description = "방문 필드", example = "CC001")
		private String ccIdx;

		@Schema(description = "티샷 최장타", example = "280.0")
		private Double maxDistance;

		@Schema(description = "최대 퍼팅 거리", example = "15.5")
		private Double maxPuttingDistance;

		@Schema(description = "점수 통계", example = "2")
		private Integer score;
	}
}
