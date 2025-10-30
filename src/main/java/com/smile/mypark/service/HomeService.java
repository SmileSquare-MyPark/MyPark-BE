package com.smile.mypark.service;

import com.smile.mypark.dto.response.HomeTapResponseDTO;

public interface HomeService {
    HomeTapResponseDTO getHomeTap(Long userId);
}