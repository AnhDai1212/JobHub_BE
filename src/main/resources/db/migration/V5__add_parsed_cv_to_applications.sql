ALTER TABLE applications
    ADD COLUMN parsed_cv_id VARCHAR(36) NULL;

ALTER TABLE applications
    ADD CONSTRAINT fk_applications_parsed_cv
        FOREIGN KEY (parsed_cv_id) REFERENCES parsed_cvs (cv_id)
        ON DELETE SET NULL;
