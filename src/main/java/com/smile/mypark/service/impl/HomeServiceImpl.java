package com.smile.mypark.service.impl;

import com.smile.mypark.dto.response.HomeTapResponseDTO;
import com.smile.mypark.dto.response.NoticeResponseDTO;
import com.smile.mypark.repository.HomeRepository;
import com.smile.mypark.repository.NoticeRepository;
import com.smile.mypark.service.HomeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class HomeServiceImpl implements HomeService {

    private final HomeRepository homeRepository;
    private final NoticeRepository noticeRepository;

    @Override
    @Transactional(readOnly = true)
    public HomeTapResponseDTO getHomeTap(Long userId) {
        return homeRepository.findHomeTapData(userId);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<NoticeResponseDTO> getNotices(Pageable pageable) {
        return noticeRepository.findNotices(pageable);
    }
}