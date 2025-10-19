package com.daita.datn.models.dto;

import lombok.*;

@Getter @Setter @Builder
@NoArgsConstructor @AllArgsConstructor
public class SkillDTO {
    private Integer skillId;
    private String skillName;
    private String proficiencyLevel;
}
