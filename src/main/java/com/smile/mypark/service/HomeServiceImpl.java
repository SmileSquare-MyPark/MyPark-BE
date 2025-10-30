package com.smile.mypark.service;

import com.smile.mypark.dto.response.HomeTapResponseDTO;
import com.smile.mypark.repository.HomeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class HomeServiceImpl implements HomeService {

    private final HomeRepository homeRepository;

    @Override
    @Transactional(readOnly = true)
    public HomeTapResponseDTO getHomeTap(Long userId) {
        return homeRepository.findHomeTapData(userId);
    }
}