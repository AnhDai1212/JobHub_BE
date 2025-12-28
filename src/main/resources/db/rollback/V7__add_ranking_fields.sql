ALTER TABLE jobs
    DROP COLUMN parsed_jd_json;

ALTER TABLE applications
    DROP COLUMN matching_score;
