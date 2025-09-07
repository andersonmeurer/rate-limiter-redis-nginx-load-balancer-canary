package com.meurer.rate.limiter.redis.nginx.canary.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class RateLimiterInterceptor implements HandlerInterceptor {

	private final RedisTemplate<String, String> redisTemplate;

	public static final int MAX_REQUESTS = 10;
	public static final int WINDOW_IN_SECONDS = 5;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		String ipAddress = request.getRemoteAddr();
		String key = "rate-limiter:" + ipAddress;

		Long count = redisTemplate.opsForValue().increment(key, 1);

		if (count == 1) {
			redisTemplate.expire(key, WINDOW_IN_SECONDS, TimeUnit.SECONDS);
		}

		if (count > MAX_REQUESTS) {
			response.setStatus(429); // Too Many Requests
			response.getWriter().write("You've reached your request limit. Please try again later.");
			return false;
		}

		return true;
	}
}