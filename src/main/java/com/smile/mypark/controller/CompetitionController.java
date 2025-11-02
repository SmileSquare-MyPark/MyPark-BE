package com.smile.mypark.controller;

import com.smile.mypark.dto.response.CompetitionResponseDTO;
import com.smile.mypark.global.annotation.AuthUser;
import com.smile.mypark.global.apipayload.ApiResponse;
import com.smile.mypark.service.CompetitionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(name = "[ 대회 탭 ]")
@RequestMapping("/api/v1/competition")
public class CompetitionController {

    private final CompetitionService competitionService;

    @Operation(
            summary = "대회 탭 조회",
            description = "진행 중인 대회와 종료된 대회의 정보를 조회합니다. 사용자의 참여 여부, 타수, 순위 정보를 포함합니다."
    )
    @GetMapping
    public ApiResponse<CompetitionResponseDTO> getCompetition(@AuthUser Long userId) {
        return ApiResponse.onSuccess(competitionService.getCompetition(userId));
    }
}
