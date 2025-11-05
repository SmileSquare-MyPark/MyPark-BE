package com.smile.mypark.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@Schema(description = "매장 찾기 응답 DTO")
public class ShopResponseDTO {

	@Schema(description = "매장 목록")
	private List<ShopInfo> shops;

	@Getter
	@Builder
	@Schema(description = "매장 정보")
	public static class ShopInfo {

		@Schema(description = "매장 이름", example = "강남점")
		private String shopName;

		@Schema(description = "주소1", example = "서울특별시 강남구")
		private String address1;

		@Schema(description = "주소2", example = "테헤란로 123")
		private String address2;

		@Schema(description = "전화번호", example = "02-1234-5678")
		private String telephone;

		@Schema(description = "보유 기기 수", example = "5")
		private Integer simulationCnt;

		@Schema(description = "매장 별점", example = "4.5")
		private Double shopRating;

		@Schema(description = "사용자 찜 여부", example = "true")
		private Boolean isLiked;
	}
}
