CREATE TABLE job_requirements (
    requirement_id INT AUTO_INCREMENT PRIMARY KEY,
    job_id INT NOT NULL,
    requirement_text TEXT NOT NULL,
    display_order INT NOT NULL,
    CONSTRAINT job_requirements_job_fk FOREIGN KEY (job_id)
        REFERENCES jobs (job_id) ON DELETE CASCADE
);

CREATE INDEX idx_job_requirements_job_id ON job_requirements(job_id);
