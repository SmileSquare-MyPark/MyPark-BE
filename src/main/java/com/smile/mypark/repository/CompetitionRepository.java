package com.smile.mypark.repository;

import com.smile.mypark.dto.response.CompetitionResponseDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
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
public class CompetitionRepository {

    private final JdbcTemplate jdbcTemplate;
    private static final int ONGOING_STATUS = 0;
    private static final int COMPLETED_STATUS = 1;
    private static final int MIN_VALID_SCORE = 18;
    private static final int MAX_VALID_SCORE = 200;

    private static final RowMapper<ChampionshipRow> CHAMPIONSHIP_ROW_MAPPER = (rs, rowNum) -> new ChampionshipRow(
            rs.getLong("seq"),
            rs.getString("roomname"),
            rs.getString("summary"),
            rs.getString("description"),
            rs.getString("prizeDescription"),
            rs.getInt("joinplayer"),
            rs.getInt("isend"),
            rs.getTimestamp("startDate") != null ? rs.getTimestamp("startDate").toLocalDateTime() : null,
            rs.getTimestamp("endDate") != null ? rs.getTimestamp("endDate").toLocalDateTime() : null
    );

    private static final RowMapper<PlayerScoreRow> PLAYER_SCORE_ROW_MAPPER = (rs, rowNum) -> new PlayerScoreRow(
            rs.getInt("TotalScore"),
            rs.getInt("user_rank")
    );

    private static final String SELECT_ALL_CHAMPIONSHIPS = """
            SELECT seq, roomname, summary, description, prizeDescription, joinplayer, isend, startDate, endDate
            FROM TB_CHAMPIONSHIP
            ORDER BY seq DESC
            OFFSET ? ROWS
            FETCH NEXT ? ROWS ONLY
            """;

    private static final String COUNT_ALL_CHAMPIONSHIPS = """
            SELECT COUNT(*)
            FROM TB_CHAMPIONSHIP
            """;

    private static final String SELECT_USER_SCORE_AND_RANK = """
            SELECT
                pi.TotalScore,
                (
                    SELECT COUNT(*) + 1
                    FROM TB_CHAMPIONSHIP_PLAYER_INFO
                    WHERE fk_room_idx = pi.fk_room_idx
                        AND TotalScore < pi.TotalScore
                        AND TotalScore IS NOT NULL
                        AND TotalScore > 0
                        AND TotalScore BETWEEN %d AND %d
                ) AS user_rank
            FROM TB_CHAMPIONSHIP_PLAYER_INFO pi
            WHERE pi.fk_room_idx = ?
                AND pi.fk_user_idx = ?
                AND pi.TotalScore IS NOT NULL
                AND pi.TotalScore > 0
                AND pi.TotalScore BETWEEN %d AND %d
            """.formatted(MIN_VALID_SCORE, MAX_VALID_SCORE, MIN_VALID_SCORE, MAX_VALID_SCORE);

    /**
     * 모든 대회 정보를 최신순으로 페이징 조회
     *
     * @param userId 사용자 ID
     * @param pageable 페이징 정보
     * @return 대회 정보 페이지
     */
    public Page<CompetitionResponseDTO.CompetitionInfo> findCompetitionData(Long userId, Pageable pageable) {
        log.debug("대회 정보 조회 시작 - userId: {}, page: {}, size: {}", userId, pageable.getPageNumber(), pageable.getPageSize());

        try {
            int offset = (int) pageable.getOffset();
            int pageSize = pageable.getPageSize();

            List<ChampionshipRow> championships = findAllChampionships(offset, pageSize);

            List<CompetitionResponseDTO.CompetitionInfo> results = championships.stream()
                    .map(championship -> {
                        PlayerScoreRow playerScore = findUserScoreAndRank(championship.seq(), userId);

                        return CompetitionResponseDTO.CompetitionInfo.builder()
                                .championshipId(championship.seq())
                                .title(championship.roomname())
                                .summary(championship.summary())
                                .description(championship.description())
                                .prizeDescription(championship.prizeDescription())
                                .participantCount(championship.joinplayer())
                                .userScore(playerScore != null ? playerScore.totalScore() : null)
                                .userRank(playerScore != null ? playerScore.userRank() : null)
                                .isOngoing(championship.isend() == ONGOING_STATUS)
                                .startDate(championship.startDate())
                                .endDate(championship.endDate())
                                .build();
                    })
                    .toList();

            long totalCount = countAllChampionships();

            log.debug("대회 정보 조회 완료 - userId: {}, total: {}", userId, totalCount);
            return new PageImpl<>(results, pageable, totalCount);

        } catch (Exception e) {
            log.error("대회 정보 조회 중 오류 발생 - userId: {}", userId, e);
            throw new RuntimeException("대회 정보 조회 중 오류가 발생했습니다.", e);
        }
    }

    /**
     * 모든 대회를 조회 (최신순)
     *
     * @param offset 시작 위치
     * @param limit 조회 개수
     * @return 대회 정보 리스트
     */
    private List<ChampionshipRow> findAllChampionships(int offset, int limit) {
        log.debug("전체 대회 조회 (최신순) - offset: {}, limit: {}", offset, limit);

        try {
            return jdbcTemplate.query(SELECT_ALL_CHAMPIONSHIPS, CHAMPIONSHIP_ROW_MAPPER, offset, limit);
        } catch (Exception e) {
            log.warn("전체 대회 조회 실패", e);
            return List.of();
        }
    }

    /**
     * 전체 대회 개수를 조회
     *
     * @return 전체 대회 개수
     */
    private long countAllChampionships() {
        try {
            Integer count = jdbcTemplate.queryForObject(COUNT_ALL_CHAMPIONSHIPS, Integer.class);
            return count != null ? count : 0;
        } catch (Exception e) {
            log.warn("전체 대회 개수 조회 실패", e);
            return 0;
        }
    }

    /**
     * 특정 대회의 사용자 점수 및 순위를 조회
     *
     * @param championshipId 대회 ID
     * @param userId         사용자 ID
     * @return 사용자 점수 및 순위 (참여하지 않았으면 null)
     */
    private PlayerScoreRow findUserScoreAndRank(Long championshipId, Long userId) {
        log.debug("사용자 점수 및 순위 조회 - championshipId: {}, userId: {}", championshipId, userId);

        try {
            return jdbcTemplate.queryForObject(
                    SELECT_USER_SCORE_AND_RANK,
                    PLAYER_SCORE_ROW_MAPPER,
                    championshipId,
                    userId
            );
        } catch (EmptyResultDataAccessException e) {
            log.info("대회 미참여 또는 점수 없음 - championshipId: {}, userId: {}", championshipId, userId);
            return null;
        }
    }

    private record ChampionshipRow(
            Long seq,
            String roomname,
            String summary,
            String description,
            String prizeDescription,
            Integer joinplayer,
            Integer isend,
            java.time.LocalDateTime startDate,
            java.time.LocalDateTime endDate
    ) {
    }

    private record PlayerScoreRow(
            Integer totalScore,
            Integer userRank
    ) {
    }
}