package com.smile.mypark.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@Schema(description = "공지사항 응답 DTO")
public class NoticeResponseDTO {

    @Schema(description = "공지사항 ID", example = "1")
    private Long noticeId;

    @Schema(description = "공지사항 제목", example = "MyPark 업데이트 안내")
    private String title;

    @Schema(description = "공지사항 내용", example = "MyPark 업데이트 내용입니다.")
    private String content;

    @Schema(description = "등록일", example = "2025-01-01T00:00:00")
    private LocalDateTime regDate;
}
