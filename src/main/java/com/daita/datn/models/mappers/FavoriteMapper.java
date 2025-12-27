package com.daita.datn.models.mappers;

import com.daita.datn.models.dto.FavoriteDTO;
import com.daita.datn.models.entities.Favorite;
import com.daita.datn.models.entities.Job;
import com.daita.datn.models.entities.JobSeeker;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface FavoriteMapper {

    @Mapping(source = "job.jobId", target = "jobId")
    @Mapping(source = "jobSeeker.jobSeekerId", target = "jobSeekerId")
    @Mapping(target = "createdAt", ignore = true)
    FavoriteDTO toDto(Favorite favorite);

    @Mapping(target = "favoriteId", ignore = true)
    @Mapping(target = "jobSeeker", source = "jobSeeker")
    @Mapping(target = "job", source = "job")
    Favorite toEntity(JobSeeker jobSeeker, Job job);
}
