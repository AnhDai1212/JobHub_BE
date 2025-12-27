package com.daita.datn.common.utils;

import com.daita.datn.enums.ErrorCode;
import com.daita.datn.exceptions.AppException;
import com.daita.datn.models.dto.pagination.PaginationDTO;
import com.daita.datn.models.dto.pagination.SortDTO;
import com.daita.datn.models.entities.Company;
import org.springframework.web.multipart.MultipartFile;
import jakarta.persistence.criteria.*;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class Util {
    private static final String NUMBERS = "0123456789";
    private static final SecureRandom SECURE_RANDOM = new SecureRandom();

    public static String randomNumbers(int length){
        return SECURE_RANDOM.ints(length,0,NUMBERS.length())
                .mapToObj(NUMBERS::charAt)
                .map(Object::toString)
                .collect(Collectors.joining());
    }

    public static Pageable toPageable(List<SortDTO> sortModels, PaginationDTO pagination, Set<String> sortFields) {
        List<Sort.Order> orders = new ArrayList<>();
        if (sortModels != null) {
            for (SortDTO sort : sortModels) {
                if (!sortFields.contains(sort.getField())) {
                    throw new AppException(ErrorCode.VALIDATION_ERROR, "Invalid sort field: " + sort.getField());
                }
                Sort.Direction dir = Sort.Direction.fromString(sort.getSort().name());
                orders.add(new Sort.Order(dir, sort.getField()));
            }
        }
        return PageRequest.of(pagination.getPage(), pagination.getPageSize(), Sort.by(orders));
    }

    public static String buildSearchKeyword(String input) {
        if (input == null || input.isBlank()) return null;
        input = input.trim();
        return "%" + input.toLowerCase()
                .replace("\\", "\\\\")
                .replace("%", "\\%")
                .replace("_", "\\_") + "%";
    }

    public static <T> Specification<T> buildSearchSpec(
            String keyword,
            Set<String> searchableFields,
            Set<String> fetchRelations,
            String isActiveField
    ) {
        return (root, query, cb) -> {

            if (fetchRelations != null && !fetchRelations.isEmpty()) {
                for (String relation : fetchRelations) {
                    FetchParent<?, ?> fetch = root;
                    for (String part : relation.split("\\.")) {
                        fetch = fetch.fetch(part, JoinType.LEFT);
                    }
                }
                query.distinct(true);
            }

            List<Predicate> predicates = new ArrayList<>();

            if (keyword != null && !keyword.isBlank()) {
                List<Predicate> searchPredicates = new ArrayList<>();
                for (String field : searchableFields) {
                    Path<?> path = root;
                    for (String part : field.split("\\.")) {
                        path = path.get(part);
                    }
                    Expression<String> expression =
                            cb.lower(path.as(String.class));

                    searchPredicates.add(
                            cb.like(expression, keyword, '\\')
                    );
                }
                predicates.add(
                        cb.or(searchPredicates.toArray(new Predicate[0]))
                );
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }


    public static String getImageIdFromUrl(String url){
        if (url == null || url.isEmpty()) {
            return null;
        }
        String[] parts = url.split("/");
        return parts[parts.length - 1].split("\\.")[0];
    }

    public static void validateFile(
            MultipartFile file,
            Set<String> allowedContentTypes,
            long maxSizeBytes
    ) {
        if (file == null || file.isEmpty()) {
            throw new AppException(ErrorCode.VALIDATION_ERROR, "File is empty");
        }

        if (file.getSize() > maxSizeBytes) {
            throw new AppException(ErrorCode.IMAGE_SIZE_EXCEEDED, "File too large");
        }

        String contentType = file.getContentType();
        if (contentType == null || !allowedContentTypes.contains(contentType)) {
            throw new AppException(ErrorCode.INVALID_IMAGE_FORMAT, "Unsupported file type");
        }
    }
}
