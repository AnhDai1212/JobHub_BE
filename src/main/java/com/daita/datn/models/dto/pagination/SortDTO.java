package com.daita.datn.models.dto.pagination;

import com.daita.datn.enums.SortDirection;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SortDTO {
    private String field;
    private SortDirection sort;
}