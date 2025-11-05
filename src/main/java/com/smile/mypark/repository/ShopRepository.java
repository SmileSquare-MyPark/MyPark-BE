package com.smile.mypark.repository;

import com.smile.mypark.dto.response.ShopResponseDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Slf4j
@Repository
@RequiredArgsConstructor
public class ShopRepository {

	private final JdbcTemplate jdbcTemplate;

	private static final RowMapper<ShopRow> SHOP_ROW_MAPPER = (rs, rowNum) -> new ShopRow(
			rs.getString("SHOP_CODE"),
			rs.getString("s_name"),
			rs.getString("s_addr1"),
			rs.getString("s_addr2"),
			rs.getString("s_tel"),
			rs.getInt("device_count"),
			rs.getObject("shop_rating") != null ? rs.getDouble("shop_rating") : null,
			rs.getInt("is_liked") == 1
	);

	private static final String SELECT_ALL_SHOPS = """
			SELECT
				s.SHOP_CODE,
				s.s_name,
				s.s_addr1,
				s.s_addr2,
				s.s_tel,
				s.shop_rating,
				ISNULL(COUNT(sim.idx), 0) AS device_count,
				CASE WHEN usl.user_id IS NOT NULL THEN 1 ELSE 0 END AS is_liked
			FROM TB_SHOP s
			LEFT JOIN TB_SIMULATOR sim ON s.SHOP_CODE = sim.SHOP_CODE
			LEFT JOIN TB_USER_SHOP_LIKE usl ON s.SHOP_CODE = usl.shop_id AND usl.user_id = ?
			GROUP BY s.SHOP_CODE, s.s_name, s.s_addr1, s.s_addr2, s.s_tel, s.shop_rating, usl.user_id
			ORDER BY s.s_name
			""";

	private static final String SELECT_SHOPS_BY_NAME = """
			SELECT
				s.SHOP_CODE,
				s.s_name,
				s.s_addr1,
				s.s_addr2,
				s.s_tel,
				s.shop_rating,
				ISNULL(COUNT(sim.idx), 0) AS device_count,
				CASE WHEN usl.user_id IS NOT NULL THEN 1 ELSE 0 END AS is_liked
			FROM TB_SHOP s
			LEFT JOIN TB_SIMULATOR sim ON s.SHOP_CODE = sim.SHOP_CODE
			LEFT JOIN TB_USER_SHOP_LIKE usl ON s.SHOP_CODE = usl.shop_id AND usl.user_id = ?
			WHERE s.s_name LIKE ?
			GROUP BY s.SHOP_CODE, s.s_name, s.s_addr1, s.s_addr2, s.s_tel, s.shop_rating, usl.user_id
			ORDER BY s.s_name
			""";

	/**
	 * 전체 매장 목록을 조회
	 *
	 * @param userId 사용자 ID
	 * @return 매장 정보 리스트
	 */
	public List<ShopResponseDTO.ShopInfo> findAllShops(Long userId) {
		log.debug("전체 매장 목록 조회 시작 - userId: {}", userId);
		try {
			List<ShopRow> shopRows = jdbcTemplate.query(SELECT_ALL_SHOPS, SHOP_ROW_MAPPER, userId);
			List<ShopResponseDTO.ShopInfo> shops = shopRows.stream()
					.map(this::convertToShopInfo)
					.toList();
			log.debug("전체 매장 목록 조회 완료 - 매장 수: {}", shops.size());
			return shops;
		} catch (EmptyResultDataAccessException e) {
			log.warn("매장 정보 없음");
			return List.of();
		} catch (Exception e) {
			log.error("매장 목록 조회 중 오류 발생", e);
			throw new RuntimeException("매장 목록 조회 중 오류가 발생했습니다.", e);
		}
	}

