package com.smile.mypark.repository;

import com.smile.mypark.dto.response.TrainingResponseDTO;
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
public class TrainingRepository {

	private final JdbcTemplate jdbcTemplate;

	private static final RowMapper<TrainingShotRow> TRAINING_SHOT_ROW_MAPPER = (rs, rowNum) -> {
		Timestamp timestamp = rs.getTimestamp("reg_date");
		LocalDateTime trainingDate = timestamp != null ? timestamp.toLocalDateTime() : null;

		return new TrainingShotRow(
				rs.getObject("direction_angle") != null ? rs.getDouble("direction_angle") : null,
				rs.getObject("launch_angle") != null ? rs.getDouble("launch_angle") : null,
				rs.getObject("ball_speed") != null ? rs.getDouble("ball_speed") : null,
				rs.getObject("club_speed") != null ? rs.getDouble("club_speed") : null,
				rs.getObject("distance") != null ? rs.getDouble("distance") : null,
				rs.getString("video_name"),
				trainingDate
		);
	};

	private static final String SELECT_TRAINING_SHOTS_BY_USER = """
			SELECT
				direction_angle,
				launch_angle,
				ball_speed,
				club_speed,
				distance,
				video_name,
				reg_date
			FROM TB_TRAINNING_SHOT
			WHERE fk_user_id = ?
			ORDER BY reg_date DESC
			OFFSET ? ROWS
			FETCH NEXT ? ROWS ONLY
			""";

	/**
	 * 사용자의 연습 샷 목록을 조회
	 *
	 * @param userId 사용자 ID
	 * @return 연습 샷 정보 리스트
	 */
	public List<TrainingResponseDTO.TrainingShotInfo> findTrainingShotsByUserId(Long userId) {
		log.debug("사용자 연습 샷 목록 조회 시작 - userId: {}", userId);
		try {
			List<TrainingShotRow> shotRows = jdbcTemplate.query(
					SELECT_TRAINING_SHOTS_BY_USER,
					TRAINING_SHOT_ROW_MAPPER,
					userId
			);
			List<TrainingResponseDTO.TrainingShotInfo> shots = shotRows.stream()
					.map(this::convertToTrainingShotInfo)
					.toList();
			log.debug("사용자 연습 샷 목록 조회 완료 - userId: {}, 샷 수: {}", userId, shots.size());
			return shots;
		} catch (EmptyResultDataAccessException e) {
			log.warn("연습 샷 정보 없음 - userId: {}", userId);
			return List.of();
		} catch (Exception e) {
			log.error("연습 샷 목록 조회 중 오류 발생 - userId: {}", userId, e);
			throw new RuntimeException("연습 샷 목록 조회 중 오류가 발생했습니다.", e);
		}
	}

	/**
	 * TrainingShotRow를 TrainingShotInfo로 변환
	 *
	 * @param shotRow 연습 샷 row 데이터
	 * @return 연습 샷 정보 DTO
	 */
	private TrainingResponseDTO.TrainingShotInfo convertToTrainingShotInfo(TrainingShotRow shotRow) {
		return TrainingResponseDTO.TrainingShotInfo.builder()
				.directionAngle(shotRow.directionAngle())
				.launchAngle(shotRow.launchAngle())
				.ballSpeed(shotRow.ballSpeed())
				.clubSpeed(shotRow.clubSpeed())
				.distance(shotRow.distance())
				.videoName(shotRow.videoName())
				.build();
	}

	/**
	 * TrainingShotRow를 TrainingShotInfo와 날짜와 함께 변환
	 *
	 * @param shotRow 연습 샷 row 데이터
	 * @return 날짜와 연습 샷 정보가 포함된 래퍼 객체
	 */
	public TrainingShotInfoWithDate convertToTrainingShotInfoWithDate(TrainingShotRow shotRow) {
		TrainingResponseDTO.TrainingShotInfo info = convertToTrainingShotInfo(shotRow);
		return new TrainingShotInfoWithDate(
				shotRow.trainingDate() != null ? shotRow.trainingDate().toLocalDate() : null,
				info
		);
	}

	public record TrainingShotInfoWithDate(
			java.time.LocalDate date,
			TrainingResponseDTO.TrainingShotInfo info
	) {
	}

	/**
	 * 사용자의 연습 샷을 날짜와 함께 페이징하여 조회
	 *
	 * @param userId 사용자 ID
	 * @param page 페이지 번호
	 * @param size 페이지 크기
	 * @return 날짜와 연습 샷 정보가 포함된 리스트
	 */
	public List<TrainingShotInfoWithDate> findTrainingShotsWithDateByUserId(Long userId, int page, int size) {
		log.debug("사용자 연습 샷 목록(날짜 포함, 페이징) 조회 시작 - userId: {}, page: {}, size: {}", userId, page, size);
		try {
			int offset = page * size;
			List<TrainingShotRow> shotRows = jdbcTemplate.query(
					SELECT_TRAINING_SHOTS_BY_USER,
					TRAINING_SHOT_ROW_MAPPER,
					userId,
					offset,
					size
			);
			List<TrainingShotInfoWithDate> shots = shotRows.stream()
					.map(this::convertToTrainingShotInfoWithDate)
					.toList();
			log.debug("사용자 연습 샷 목록(날짜 포함, 페이징) 조회 완료 - userId: {}, 샷 수: {}", userId, shots.size());
			return shots;
		} catch (EmptyResultDataAccessException e) {
			log.warn("연습 샷 정보 없음 - userId: {}", userId);
			return List.of();
		} catch (Exception e) {
			log.error("연습 샷 목록 조회 중 오류 발생 - userId: {}", userId, e);
			throw new RuntimeException("연습 샷 목록 조회 중 오류가 발생했습니다.", e);
		}
	}

	private record TrainingShotRow(
			Double directionAngle,
			Double launchAngle,
			Double ballSpeed,
			Double clubSpeed,
			Double distance,
			String videoName,
			LocalDateTime trainingDate
	) {
	}
}
