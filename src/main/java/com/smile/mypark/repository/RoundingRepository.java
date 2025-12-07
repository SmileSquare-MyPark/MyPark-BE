package com.smile.mypark.repository;

import com.smile.mypark.dto.response.RoundingResponseDTO;
import com.smile.mypark.dto.response.ScoreStatisticsResponseDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Repository
@RequiredArgsConstructor
public class RoundingRepository {

	private final JdbcTemplate jdbcTemplate;

	private static final RowMapper<RoundingRow> ROUNDING_ROW_MAPPER = (rs, rowNum) -> {
		Timestamp timestamp = rs.getTimestamp("indyt");
		LocalDateTime roundingDate = timestamp != null ? timestamp.toLocalDateTime() : null;

		return new RoundingRow(
				rs.getObject("swing_count") != null ? rs.getInt("swing_count") : null,
				rs.getObject("average_distance") != null ? rs.getDouble("average_distance") : null,
				rs.getObject("greeninregulation") != null ? rs.getDouble("greeninregulation") : null,
				rs.getObject("putting_rate") != null ? rs.getDouble("putting_rate") : null,
				rs.getString("ccidx"),
				rs.getObject("max_distance") != null ? rs.getDouble("max_distance") : null,
				rs.getObject("max_putting_distance") != null ? rs.getDouble("max_putting_distance") : null,
				rs.getObject("score") != null ? rs.getInt("score") : null,
				roundingDate
		);
	};

	private static final String SELECT_ROUNDING_INFO_BY_USER = """
			SELECT
				swing_count,
				average_distance,
				greeninregulation,
				putting_rate,
				ccidx,
				max_distance,
				max_putting_distance,
				score,
				indyt
			FROM TB_ROUNDING_INFO
			WHERE fk_idx = ?
			ORDER BY indyt DESC
			""";

	private static final String COUNT_ROUNDING_BY_USER = """
			SELECT COUNT(*) AS total_count
			FROM TB_ROUNDING_INFO
			WHERE fk_idx = ?
			""";

	private static final RowMapper<ScoreStatisticsRow> SCORE_STATISTICS_ROW_MAPPER = (rs, rowNum) -> new ScoreStatisticsRow(
			rs.getInt("holeinone"),
			rs.getInt("albatross"),
			rs.getInt("eagle"),
			rs.getInt("birdie"),
			rs.getInt("par"),
			rs.getInt("bogey")
	);

	private static final String SELECT_SCORE_STATISTICS = """
			SELECT
				ISNULL(SUM(holeinone), 0) AS holeinone,
				ISNULL(SUM(albatross), 0) AS albatross,
				ISNULL(SUM(eagle), 0) AS eagle,
				ISNULL(SUM(birdie), 0) AS birdie,
				ISNULL(SUM(par), 0) AS par,
				ISNULL(SUM(bogey), 0) AS bogey
			FROM TB_ROUNDING_INFO
			WHERE fk_idx = ?
			""";

	/**
	 * 사용자의 라운딩 정보 목록을 조회
	 *
	 * @param userId 사용자 ID
	 * @return 라운딩 정보 리스트
	 */
	public List<RoundingResponseDTO.RoundingInfo> findRoundingInfoByUserId(Long userId) {
		log.debug("사용자 라운딩 정보 목록 조회 시작 - userId: {}", userId);
		try {
			List<RoundingRow> roundingRows = jdbcTemplate.query(
					SELECT_ROUNDING_INFO_BY_USER,
					ROUNDING_ROW_MAPPER,
					userId
			);
			List<RoundingResponseDTO.RoundingInfo> roundings = roundingRows.stream()
					.map(this::convertToRoundingInfo)
					.toList();
			log.debug("사용자 라운딩 정보 목록 조회 완료 - userId: {}, 라운드 수: {}", userId, roundings.size());
			return roundings;
		} catch (EmptyResultDataAccessException e) {
			log.warn("라운딩 정보 없음 - userId: {}", userId);
			return List.of();
		} catch (Exception e) {
			log.error("라운딩 정보 목록 조회 중 오류 발생 - userId: {}", userId, e);
			throw new RuntimeException("라운딩 정보 목록 조회 중 오류가 발생했습니다.", e);
		}
	}

