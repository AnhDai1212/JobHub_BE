package com.daita.datn.services.implement;

import com.daita.datn.common.constants.Constant;
import com.daita.datn.common.utils.Util;
import com.daita.datn.enums.ErrorCode;
import com.daita.datn.exceptions.AppException;
import com.daita.datn.models.dto.BaseSearchDTO;
import com.daita.datn.models.dto.FavoriteDTO;
import com.daita.datn.models.dto.pagination.PageListDTO;
import com.daita.datn.models.entities.Favorite;
import com.daita.datn.models.entities.Job;
import com.daita.datn.models.entities.JobSeeker;
import com.daita.datn.models.entities.auth.Account;
import com.daita.datn.models.mappers.FavoriteMapper;
import com.daita.datn.repositories.FavoriteRepository;
import com.daita.datn.repositories.JobRepository;
import com.daita.datn.repositories.JobSeekerRepository;
import com.daita.datn.services.AccountService;
import com.daita.datn.services.FavoriteService;
import java.util.List;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class FavoriteServiceImpl implements FavoriteService {

    AccountService accountService;
    JobSeekerRepository jobSeekerRepository;
    JobRepository jobRepository;
    FavoriteRepository favoriteRepository;
    FavoriteMapper favoriteMapper;

    @Override
    @Transactional
    public FavoriteDTO addFavorite(Integer jobId) {
        Account account = accountService.getCurrentAccount();

        JobSeeker jobSeeker = jobSeekerRepository.findByAccount_AccountId(account.getAccountId())
                .orElseThrow(() -> new AppException(ErrorCode.RESOURCE_NOT_FOUND, "JobSeeker"));

        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new AppException(ErrorCode.RESOURCE_NOT_FOUND, "Job"));

        if (favoriteRepository.existsByJobSeeker_JobSeekerIdAndJob_JobId(
                jobSeeker.getJobSeekerId(), job.getJobId())) {
            throw new AppException(ErrorCode.RESOURCE_ALREADY_EXISTS, "Favorite");
        }

        Favorite favorite = favoriteMapper.toEntity(jobSeeker, job);
        Favorite saved = favoriteRepository.save(favorite);
        return favoriteMapper.toDto(saved);
    }

    @Override
    @Transactional
    public void removeFavorite(Integer jobId) {
        Account account = accountService.getCurrentAccount();

        JobSeeker jobSeeker = jobSeekerRepository.findByAccount_AccountId(account.getAccountId())
                .orElseThrow(() -> new AppException(ErrorCode.RESOURCE_NOT_FOUND, "JobSeeker"));

        Favorite favorite = favoriteRepository.findByJobSeeker_JobSeekerIdAndJob_JobId(
                        jobSeeker.getJobSeekerId(),
                        jobId
                )
                .orElseThrow(() -> new AppException(ErrorCode.RESOURCE_NOT_FOUND, "Favorite"));

        favoriteRepository.delete(favorite);
    }

    @Override
    @Transactional(readOnly = true)
    public PageListDTO<FavoriteDTO> listFavorites(BaseSearchDTO<FavoriteDTO> request) {
        Account account = accountService.getCurrentAccount();

        JobSeeker jobSeeker = jobSeekerRepository.findByAccount_AccountId(account.getAccountId())
                .orElseThrow(() -> new AppException(ErrorCode.RESOURCE_NOT_FOUND, "JobSeeker"));

        Pageable pageable = Util.toPageable(
                request.getSortedBy(),
                request.getPagination(),
                Constant.FAVORITE_SORT_FIELDS
        );

        Page<Favorite> page = favoriteRepository.findAllByJobSeeker_JobSeekerId(
                jobSeeker.getJobSeekerId(),
                pageable
        );

        List<FavoriteDTO> rows = page.getContent()
                .stream()
                .map(favoriteMapper::toDto)
                .toList();

        return new PageListDTO<>(rows, (int) page.getTotalElements());
    }
}