	/**
	 * 이름으로 매장을 검색
	 *
	 * @param userId 사용자 ID
	 * @param name 검색할 매장 이름 키워드
	 * @return 매장 정보 리스트
	 */
	public List<ShopResponseDTO.ShopInfo> findShopsByName(Long userId, String name) {
		log.debug("이름으로 매장 검색 시작 - userId: {}, name: {}", userId, name);
		try {
			String searchPattern = "%" + name + "%";
			List<ShopRow> shopRows = jdbcTemplate.query(
					SELECT_SHOPS_BY_NAME,
					SHOP_ROW_MAPPER,
					userId,
					searchPattern
			);
			List<ShopResponseDTO.ShopInfo> shops = shopRows.stream()
					.map(this::convertToShopInfo)
					.toList();
			log.debug("이름으로 매장 검색 완료 - 매장 수: {}", shops.size());
			return shops;
		} catch (EmptyResultDataAccessException e) {
			log.warn("이름으로 매장 검색 결과 없음 - name: {}", name);
			return List.of();
		} catch (Exception e) {
			log.error("이름으로 매장 검색 중 오류 발생 - name: {}", name, e);
			throw new RuntimeException("매장 검색 중 오류가 발생했습니다.", e);
		}
	}

	/**
	 * 매장을 찜하기
	 *
	 * @param userId 사용자 ID
	 * @param shopCode 매장 코드
	 * @return 삽입된 행의 수
	 */
	public int likeShop(Long userId, String shopCode) {
		log.debug("매장 찜하기 시작 - userId: {}, shopCode: {}", userId, shopCode);
		try {
			String sql = "INSERT INTO TB_USER_SHOP_LIKE (user_id, shop_id) VALUES (?, ?)";
			int result = jdbcTemplate.update(sql, userId, shopCode);
			log.debug("매장 찜하기 완료 - userId: {}, shopCode: {}", userId, shopCode);
			return result;
		} catch (Exception e) {
			log.error("매장 찜하기 중 오류 발생 - userId: {}, shopCode: {}", userId, shopCode, e);
			throw new RuntimeException("매장 찜하기 중 오류가 발생했습니다.", e);
		}
	}

	/**
	 * 매장 찜 취소
	 *
	 * @param userId 사용자 ID
	 * @param shopCode 매장 코드
	 * @return 삭제된 행의 수
	 */
	public int unlikeShop(Long userId, String shopCode) {
		log.debug("매장 찜 취소 시작 - userId: {}, shopCode: {}", userId, shopCode);
		try {
			String sql = "DELETE FROM TB_USER_SHOP_LIKE WHERE user_id = ? AND shop_id = ?";
			int result = jdbcTemplate.update(sql, userId, shopCode);
			log.debug("매장 찜 취소 완료 - userId: {}, shopCode: {}", userId, shopCode);
			return result;
		} catch (Exception e) {
			log.error("매장 찜 취소 중 오류 발생 - userId: {}, shopCode: {}", userId, shopCode, e);
			throw new RuntimeException("매장 찜 취소 중 오류가 발생했습니다.", e);
		}
	}

	/**
	 * ShopRow를 ShopInfo로 변환
	 *
	 * @param shopRow 매장 row 데이터
	 * @return 매장 정보 DTO
	 */
	private ShopResponseDTO.ShopInfo convertToShopInfo(ShopRow shopRow) {
		return ShopResponseDTO.ShopInfo.builder()
				.shopCode(shopRow.shopCode())
				.shopName(shopRow.shopName())
				.address1(shopRow.address1())
				.address2(shopRow.address2())
				.telephone(shopRow.telephone())
				.simulationCnt(shopRow.simulationCnt())
				.shopRating(shopRow.shopRating())
				.isLiked(shopRow.isLiked())
				.build();
	}

	private record ShopRow(
			String shopCode,
			String shopName,
			String address1,
			String address2,
			String telephone,
			Integer simulationCnt,
			Double shopRating,
			Boolean isLiked
	) {
	}
}
