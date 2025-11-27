package com.smile.mypark.global.auth.controller;

import com.smile.mypark.global.annotation.AuthUser;
import com.smile.mypark.global.apipayload.ApiResponse;
import com.smile.mypark.global.auth.service.CustomOAuth2UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(name = "[ 인증 ]")
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final CustomOAuth2UserService customOAuth2UserService;

    @PostMapping("/reissue")
    public ApiResponse<?> reissue(@AuthUser Long userId) {
        return ApiResponse.onSuccess(customOAuth2UserService.reissue(userId));
    }
}
