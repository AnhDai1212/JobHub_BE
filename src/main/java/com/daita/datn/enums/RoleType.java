package com.daita.datn.enums;

public enum RoleType {
    ADMIN("admin"),
    RECRUITER("recruiter"),
    JOB_SEEKER("job_seeker");

    private final String value;

    RoleType(String value) {
        this.value = value;
    }
}