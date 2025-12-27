package com.daita.datn.models.dto;

import com.daita.datn.common.constants.MessageConstant;
import com.daita.datn.models.dto.pagination.PaginationDTO;
import com.daita.datn.models.dto.pagination.SortDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BaseSearchDTO<T> {
    @Valid
    private PaginationDTO pagination;
    private List<SortDTO> sortedBy;
    @Size(max = 100, message = MessageConstant.KEY_WORD_TOO_LONG)
    private String searchedBy;
    private T filter;
}