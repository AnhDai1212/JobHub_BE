package com.daita.datn.models.dto.auth;

import lombok.*;
import lombok.experimental.FieldDefaults;
import java.io.Serializable;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class JwtInfo implements Serializable {
    String jwtID;
    Date issueTime;
    Date expiredTime;
}
