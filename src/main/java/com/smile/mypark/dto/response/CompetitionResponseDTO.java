package com.smile.mypark.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@Schema(description = "대회 탭 응답 DTO")
public class CompetitionResponseDTO {

    @Schema(description = "진행 중인 대회 목록")
    private List<CompetitionInfo> ongoingCompetitions;

    @Schema(description = "종료된 대회 목록")
    private List<CompetitionInfo> completedCompetitions;

    @Getter
    @Builder
    @Schema(description = "대회 상세 정보")
    public static class CompetitionInfo {

        @Schema(description = "대회 ID", example = "1")
        private Long championshipId;

        @Schema(description = "대회 제목", example = "2025 봄 시즌 챔피언십")
        private String title;

        @Schema(description = "대회 설명", example = "봄 시즌 골프 대회에 참여하세요")
        private String summary;

        @Schema(description = "상세 내용", example = "대회 상세 설명 및 규칙")
        private String description;

        @Schema(description = "시상 내용", example = "1등 100만원, 2등 50만원")
        private String prizeDescription;

        @Schema(description = "참여자 수", example = "128")
        private Integer participantCount;

        @Schema(description = "사용자 타수 (없으면 null)", example = "72")
        private Integer userScore;

        @Schema(description = "사용자 순위 (없으면 null)", example = "15")
        private Integer userRank;

        @Schema(description = "대회 진행 여부", example = "true")
        private Boolean isOngoing;
    }
}