package com.daita.datn.models.dto.pagination;

import com.daita.datn.common.constants.MessageConstant;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaginationDTO {
    @Min(value = 0, message = MessageConstant.PAGE_MIN)
    private int page;

    @Max(value = 100, message = MessageConstant.PAGE_SIZE_MAX)
    private int pageSize;
}