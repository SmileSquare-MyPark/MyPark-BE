package com.smile.mypark.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Schema(description = "홈 탭 응답 DTO")
public class HomeTapResponseDTO {

    @Schema(description = "사용자 ID", example = "1")
    private Long uId;

    @Schema(description = "사용자 닉네임", example = "MyPark")
    private String userNickname;

    @Schema(description = "홀인원 개수", example = "2")
    private Integer holeInOneCount;

    @Schema(description = "알바트로스 개수", example = "1")
    private Integer albatrossCount;

    @Schema(description = "이글 개수", example = "5")
    private Integer eagleCount;

    @Schema(description = "베스트 타수", example = "68")
    private Integer bestScore;

    @Schema(description = "골프 경력(년)", example = "3")
    private Integer experienceYears;

    @Schema(description = "최근 5경기 평균 타수", example = "74")
    private Double recentAvgScore;
}