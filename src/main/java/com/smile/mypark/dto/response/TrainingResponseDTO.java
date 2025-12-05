package com.smile.mypark.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Builder
@Schema(description = "연습 결과 응답 DTO")
public class TrainingResponseDTO {

	@Schema(description = "날짜별 연습 목록")
	private List<DateTrainingGroup> trainings;

	@Getter
	@Builder
	@Schema(description = "날짜별 연습 그룹")
	public static class DateTrainingGroup {

		@Schema(description = "연습 날짜", example = "2025-01-01")
		private LocalDate trainingDate;

		@Schema(description = "해당 날짜의 연습 샷 목록")
		private List<TrainingShotInfo> trainingList;
	}

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
	}
}
