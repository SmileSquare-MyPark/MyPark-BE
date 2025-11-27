package com.smile.mypark.service;

import com.smile.mypark.dto.response.HomeTapResponseDTO;
import com.smile.mypark.dto.response.NoticeResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface HomeService {
    HomeTapResponseDTO getHomeTap(Long userId);

    Page<NoticeResponseDTO> getNotices(Pageable pageable);
}