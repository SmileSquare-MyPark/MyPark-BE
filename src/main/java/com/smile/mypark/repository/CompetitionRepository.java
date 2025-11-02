package com.smile.mypark.repository;

import com.smile.mypark.dto.response.CompetitionResponseDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

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
            rs.getInt("isend")
    );

    private static final RowMapper<PlayerScoreRow> PLAYER_SCORE_ROW_MAPPER = (rs, rowNum) -> new PlayerScoreRow(
            rs.getInt("TotalScore"),
            rs.getInt("user_rank")
    );

    private static final String SELECT_ONGOING_CHAMPIONSHIP = """
            SELECT TOP 1 seq, roomname, summary, description, prizeDescription, joinplayer, isend
            FROM TB_CHAMPIONSHIP
            WHERE isend = ?
            ORDER BY seq DESC
            """;

    private static final String SELECT_COMPLETED_CHAMPIONSHIP = """
            SELECT TOP 1 seq, roomname, summary, description, prizeDescription, joinplayer, isend
            FROM TB_CHAMPIONSHIP
            WHERE isend = ?
            ORDER BY seq DESC
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
     * 사용자의 대회 정보를 조회
     *
     * @param userId 사용자 ID
     * @return 대회 응답 DTO
     */
    public CompetitionResponseDTO findCompetitionData(Long userId) {
        log.debug("대회 정보 조회 시작 - userId: {}", userId);

        try {

            CompetitionResponseDTO.CompetitionInfo ongoingCompetition = findChampionshipInfo(
                    userId, ONGOING_STATUS
            );

            CompetitionResponseDTO.CompetitionInfo completedCompetition = findChampionshipInfo(
                    userId, COMPLETED_STATUS
            );

            CompetitionResponseDTO response = CompetitionResponseDTO.builder()
                    .ongoingCompetition(ongoingCompetition)
                    .completedCompetition(completedCompetition)
                    .build();

            log.debug("대회 정보 조회 완료 - userId: {}", userId);
            return response;

        } catch (Exception e) {
            log.error("대회 정보 조회 중 오류 발생 - userId: {}", userId, e);
            throw new RuntimeException("대회 정보 조회 중 오류가 발생했습니다.", e);
        }
    }

    /**
     * 특정 상태의 대회 정보를 조회
     *
     * @param userId 사용자 ID
     * @param status 대회 상태 (0: 진행중, 1: 종료)
     * @return 대회 정보 (없으면 null)
     */
    private CompetitionResponseDTO.CompetitionInfo findChampionshipInfo(Long userId, int status) {
        String statusName = status == ONGOING_STATUS ? "진행중" : "종료";
        log.debug("{} 대회 조회 - userId: {}", statusName, userId);

        try {

            ChampionshipRow championship = findChampionship(status);

            if (championship == null) {
                log.info("{} 대회 없음 - userId: {}", statusName, userId);
                return null;
            }

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
                    .isOngoing(status == ONGOING_STATUS)
                    .build();

        } catch (Exception e) {
            log.error("{} 대회 조회 실패 - userId: {}", statusName, userId, e);
            return null;
        }
    }

    /**
     * 특정 상태의 대회를 조회
     *
     * @param status 대회 상태 (0: 진행중, 1: 종료)
     * @return 대회 정보 (없으면 null)
     */
    private ChampionshipRow findChampionship(int status) {
        String query = status == ONGOING_STATUS ? SELECT_ONGOING_CHAMPIONSHIP : SELECT_COMPLETED_CHAMPIONSHIP;
        String statusName = status == ONGOING_STATUS ? "진행중" : "종료";

        log.debug("{} 대회 기본 정보 조회 - status: {}", statusName, status);

        try {
            return jdbcTemplate.queryForObject(query, CHAMPIONSHIP_ROW_MAPPER, status);
        } catch (EmptyResultDataAccessException e) {
            log.warn("{} 대회 없음 - status: {}", statusName, status);
            return null;
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
            Integer isend
    ) {
    }

    private record PlayerScoreRow(
            Integer totalScore,
            Integer userRank
    ) {
    }
}