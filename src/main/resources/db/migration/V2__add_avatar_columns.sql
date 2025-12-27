-- Add avatar columns for recruiter and job seeker profiles

ALTER TABLE recruiters
    ADD COLUMN avatar_url VARCHAR(255) AFTER phone;

ALTER TABLE job_seekers
    ADD COLUMN avatar_url VARCHAR(255) AFTER cv_url;
