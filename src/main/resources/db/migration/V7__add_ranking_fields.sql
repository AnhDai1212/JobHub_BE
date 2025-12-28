ALTER TABLE jobs
    ADD COLUMN parsed_jd_json JSON NULL;

ALTER TABLE applications
    ADD COLUMN matching_score DECIMAL(6,4) NULL;
