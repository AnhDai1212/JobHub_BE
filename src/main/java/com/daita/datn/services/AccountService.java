package com.daita.datn.services;

import com.daita.datn.enums.RoleType;
import com.daita.datn.models.dto.UpgradeRecruiterDTO;
import com.daita.datn.models.dto.auth.*;
import com.daita.datn.models.entities.auth.Account;

public interface AccountService {
    void register(RegisterRequestDTO requestDTO);
    void verifyRegistrationByOtp(OtpVerificationDTO otpVerificationDTO);
    void forgotPassword(ForgotPasswordDTO dto);
    void verifyOtp(OtpVerificationDTO dto);
    void updatePassword(UpdatePasswordDTO updatePasswordDTO);
    Account findByEmail(String email);
    boolean existsByEmail(String email);
    boolean existsByRole(RoleType roleType);
    void save(Account account);
    void upgradeToRecruiter(UpgradeRecruiterDTO dto);
    Account getCurrentAccount();

}
