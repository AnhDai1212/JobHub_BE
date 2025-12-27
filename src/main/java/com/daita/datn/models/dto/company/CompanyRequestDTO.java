package com.daita.datn.models.dto.company;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter @Setter @Builder
@NoArgsConstructor @AllArgsConstructor
public class CompanyRequestDTO {

    @NotBlank
    @Size(max = 100)
    private String companyName;

    @Size(max = 100)
    private String location;

    @Size(max = 255)
    private String website;

    private String introduction;
}
