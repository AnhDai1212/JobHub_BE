-- Rollback avatar columns

ALTER TABLE recruiters
    DROP COLUMN IF EXISTS avatar_url;

ALTER TABLE job_seekers
    DROP COLUMN IF EXISTS avatar_url;
