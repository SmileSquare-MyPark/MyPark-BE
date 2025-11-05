package com.smile.mypark.service;

import com.smile.mypark.dto.response.ShopResponseDTO;

public interface ShopService {
	ShopResponseDTO getAllShops(Long userId);

	ShopResponseDTO searchShopsByName(Long userId, String name);

	void likeShop(Long userId, String shopCode);

	void unlikeShop(Long userId, String shopCode);
}
