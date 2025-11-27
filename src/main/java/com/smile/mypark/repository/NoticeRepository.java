package com.smile.mypark.repository;

import com.smile.mypark.dto.response.NoticeResponseDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Slf4j
@Repository
@RequiredArgsConstructor
public class NoticeRepository {

    private final JdbcTemplate jdbcTemplate;

    private static final RowMapper<NoticeRow> NOTICE_ROW_MAPPER = (rs, rowNum) -> new NoticeRow(
            rs.getLong("idx"),
            rs.getString("b_title"),
            rs.getString("b_content"),
            rs.getTimestamp("b_regdate") != null ? rs.getTimestamp("b_regdate").toLocalDateTime() : null
    );

    private static final String SELECT_NOTICES = """
            SELECT idx, b_title, b_content, b_regdate
            FROM TB_Shop_Board
            ORDER BY b_regdate DESC
            OFFSET ? ROWS
            FETCH NEXT ? ROWS ONLY
            """;

    private static final String COUNT_NOTICES = """
            SELECT COUNT(*)
            FROM TB_Shop_Board
            """;

    /**
     * 공지사항을 페이징하여 조회
     *
     * @param pageable 페이징 정보
     * @return 공지사항 페이지
     */
    public Page<NoticeResponseDTO> findNotices(Pageable pageable) {
        log.debug("공지사항 조회 시작 - page: {}, size: {}", pageable.getPageNumber(), pageable.getPageSize());

        try {
            int offset = (int) pageable.getOffset();
            int pageSize = pageable.getPageSize();

            List<NoticeRow> notices = jdbcTemplate.query(SELECT_NOTICES, NOTICE_ROW_MAPPER, offset, pageSize);

            List<NoticeResponseDTO> results = notices.stream()
                    .map(notice -> NoticeResponseDTO.builder()
                            .noticeId(notice.bIdx())
                            .title(notice.bTitle())
                            .content(notice.bContent())
                            .regDate(notice.bRegdate())
                            .build())
                    .toList();

            long totalCount = countNotices();

            log.debug("공지사항 조회 완료 - total: {}", totalCount);
            return new PageImpl<>(results, pageable, totalCount);

        } catch (Exception e) {
            log.error("공지사항 조회 중 오류 발생", e);
            throw new RuntimeException("공지사항 조회 중 오류가 발생했습니다.", e);
        }
    }

    /**
     * 전체 공지사항 개수를 조회
     *
     * @return 전체 공지사항 개수
     */
    private long countNotices() {
        try {
            Integer count = jdbcTemplate.queryForObject(COUNT_NOTICES, Integer.class);
            return count != null ? count : 0;
        } catch (Exception e) {
            log.warn("공지사항 개수 조회 실패", e);
            return 0;
        }
    }

    private record NoticeRow(
            Long bIdx,
            String bTitle,
            String bContent,
            java.time.LocalDateTime bRegdate
    ) {
    }
}
