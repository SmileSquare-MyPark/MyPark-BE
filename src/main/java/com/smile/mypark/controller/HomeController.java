package com.smile.mypark.controller;

import com.smile.mypark.dto.response.HomeTapResponseDTO;
import com.smile.mypark.global.annotation.AuthUser;
import com.smile.mypark.global.apipayload.ApiResponse;
import com.smile.mypark.service.HomeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(name = "[ 홈 탭 ]")
@RequestMapping("/api/v1/home")
public class HomeController {

    private final HomeService homeService;

    @Operation(summary = "홈 탭", description = "홈 탭 API")
    @GetMapping
    public ApiResponse<HomeTapResponseDTO> getHomeTap(@AuthUser Long userId) {
        return ApiResponse.onSuccess(homeService.getHomeTap(userId));
    }
}