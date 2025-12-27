package com.daita.datn.models.dto.company;

import com.daita.datn.models.dto.JobDTO;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.Set;

@Getter @Setter @Builder
@NoArgsConstructor @AllArgsConstructor
public class CompanyUpdateDTO {
    private String companyName;
    private String location;
    private String website;
    private String introduction;
}
