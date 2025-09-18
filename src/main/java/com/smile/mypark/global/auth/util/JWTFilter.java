package com.smile.mypark.global.auth.util;

import static com.smile.mypark.global.auth.util.CookieUtil.*;

import java.io.IOException;
import java.util.Map;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import com.smile.mypark.domain.user.dto.request.UserDTO;
import com.smile.mypark.domain.user.entity.User;
import com.smile.mypark.domain.user.repository.UserRepository;
import com.smile.mypark.global.auth.dto.CustomOAuth2User;
import com.smile.mypark.global.constants.Constants;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class JWTFilter extends OncePerRequestFilter {

	private final JWTUtil jwtUtil;
	private final UserRepository userRepository;

	private static final AntPathMatcher pathMatcher = new AntPathMatcher();

	@Override
	protected boolean shouldNotFilter(HttpServletRequest request) {
		String uri = request.getRequestURI();
		return Constants.NO_NEED_FILTER_URLS.stream()
			.anyMatch(pattern -> pathMatcher.match(pattern, uri));
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
		FilterChain filterChain) throws ServletException, IOException {

		Map<String, String> tokens = extractTokensFromCookie(request);

		if (tokens.isEmpty() || tokens.get("refreshToken") == null) {
			filterChain.doFilter(request, response);
			return;
		}

		if (tokens.get("accessToken") == null || jwtUtil.isExpired(tokens.get("accessToken"))) {
			filterChain.doFilter(request, response);
			return;
		}

		authenticateUser(tokens.get("accessToken"), response);
		filterChain.doFilter(request, response);
	}

	private void authenticateUser(String token, HttpServletResponse response) {
		String providerId = jwtUtil.getProviderId(token);

		User user = userRepository.findByuIdx(Long.valueOf(providerId)).orElse(null);

		if (user == null) {
			return;
		}

		UserDTO userDTO = UserDTO.builder()
			.id(user.getIdx())
			.nickname(user.getNickname())
			.uId(user.getUId())
			.providerId(providerId)
			.build();

		CustomOAuth2User customUser = new CustomOAuth2User(userDTO, false);
		Authentication auth = new UsernamePasswordAuthenticationToken(customUser, null, customUser.getAuthorities());
		SecurityContextHolder.getContext().setAuthentication(auth);
	}
}