package com.smile.mypark.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
@Schema(description = "연습 결과 응답 DTO")
public class TrainingResponseDTO {

	@Schema(description = "연습 샷 목록")
	private List<TrainingShotInfo> trainings;

	@Getter
	@Builder
	@Schema(description = "연습 샷 정보")
	public static class TrainingShotInfo {

		@Schema(description = "방향각", example = "2.5")
		private Double directionAngle;

		@Schema(description = "발사각", example = "15.3")
		private Double launchAngle;

		@Schema(description = "볼스피드", example = "150.5")
		private Double ballSpeed;

		@Schema(description = "클럽 스피드", example = "180.2")
		private Double clubSpeed;

		@Schema(description = "비거리", example = "250.0")
		private Double distance;

		@Schema(description = "연습 영상 파일명", example = "video_20231029_001.mp4")
		private String videoName;

		@Schema(description = "연습 날짜", example = "2025-01-01T00:00:00")
		private LocalDateTime trainingDate;
	}
}
