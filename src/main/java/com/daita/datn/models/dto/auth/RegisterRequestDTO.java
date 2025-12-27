package com.daita.datn.models.dto.auth;

import com.daita.datn.common.constants.Constant;
import com.daita.datn.common.constants.MessageConstant;
import lombok.*;
import jakarta.validation.constraints.Pattern;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequestDTO {
    @Pattern(regexp = Constant.EMAIL_REGEX, message = MessageConstant.INVALID_EMAIL)
    private String email;
    @Pattern(regexp = Constant.PASSWORD_REGEX, message = MessageConstant.INVALID_PASSWORD)
    private String password;
}
