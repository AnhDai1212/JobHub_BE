-- ===========================================
-- JOBHUB - INITIAL DATABASE SCHEMA (V1)
-- ===========================================

-- =====================
-- AUTH
-- =====================
CREATE TABLE accounts (
                          account_id VARCHAR(36) PRIMARY KEY,
                          email VARCHAR(100) NOT NULL UNIQUE,
                          password VARCHAR(255) NOT NULL,
                          last_login DATETIME,
                          status ENUM('ACTIVE', 'INACTIVE', 'LOCKED') DEFAULT 'ACTIVE',
                          create_date DATETIME DEFAULT CURRENT_TIMESTAMP,
                          modified_date DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                          create_by VARCHAR(50),
                          modified_by VARCHAR(50)
);

CREATE TABLE roles (
                       role_id INT AUTO_INCREMENT PRIMARY KEY,
                       role_name VARCHAR(50) NOT NULL UNIQUE,
                       role_description VARCHAR(100),
                       create_date DATETIME DEFAULT CURRENT_TIMESTAMP,
                       modified_date DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                       create_by VARCHAR(50),
                       modified_by VARCHAR(50)
);

CREATE TABLE account_roles (
                               account_id VARCHAR(36) NOT NULL,
                               role_id INT NOT NULL,
                               PRIMARY KEY (account_id, role_id),
                               FOREIGN KEY (account_id) REFERENCES accounts(account_id) ON DELETE CASCADE,
                               FOREIGN KEY (role_id) REFERENCES roles(role_id) ON DELETE CASCADE
);

-- =====================
-- JOB
-- =====================
CREATE TABLE companies (
                           company_id INT AUTO_INCREMENT PRIMARY KEY,
                           company_name VARCHAR(100) NOT NULL UNIQUE,
                           location VARCHAR(100),
                           website VARCHAR(255),
                           avatar_url VARCHAR(255),
                           is_approved BOOLEAN DEFAULT FALSE,
                           create_date DATETIME DEFAULT CURRENT_TIMESTAMP,
                           modified_date DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                           create_by VARCHAR(50),
                           modified_by VARCHAR(50)
);

CREATE TABLE recruiters (
                            recruiter_id INT AUTO_INCREMENT PRIMARY KEY,
                            account_id VARCHAR(36) NOT NULL UNIQUE,
                            company_id INT NOT NULL,
                            position VARCHAR(100),
                            phone VARCHAR(20),

                            status ENUM('PENDING','APPROVED','REJECTED') DEFAULT 'PENDING',

                            create_date DATETIME DEFAULT CURRENT_TIMESTAMP,
                            modified_date DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                            create_by VARCHAR(50),
                            modified_by VARCHAR(50),
                            FOREIGN KEY (account_id) REFERENCES accounts(account_id) ON DELETE CASCADE,
                            FOREIGN KEY (company_id) REFERENCES companies(company_id) ON DELETE CASCADE
);

CREATE TABLE job_seekers (
                             job_seeker_id INT AUTO_INCREMENT PRIMARY KEY,
                             account_id VARCHAR(36) NOT NULL UNIQUE,
                             full_name VARCHAR(100),
                             dob DATE,
                             phone VARCHAR(20),
                             address VARCHAR(255),
                             cv_url VARCHAR(255),
                             bio TEXT,
                             create_date DATETIME DEFAULT CURRENT_TIMESTAMP,
                             modified_date DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                             create_by VARCHAR(50),
                             modified_by VARCHAR(50),
                             FOREIGN KEY (account_id) REFERENCES accounts(account_id) ON DELETE CASCADE
);

