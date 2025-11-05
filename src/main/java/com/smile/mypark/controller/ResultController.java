package com.smile.mypark.controller;

import com.smile.mypark.dto.response.RoundingResponseDTO;
import com.smile.mypark.dto.response.TrainingResponseDTO;
import com.smile.mypark.global.annotation.AuthUser;
import com.smile.mypark.global.apipayload.ApiResponse;
import com.smile.mypark.service.ResultService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(name = "[ 결과 탭 ]")
@RequestMapping("/api/v1/result")
public class ResultController {

	private final ResultService resultService;

	@Operation(summary = "연습 결과 조회", description = "사용자의 연습 샷 기록을 조회")
	@GetMapping("/training")
	public ApiResponse<TrainingResponseDTO> getTrainingResults(@AuthUser Long userId) {
		return ApiResponse.onSuccess(resultService.getTrainingResults(userId));
	}

	@Operation(summary = "라운딩 결과 조회", description = "사용자의 라운딩 기록을 조회")
	@GetMapping("/rounding")
	public ApiResponse<RoundingResponseDTO> getRoundingResults(@AuthUser Long userId) {
		return ApiResponse.onSuccess(resultService.getRoundingResults(userId));
	}
}