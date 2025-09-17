package com.smile.mypark.global.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import net.nurigo.sdk.NurigoApp;
import net.nurigo.sdk.message.model.Message;
import net.nurigo.sdk.message.request.SingleMessageSendingRequest;
import net.nurigo.sdk.message.service.DefaultMessageService;

import jakarta.annotation.PostConstruct;

@Component
public class SmsCertificationUtil {

	@Value("${coolsms.api-key}")
	private String apiKey;

	@Value("${coolsms.api-secret}")
	private String apiSecret;

	@Value("${coolsms.from-number}")
	private String fromNumber;

	private DefaultMessageService messageService;

	@PostConstruct
	public void init() {
		this.messageService = NurigoApp.INSTANCE.initialize(apiKey, apiSecret, "https://api.coolsms.co.kr");
	}

	public void sendSMS(String to, String certificationCode) {
		try {
			Message message = new Message();
			message.setFrom(fromNumber);
			message.setTo(to);
			message.setText("[마이파크] 본인확인 인증번호는 " + certificationCode + "입니다.");

			this.messageService.sendOne(new SingleMessageSendingRequest(message));
		} catch (Exception e) {
			throw new RuntimeException("SMS 전송 중 오류가 발생했습니다." + e.getMessage());
		}
	}
}