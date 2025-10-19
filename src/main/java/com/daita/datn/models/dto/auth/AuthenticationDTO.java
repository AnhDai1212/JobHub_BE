package com.daita.datn.models.dto.auth;

import com.daita.datn.models.dto.AccountDTO;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
@Builder
public class AuthenticationDTO {
    String accessToken;
    String refreshToken;
    AccountDTO account;
}
