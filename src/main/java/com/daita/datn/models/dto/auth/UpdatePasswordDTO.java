package com.daita.datn.models.dto.auth;


import com.daita.datn.common.constants.Constant;
import com.daita.datn.common.constants.MessageConstant;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.processing.Pattern;

@Data
@RequiredArgsConstructor
public class UpdatePasswordDTO {
//    @Pattern(regexp = Constant.PASSWORD_REGEX, message = MessageConstant.INVALID_PASSWORD)
    private String newPassword;
    private String email;
}