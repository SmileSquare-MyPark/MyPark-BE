package com.smile.mypark.service.impl;

import com.smile.mypark.dto.response.ShopResponseDTO;
import com.smile.mypark.repository.ShopRepository;
import com.smile.mypark.service.ShopService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ShopServiceImpl implements ShopService {

	private final ShopRepository shopRepository;

	@Override
	@Transactional(readOnly = true)
	public ShopResponseDTO getAllShops(Long userId) {
		List<ShopResponseDTO.ShopInfo> shops = shopRepository.findAllShops(userId);
		return ShopResponseDTO.builder()
				.shops(shops)
				.build();
	}

	@Override
	@Transactional(readOnly = true)
	public ShopResponseDTO searchShopsByName(Long userId, String name) {
		List<ShopResponseDTO.ShopInfo> shops = shopRepository.findShopsByName(userId, name);
		return ShopResponseDTO.builder()
				.shops(shops)
				.build();
	}
}
