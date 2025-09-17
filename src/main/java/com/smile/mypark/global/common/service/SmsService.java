package com.smile.mypark.global.common.service;

import java.time.Duration;
import java.util.concurrent.ThreadLocalRandom;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import com.smile.mypark.global.apipayload.code.status.ErrorStatus;
import com.smile.mypark.global.apipayload.exception.GeneralException;
import com.smile.mypark.global.common.dto.SmsRequestDTO;
import com.smile.mypark.global.common.dto.SmsVerifyRequestDTO;
import com.smile.mypark.global.util.SmsCertificationUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class SmsService {

	private static final int CODE_LEN = 4;
	private static final Duration TTL = Duration.ofMinutes(3);
	private static final String PREFIX = "SMS:CERT:";

	private final SmsCertificationUtil smsCertificationUtil;
	private final StringRedisTemplate redisTemplate;

	public void sendCertificationCode(SmsRequestDTO request) {
		String code = generate();
		String phoneNumber = normalizePhoneNumber(request.getPhoneNumber());
		String key = PREFIX + phoneNumber;

		if (Boolean.TRUE.equals(redisTemplate.hasKey(key))) {
			throw new GeneralException(ErrorStatus._SMS_ALREADY_SEND);
		}

		redisTemplate.opsForValue().set(key, code, TTL);

		try {
			smsCertificationUtil.sendSMS(request.getPhoneNumber(), code);
			log.info("[SMS] 전송 성공: 전화번호={}, 코드={}", phoneNumber, code);

		} catch (Exception e) {
			redisTemplate.delete(key);
			log.error("[SMS] 전송 실패", e);

			throw new GeneralException(ErrorStatus._SMS_SEND_FAIL);
		}
	}

	public void verifyCertificationCode(SmsVerifyRequestDTO request) {
		String phoneNumber = normalizePhoneNumber(request.getPhoneNumber());
		String key = PREFIX + phoneNumber;
		String expectedCode = redisTemplate.opsForValue().get(key);

		if (expectedCode == null) {
			throw new GeneralException(ErrorStatus._SMS_CERTIFICATION_EXPIRED);
		}

		if (!expectedCode.trim().equals(request.getCertificationCode().trim())) {
			throw new GeneralException(ErrorStatus._SMS_CERTIFICATION_MISMATCH);
		}

		redisTemplate.delete(key);
		log.info("[SMS] 인증 완료: 전화번호 {} ", phoneNumber);
	}

	private String generate() {
		int bound = (int)Math.pow(10, CODE_LEN);
		return String.format("%0" + CODE_LEN + "d",
			ThreadLocalRandom.current().nextInt(bound));
	}

	private String normalizePhoneNumber(String phoneNumber) {
		return phoneNumber.replaceAll("-", "");
	}
}