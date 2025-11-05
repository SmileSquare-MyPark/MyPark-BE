package com.smile.mypark.controller;

import com.smile.mypark.dto.response.ShopResponseDTO;
import com.smile.mypark.global.annotation.AuthUser;
import com.smile.mypark.global.apipayload.ApiResponse;
import com.smile.mypark.service.ShopService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

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

	@Operation(summary = "매장 찜하기", description = "매장을 찜 목록에 추가합니다")
	@PostMapping("/{shopCode}/like")
	public ApiResponse<Void> likeShop(
			@AuthUser Long userId,
			@Parameter(description = "매장 코드")
            @PathVariable String shopCode) {
		shopService.likeShop(userId, shopCode);
		return ApiResponse.onSuccess(null);
	}

	@Operation(summary = "매장 찜 취소", description = "매장을 찜 목록에서 제거합니다")
	@DeleteMapping("/{shopCode}/like")
	public ApiResponse<Void> unlikeShop(
			@AuthUser Long userId,
			@Parameter(description = "매장 코드")
            @PathVariable String shopCode) {
		shopService.unlikeShop(userId, shopCode);
		return ApiResponse.onSuccess(null);
	}
}
