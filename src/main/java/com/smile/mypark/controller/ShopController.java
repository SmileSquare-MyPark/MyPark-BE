package com.smile.mypark.controller;

import com.smile.mypark.dto.response.ShopResponseDTO;
import com.smile.mypark.global.annotation.AuthUser;
import com.smile.mypark.global.apipayload.ApiResponse;
import com.smile.mypark.service.ShopService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(name = "[ 매장 찾기 ]")
@RequestMapping("/api/v1/shops")
public class ShopController {

	private final ShopService shopService;

	@Operation(summary = "매장 찾기", description = "전체 매장 목록을 조회 및 검색")
	@GetMapping
	public ApiResponse<ShopResponseDTO> searchShops(
			@AuthUser Long userId,
			@Parameter(description = "검색할 매장 이름 키워드 (예: 강남점, 서울)")
			@RequestParam(required = false) String name) {

		if (name != null && !name.isBlank()) {
			return ApiResponse.onSuccess(shopService.searchShopsByName(userId, name));
		}

		return ApiResponse.onSuccess(shopService.getAllShops(userId));
	}
}