-- =====================
-- JOBS & RELATED
-- =====================
CREATE TABLE jobs (
                      job_id INT AUTO_INCREMENT PRIMARY KEY,
                      company_id INT NOT NULL,
                      recruiter_id INT,
                      title VARCHAR(100) NOT NULL,
                      description TEXT,
                      location VARCHAR(100),
                      status ENUM('OPEN','CLOSED','DRAFT') DEFAULT 'OPEN',
                      min_salary DOUBLE,
                      max_salary DOUBLE,
                      job_type VARCHAR(50),
                      deadline DATE,
                      create_date DATETIME DEFAULT CURRENT_TIMESTAMP,
                      modified_date DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                      create_by VARCHAR(50),
                      modified_by VARCHAR(50),
                      FOREIGN KEY (company_id) REFERENCES companies(company_id) ON DELETE CASCADE,
                      FOREIGN KEY (recruiter_id) REFERENCES recruiters(recruiter_id) ON DELETE SET NULL
);

CREATE TABLE job_categories (
                                category_id INT AUTO_INCREMENT PRIMARY KEY,
                                category_name VARCHAR(100) NOT NULL UNIQUE
);

CREATE TABLE job_category_mapping (
                                      job_id INT,
                                      category_id INT,
                                      PRIMARY KEY (job_id, category_id),
                                      FOREIGN KEY (job_id) REFERENCES jobs(job_id) ON DELETE CASCADE,
                                      FOREIGN KEY (category_id) REFERENCES job_categories(category_id) ON DELETE CASCADE
);