	/**
	 * 사용자의 총 라운드 수를 조회
	 *
	 * @param userId 사용자 ID
	 * @return 총 라운드 수
	 */
	public Integer countRoundingByUserId(Long userId) {
		log.debug("사용자 총 라운드 수 조회 시작 - userId: {}", userId);
		try {
			Integer count = jdbcTemplate.queryForObject(COUNT_ROUNDING_BY_USER, Integer.class, userId);
			log.debug("사용자 총 라운드 수 조회 완료 - userId: {}, 총 라운드 수: {}", userId, count);
			return count != null ? count : 0;
		} catch (EmptyResultDataAccessException e) {
			log.warn("라운딩 정보 없음 - userId: {}", userId);
			return 0;
		} catch (Exception e) {
			log.error("총 라운드 수 조회 중 오류 발생 - userId: {}", userId, e);
			throw new RuntimeException("총 라운드 수 조회 중 오류가 발생했습니다.", e);
		}
	}

	/**
	 * RoundingRow를 RoundingInfo로 변환
	 *
	 * @param roundingRow 라운딩 row 데이터
	 * @return 라운딩 정보 DTO
	 */
	private RoundingResponseDTO.RoundingInfo convertToRoundingInfo(RoundingRow roundingRow) {
		return RoundingResponseDTO.RoundingInfo.builder()
				.roundingDate(roundingRow.roundingDate())
				.score(roundingRow.score())
				.averageDistance(roundingRow.averageDistance())
				.greenInRegulation(roundingRow.greenInRegulation())
				.puttingRate(roundingRow.puttingRate())
				.build();
	}

	/**
	 * 사용자의 스코어 통계를 조회 (전체 기록)
	 *
	 * @param userId 사용자 ID
	 * @return 스코어 통계 정보
	 */
	public ScoreStatisticsResponseDTO findScoreStatistics(Long userId) {
		log.debug("스코어 통계 조회 시작 - userId: {}", userId);
		try {
			ScoreStatisticsRow stats = jdbcTemplate.queryForObject(
					SELECT_SCORE_STATISTICS,
					SCORE_STATISTICS_ROW_MAPPER,
					userId
			);
			log.debug("쿼리 결과 - holeinone: {}, albatross: {}, eagle: {}, birdie: {}, par: {}, bogey: {}",
					stats.holeinone(), stats.albatross(), stats.eagle(), stats.birdie(), stats.par(), stats.bogey());
			ScoreStatisticsResponseDTO response = ScoreStatisticsResponseDTO.builder()
					.holeInOne(stats.holeinone())
					.albatross(stats.albatross())
					.eagle(stats.eagle())
					.birdie(stats.birdie())
					.par(stats.par())
					.bogey(stats.bogey())
					.build();
			log.debug("스코어 통계 조회 완료 - userId: {}, holeInOne: {}, albatross: {}, eagle: {}, birdie: {}, par: {}, bogey: {}",
					userId, response.getHoleInOne(), response.getAlbatross(), response.getEagle(),
					response.getBirdie(), response.getPar(), response.getBogey());
			return response;
		} catch (EmptyResultDataAccessException e) {
			log.warn("스코어 통계 정보 없음 - userId: {}, 기본값 반환", userId);
			return ScoreStatisticsResponseDTO.builder()
					.holeInOne(0)
					.albatross(0)
					.eagle(0)
					.birdie(0)
					.par(0)
					.bogey(0)
					.build();
		} catch (Exception e) {
			log.error("스코어 통계 조회 중 오류 발생 - userId: {}", userId, e);
			throw new RuntimeException("스코어 통계 조회 중 오류가 발생했습니다.", e);
		}
	}

	private record RoundingRow(
			Integer swingCount,
			Double averageDistance,
			Double greenInRegulation,
			Double puttingRate,
			String ccIdx,
			Double maxDistance,
			Double maxPuttingDistance,
			Integer score,
			LocalDateTime roundingDate
	) {
	}

	private record ScoreStatisticsRow(
			int holeinone,
			int albatross,
			int eagle,
			int birdie,
			int par,
			int bogey
	) {
	}
}
