package com.smile.mypark.repository;

import com.smile.mypark.dto.response.HomeTapResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Repository
@RequiredArgsConstructor
public class HomeRepository {

    private final JdbcTemplate jdbcTemplate;

    public HomeTapResponseDTO findHomeTapData(Long userId) {

        var userQuery = """
            SELECT u_nickname, u_regdate
            FROM TB_USER
            WHERE u_id = ?
        """;

        var user = jdbcTemplate.queryForObject(userQuery, (rs, rowNum) -> new UserRow(
                rs.getString("u_nickname"),
                rs.getDate("u_regdate").toLocalDate()
        ), userId);

        var statQuery = """
            SELECT 
                ISNULL(SUM(holeinone), 0) AS holeinone,
                ISNULL(SUM(albatross), 0) AS albatross,
                ISNULL(SUM(eagle), 0) AS eagle
            FROM TB_ROUNDING_PLAYER_INFO
            WHERE u_id = ?
        """;

        var stats = jdbcTemplate.queryForObject(statQuery, (rs, rowNum) -> new StatRow(
                rs.getInt("holeinone"),
                rs.getInt("albatross"),
                rs.getInt("eagle")
        ), userId);

        String bestScoreSql = """
            SELECT MIN(swing_count)
            FROM TB_ROUNDING_INFO
            WHERE u_id = ?
        """;
        Integer bestScore = jdbcTemplate.queryForObject(bestScoreSql, Integer.class, userId);

        String avgScoreSql = """
            SELECT AVG(CAST(swing_count AS FLOAT))
            FROM (
                SELECT TOP 5 swing_count
                FROM TB_ROUNDING_INFO
                WHERE u_id = ?
                ORDER BY play_date DESC
            ) AS recent_rounds
        """;
        Double recentAvgScore = jdbcTemplate.queryForObject(avgScoreSql, Double.class, userId);

        String noticeSql = """
            SELECT TOP 1 b_title, b_content
            FROM TB_Shop_Board
            ORDER BY b_createdate DESC
        """;
        var notice = jdbcTemplate.query(noticeSql, (ResultSet rs) ->
                rs.next() ? new NoticeRow(
                        rs.getString("b_title"),
                        rs.getString("b_content")
                ) : new NoticeRow("", "")
        );

        long experienceYears = ChronoUnit.YEARS.between(user.uRegdate(), LocalDate.now());

        return HomeTapResponseDTO.builder()
                .uId(userId)
                .userNickname(user.uNickname())
                .holeInOneCount(stats.holeinone())
                .albatrossCount(stats.albatross())
                .eagleCount(stats.eagle())
                .bestScore(bestScore != null ? bestScore : 0)
                .experienceYears((int) experienceYears)
                .recentAvgScore(recentAvgScore != null ? recentAvgScore : 0.0)
                .noticeTitle(notice.bTitle())
                .noticeContent(notice.bContent())
                .build();
    }

    private record UserRow(String uNickname, LocalDate uRegdate) {}
    private record StatRow(int holeinone, int albatross, int eagle) {}
    private record NoticeRow(String bTitle, String bContent) {}
}