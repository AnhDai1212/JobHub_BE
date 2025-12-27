package com.daita.datn.services;

import com.daita.datn.models.dto.BaseSearchDTO;
import com.daita.datn.models.dto.FavoriteDTO;
import com.daita.datn.models.dto.pagination.PageListDTO;

public interface FavoriteService {
    FavoriteDTO addFavorite(Integer jobId);

    void removeFavorite(Integer jobId);

    PageListDTO<FavoriteDTO> listFavorites(BaseSearchDTO<FavoriteDTO> request);
}
