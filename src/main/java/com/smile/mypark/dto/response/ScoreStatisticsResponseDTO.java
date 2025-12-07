package com.smile.mypark.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Schema(description = "스코어 통계 응답 DTO")
public class ScoreStatisticsResponseDTO {

	@Schema(description = "홀인원 개수", example = "1")
	private Integer holeInOne;

	@Schema(description = "알바트로스 개수", example = "2")
	private Integer albatross;

	@Schema(description = "이글 개수", example = "3")
	private Integer eagle;

	@Schema(description = "버디 개수", example = "999")
	private Integer birdie;

	@Schema(description = "파 개수", example = "9999")
	private Integer par;

	@Schema(description = "보기 개수", example = "9999")
	private Integer bogey;
}

