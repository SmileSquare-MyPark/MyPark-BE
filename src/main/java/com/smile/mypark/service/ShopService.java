package com.smile.mypark.service;

import com.smile.mypark.dto.response.ShopResponseDTO;

public interface ShopService {
	ShopResponseDTO getAllShops(Long userId);

	ShopResponseDTO searchShopsByName(Long userId, String name);
}
