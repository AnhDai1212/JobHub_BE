package com.daita.datn.controllers;

import com.daita.datn.common.base.ApiResponse;
import com.daita.datn.common.constants.MessageConstant;
import com.daita.datn.models.dto.BaseSearchDTO;
import com.daita.datn.models.dto.FavoriteDTO;
import com.daita.datn.models.dto.pagination.PageListDTO;
import com.daita.datn.services.FavoriteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/favorites")
@RequiredArgsConstructor
public class FavoriteController {

    private final FavoriteService favoriteService;

    @PostMapping("/{jobId}")
    @PreAuthorize("hasRole('JOB_SEEKER')")
    public ApiResponse<FavoriteDTO> addFavorite(
            @PathVariable Integer jobId
    ) {
        FavoriteDTO dto = favoriteService.addFavorite(jobId);
        return ApiResponse.<FavoriteDTO>builder()
                .code(HttpStatus.CREATED.value())
                .status(HttpStatus.CREATED.getReasonPhrase())
                .message(MessageConstant.FAVORITE_ADD_SUCCESS)
                .data(dto)
                .build();
    }

    @DeleteMapping("/{jobId}")
    @PreAuthorize("hasRole('JOB_SEEKER')")
    public ApiResponse<Void> removeFavorite(
            @PathVariable Integer jobId
    ) {
        favoriteService.removeFavorite(jobId);
        return ApiResponse.<Void>builder()
                .code(HttpStatus.OK.value())
                .status(HttpStatus.OK.getReasonPhrase())
                .message(MessageConstant.FAVORITE_REMOVE_SUCCESS)
                .build();
    }

    @PostMapping("/search")
    @PreAuthorize("hasRole('JOB_SEEKER')")
    public ApiResponse<PageListDTO<FavoriteDTO>> listFavorites(
            @RequestBody BaseSearchDTO<FavoriteDTO> request
    ) {
        PageListDTO<FavoriteDTO> list = favoriteService.listFavorites(request);
        return ApiResponse.<PageListDTO<FavoriteDTO>>builder()
                .code(HttpStatus.OK.value())
                .status(HttpStatus.OK.getReasonPhrase())
                .message(MessageConstant.FAVORITE_LIST_SUCCESS)
                .data(list)
                .build();
    }
}
