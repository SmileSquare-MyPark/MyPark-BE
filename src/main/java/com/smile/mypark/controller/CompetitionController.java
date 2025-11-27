package com.smile.mypark.controller;

import com.smile.mypark.dto.response.CompetitionResponseDTO;
import com.smile.mypark.global.annotation.AuthUser;
import com.smile.mypark.global.apipayload.ApiResponse;
import com.smile.mypark.service.CompetitionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Tag(name = "[ 대회 탭 ]")
@RequestMapping("/api/v1/competition")
public class CompetitionController {

    private final CompetitionService competitionService;

    @Operation(summary = "대회 탭 조회", description = "진행 중인 대회와 종료된 대회의 정보를 조회")
    @GetMapping
    public ApiResponse<List<CompetitionResponseDTO>> getCompetitions(
        @AuthUser Long userId,
        @RequestParam(value = "page", defaultValue = "0") int page,
        @RequestParam(value = "size", defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<CompetitionResponseDTO> results = competitionService.getCompetitions(userId, pageable);
        return ApiResponse.onSuccess(results.getContent());
    }
}