CREATE TABLE job_tags (
                          tag_id INT AUTO_INCREMENT PRIMARY KEY,
                          tag_name VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE job_tag_mapping (
                                 job_id INT,
                                 tag_id INT,
                                 PRIMARY KEY (job_id, tag_id),
                                 FOREIGN KEY (job_id) REFERENCES jobs(job_id) ON DELETE CASCADE,
                                 FOREIGN KEY (tag_id) REFERENCES job_tags(tag_id) ON DELETE CASCADE
);

-- =====================
-- APPLICATIONS
-- =====================
CREATE TABLE applications (
                              application_id VARCHAR(36) PRIMARY KEY,
                              job_id INT NOT NULL,
                              job_seeker_id INT NOT NULL,
                              applied_at DATETIME DEFAULT CURRENT_TIMESTAMP,
                              status VARCHAR(50),
                              FOREIGN KEY (job_id) REFERENCES jobs(job_id) ON DELETE CASCADE,
                              FOREIGN KEY (job_seeker_id) REFERENCES job_seekers(job_seeker_id) ON DELETE CASCADE
);

CREATE TABLE application_history (
                                     history_id INT AUTO_INCREMENT PRIMARY KEY,
                                     application_id VARCHAR(36) NOT NULL,
                                     status VARCHAR(50),

                                     note VARCHAR(1000) NULL,

                                     updated_at DATETIME DEFAULT CURRENT_TIMESTAMP,
                                     FOREIGN KEY (application_id) REFERENCES applications(application_id) ON DELETE CASCADE
);

-- =====================
-- FAVORITES + SKILLS + NOTIFICATION
-- =====================
CREATE TABLE favorites (
                           favorite_id INT AUTO_INCREMENT PRIMARY KEY,
                           job_seeker_id INT NOT NULL,
                           job_id INT NOT NULL,
                           FOREIGN KEY (job_seeker_id) REFERENCES job_seekers(job_seeker_id) ON DELETE CASCADE,
                           FOREIGN KEY (job_id) REFERENCES jobs(job_id) ON DELETE CASCADE
);

CREATE TABLE candidate_skills (
                                  skill_id INT AUTO_INCREMENT PRIMARY KEY,
                                  job_seeker_id INT NOT NULL,

                                  skill_name VARCHAR(100) NOT NULL,
                                  proficiency_level ENUM('BEGINNER','INTERMEDIATE','ADVANCED','EXPERT') NULL,

                                  years_of_experience DECIMAL(4,1) NULL,     -- e.g. 1.5 years
                                  last_used_year YEAR NULL,                  -- last time used
                                  is_primary BOOLEAN DEFAULT FALSE,          -- skill chính
                                  certificate VARCHAR(255) NULL,             -- chứng chỉ (AWS SAA, TOEIC, …)
                                  description VARCHAR(500) NULL,             -- ghi chú ngắn (framework/stack liên quan)

                                  create_date DATETIME DEFAULT CURRENT_TIMESTAMP,
                                  modified_date DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

                                  FOREIGN KEY (job_seeker_id) REFERENCES job_seekers(job_seeker_id) ON DELETE CASCADE
);

CREATE TABLE notifications (
                               notification_id VARCHAR(36) PRIMARY KEY,
                               account_id VARCHAR(36) NOT NULL,
                               message TEXT,
                               is_read BOOLEAN DEFAULT FALSE,
                               create_date DATETIME DEFAULT CURRENT_TIMESTAMP,
                               FOREIGN KEY (account_id) REFERENCES accounts(account_id) ON DELETE CASCADE
);

-- =====================
-- AI
-- =====================
CREATE TABLE job_recommendations (
                                     recommendation_id VARCHAR(36) PRIMARY KEY,
                                     job_seeker_id INT NOT NULL,
                                     job_id INT NOT NULL,
                                     score DECIMAL(5,4) NOT NULL,
                                     FOREIGN KEY (job_seeker_id) REFERENCES job_seekers(job_seeker_id) ON DELETE CASCADE,
                                     FOREIGN KEY (job_id) REFERENCES jobs(job_id) ON DELETE CASCADE
);

CREATE TABLE parsed_cvs (
                            cv_id VARCHAR(36) PRIMARY KEY,
                            job_seeker_id INT NOT NULL,
                            file_url VARCHAR(255),
                            extracted_text LONGTEXT,
                            embedding JSON,
                            parsed_json JSON,
                            create_date DATETIME DEFAULT CURRENT_TIMESTAMP,
                            modified_date DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                            create_by VARCHAR(50),
                            modified_by VARCHAR(50),
                            FOREIGN KEY (job_seeker_id) REFERENCES job_seekers(job_seeker_id) ON DELETE CASCADE
);

CREATE TABLE ai_logs (
                         log_id VARCHAR(36) PRIMARY KEY,
                         account_id VARCHAR(36) NOT NULL,
                         job_id INT,
                         action VARCHAR(100),
                         explanation TEXT,
                         create_date DATETIME DEFAULT CURRENT_TIMESTAMP,
                         FOREIGN KEY (account_id) REFERENCES accounts(account_id) ON DELETE CASCADE,
                         FOREIGN KEY (job_id) REFERENCES jobs(job_id) ON DELETE CASCADE
);

-- =====================
-- RECRUITER DOCUMENTS / CONSULTATIONS
-- =====================
CREATE TABLE recruiter_documents (
                                     document_id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                     recruiter_id INT NOT NULL,
                                     file_url VARCHAR(500),
                                     file_name VARCHAR(255),
                                     content_type VARCHAR(100),
                                     create_date DATETIME DEFAULT CURRENT_TIMESTAMP,
                                     modified_date DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                                     FOREIGN KEY (recruiter_id) REFERENCES recruiters(recruiter_id) ON DELETE CASCADE
);

CREATE TABLE recruiter_consultations (
                                         consultation_id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                         recruiter_id INT NOT NULL,
                                         hiring_position VARCHAR(255) NOT NULL,
                                         industry VARCHAR(255),
                                         budget DOUBLE,
                                         currency VARCHAR(20),
                                         notes VARCHAR(1000),
                                         create_date DATETIME DEFAULT CURRENT_TIMESTAMP,
                                         modified_date DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                                         FOREIGN KEY (recruiter_id) REFERENCES recruiters(recruiter_id) ON DELETE CASCADE
);

-- =====================
-- INIT ROLES
-- =====================
INSERT INTO roles (role_name, role_description, create_by)
VALUES
    ('ADMIN', 'System administrator', 'SYSTEM'),
    ('RECRUITER', 'Recruiter user', 'SYSTEM'),
    ('JOB_SEEKER', 'Job seeker user', 'SYSTEM');
