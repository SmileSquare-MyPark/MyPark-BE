package com.smile.mypark.repository;

import com.smile.mypark.dto.response.HomeTapResponseDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

@Slf4j
@Repository
@RequiredArgsConstructor
public class HomeRepository {

    private final JdbcTemplate jdbcTemplate;

    private static final int RECENT_ROUNDS_COUNT = 5;
    private static final int DEFAULT_SCORE = 0;
    private static final double DEFAULT_AVG_SCORE = 0.0;
    private static final String EMPTY_STRING = "";

    private static final RowMapper<UserRow> USER_ROW_MAPPER = (rs, rowNum) -> new UserRow(
            rs.getString("u_nick"),
            rs.getDate("u_regdate").toLocalDate()
    );

    private static final RowMapper<StatRow> STAT_ROW_MAPPER = (rs, rowNum) -> new StatRow(
            rs.getInt("holeinone"),
            rs.getInt("albatross"),
            rs.getInt("eagle")
    );

    private static final RowMapper<NoticeRow> NOTICE_ROW_MAPPER = (rs, rowNum) -> new NoticeRow(
            rs.getString("b_title"),
            rs.getString("b_content")
    );

    private static final String SELECT_USER_INFO = """
            SELECT u_nick, u_regdate
            FROM TB_USER
            WHERE idx = ?
            """;

    private static final String SELECT_USER_STATS = """
            SELECT
                ISNULL(SUM(holeinone), 0) AS holeinone,
                ISNULL(SUM(albatross), 0) AS albatross,
                ISNULL(SUM(eagle), 0) AS eagle
            FROM TB_ROUNDING_PLAYER_INFO
            WHERE fk_user_idx = ?
            """;

    private static final String SELECT_BEST_SCORE = """
            SELECT MIN(swing_count)
            FROM TB_ROUNDING_INFO
            WHERE fk_idx = ?
            """;

    private static final String SELECT_RECENT_AVG_SCORE = """
            SELECT AVG(CAST(swing_count AS FLOAT))
            FROM (
                SELECT TOP (?) swing_count
                FROM TB_ROUNDING_INFO
                WHERE fk_idx = ?
                ORDER BY indyt DESC
            ) AS recent_rounds
            """;

    private static final String SELECT_LATEST_NOTICE = """
            SELECT TOP 1 b_title, b_content
            FROM TB_Shop_Board
            ORDER BY b_regdate DESC
            """;

    /**
     * 사용자 홈 탭 데이터를 조회
     *
     * @param userId 사용자 ID
     * @return 홈 탭 응답 DTO
     * @throws EmptyResultDataAccessException
     */
    public HomeTapResponseDTO findHomeTapData(Long userId) {
        log.debug("홈 탭 데이터 조회 시작 - userId: {}", userId);

        try {
            UserRow user = findUserInfo(userId);
            StatRow stats = findUserStats(userId);
            Integer bestScore = findBestScore(userId);
            Double recentAvgScore = findRecentAvgScore(userId);
            NoticeRow notice = findLatestNotice();

            long experienceYears = ChronoUnit.YEARS.between(user.uRegdate(), LocalDate.now());

            HomeTapResponseDTO response = HomeTapResponseDTO.builder()
                    .uId(userId)
                    .userNickname(user.uNickname())
                    .holeInOneCount(stats.holeinone())
                    .albatrossCount(stats.albatross())
                    .eagleCount(stats.eagle())
                    .bestScore(bestScore)
                    .experienceYears((int) experienceYears)
                    .recentAvgScore(recentAvgScore)
                    .noticeTitle(notice.bTitle())
                    .noticeContent(notice.bContent())
                    .build();

            log.debug("홈 탭 데이터 조회 완료 - userId: {}", userId);
            return response;

        } catch (EmptyResultDataAccessException e) {
            log.error("사용자 정보 조회 실패 - userId: {}", userId, e);
            throw e;
        } catch (Exception e) {
            log.error("홈 탭 데이터 조회 중 오류 발생 - userId: {}", userId, e);
            throw new RuntimeException("홈 탭 데이터 조회 중 오류가 발생했습니다.", e);
        }
    }

    /**
     * 사용자 기본 정보를 조회
     *
     * @param userId 사용자 ID
     * @return 사용자 정보
     * @throws EmptyResultDataAccessException
     */
    private UserRow findUserInfo(Long userId) {
        log.debug("사용자 정보 조회 - userId: {}", userId);
        return jdbcTemplate.queryForObject(SELECT_USER_INFO, USER_ROW_MAPPER, userId);
    }

    /**
     * 사용자 골프 통계를 조회
     *
     * @param userId 사용자 ID
     * @return 골프 통계 (결과 없으면 0으로 초기화된 통계)
     */
    private StatRow findUserStats(Long userId) {
        log.debug("사용자 통계 조회 - userId: {}", userId);
        try {
            return jdbcTemplate.queryForObject(SELECT_USER_STATS, STAT_ROW_MAPPER, userId);
        } catch (EmptyResultDataAccessException e) {
            log.warn("통계 정보 없음 - userId: {}, 기본값 반환", userId);
            return new StatRow(0, 0, 0);
        }
    }

    /**
     * 사용자 최고 점수를 조회
     *
     * @param userId 사용자 ID
     * @return 최고 점수 (결과 없으면 기본값 0)
     */
    private Integer findBestScore(Long userId) {
        log.debug("베스트 스코어 조회 - userId: {}", userId);
        return Optional.ofNullable(
                jdbcTemplate.queryForObject(SELECT_BEST_SCORE, Integer.class, userId)
        ).orElse(DEFAULT_SCORE);
    }

    /**
     * 최근 경기 평균 점수를 조회
     *
     * @param userId 사용자 ID
     * @return 최근 평균 점수 (결과 없으면 기본값 0.0)
     */
    private Double findRecentAvgScore(Long userId) {
        log.debug("최근 평균 스코어 조회 - userId: {}, 최근 {}경기", userId, RECENT_ROUNDS_COUNT);
        return Optional.ofNullable(
                jdbcTemplate.queryForObject(SELECT_RECENT_AVG_SCORE, Double.class,
                        RECENT_ROUNDS_COUNT, userId)
        ).orElse(DEFAULT_AVG_SCORE);
    }

    /**
     * 최신 공지사항을 조회
     *
     * @return 최신 공지사항 (결과 없으면 빈 문자열로 초기화된 공지사항)
     */
    private NoticeRow findLatestNotice() {
        log.debug("최신 공지사항 조회");
        try {
            return jdbcTemplate.queryForObject(SELECT_LATEST_NOTICE, NOTICE_ROW_MAPPER);
        } catch (EmptyResultDataAccessException e) {
            log.warn("공지사항 없음 - 기본값 반환");
            return new NoticeRow(EMPTY_STRING, EMPTY_STRING);
        }
    }

    private record UserRow(String uNickname, LocalDate uRegdate) {}
    private record StatRow(int holeinone, int albatross, int eagle) {}
    private record NoticeRow(String bTitle, String bContent) {}
}