package com.daita.datn.services;

public interface OtpService {

    String generateOtp(String email);
    boolean isOtpLimitExceeded(String email);
    void increaseRequestCount(String email);
    boolean deleteOtp(String email);
    String getOtp(String email);
    boolean verifyOtp(String inputOtp, String cachedOtp);
}
