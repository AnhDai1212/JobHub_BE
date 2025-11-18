package com.daita.datn.services.implement;

import com.daita.datn.common.utils.Util;
import com.daita.datn.enums.ErrorCode;
import com.daita.datn.exceptions.AppException;
import com.daita.datn.services.OtpService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Slf4j
@Service
@RequiredArgsConstructor
public class OtpServiceImpl implements OtpService {

    private static final int MAX_DAILY_SEND_REQUESTS = 5;
    private static final Duration OTP_TTL = Duration.ofMinutes(5);
    private static final Duration LIMIT_TTL = Duration.ofDays(1);
    private final RedisTemplate<String, String> redisTemplate;

    private String otpKey(String email) {
        return "otp:" + email;
    }

    private String limitKey(String email) {
        return "otp:limit:" + email;
    }

    @Override
    public String generateOtp(String email) {
        try {
            String otp = Util.randomNumbers(6);
            redisTemplate.opsForValue().set(otpKey(email), otp, OTP_TTL);
            return otp;
        } catch (Exception e) {
            throw new AppException(ErrorCode.OTP_SEND_FAILED);
        }
    }

    @Override
    public boolean isOtpLimitExceeded(String email) {
        String countStr = redisTemplate.opsForValue().get(limitKey(email));
        int count = (countStr == null) ? 0 : Integer.parseInt(countStr);
        return count >= MAX_DAILY_SEND_REQUESTS;
    }

    @Override
    public void increaseRequestCount(String email) {

        String key = limitKey(email);
        String countStr = redisTemplate.opsForValue().get(key);
        int count = (countStr == null) ? 0 : Integer.parseInt(countStr);
        redisTemplate.opsForValue().set(key, String.valueOf(count + 1), LIMIT_TTL);
    }

    @Override
    public boolean deleteOtp(String email) {
        try {
            redisTemplate.delete(otpKey(email));
            return true;
        } catch (Exception e) {
            log.error("Failed to delete OTP for '{}': {}", email, e.getMessage(), e);
            return false;
        }
    }

    @Override
    public String getOtp(String email) {
        return redisTemplate.opsForValue().get(otpKey(email));
    }

    @Override
    public boolean verifyOtp(String inputOtp, String cachedOtp) {
        return cachedOtp != null && cachedOtp.equals(inputOtp);
    }
}