-- ===========================================
-- VERSION 1 - JOB PORTAL DATABASE INITIALIZATION
-- ===========================================
-- Author : Tu Anh Dai
-- Date   : 2025-10-19
-- Description:
--   - Module 1: AUTH (Accounts, Roles, Permissions)
--   - Module 2: JOB PORTAL (JobSeekers, Recruiters, Companies, Jobs, Applications, AI)
--   - Include BaseEntity audit fields: create_date, modified_date, create_by, modified_by
-- ===========================================

-- =====================
-- MODULE 1: AUTH (IDENTITY & ACCESS)
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

CREATE TABLE permissions (
                             permission_id INT AUTO_INCREMENT PRIMARY KEY,
                             permission_name VARCHAR(50) NOT NULL UNIQUE,
                             permission_description VARCHAR(100),
                             create_date DATETIME DEFAULT CURRENT_TIMESTAMP,
                             modified_date DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                             create_by VARCHAR(50),
                             modified_by VARCHAR(50)
);

CREATE TABLE account_roles (
                               account_id VARCHAR(36),
                               role_id INT,
                               PRIMARY KEY (account_id, role_id),
                               FOREIGN KEY (account_id) REFERENCES accounts(account_id) ON DELETE CASCADE,
                               FOREIGN KEY (role_id) REFERENCES roles(role_id) ON DELETE CASCADE
);

CREATE TABLE role_permissions (
                                  role_id INT,
                                  permission_id INT,
                                  PRIMARY KEY (role_id, permission_id),
                                  FOREIGN KEY (role_id) REFERENCES roles(role_id) ON DELETE CASCADE,
                                  FOREIGN KEY (permission_id) REFERENCES permissions(permission_id) ON DELETE CASCADE
);

-- =====================
-- MODULE 2: JOB PORTAL DOMAIN
-- =====================

CREATE TABLE companies (
                           company_id INT AUTO_INCREMENT PRIMARY KEY,
                           company_name VARCHAR(100) NOT NULL,
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
                            company_id INT,
                            position VARCHAR(100),
                            phone VARCHAR(20),
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
                                category_name VARCHAR(100) NOT NULL UNIQUE,
                                create_date DATETIME DEFAULT CURRENT_TIMESTAMP,
                                modified_date DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                                create_by VARCHAR(50),
                                modified_by VARCHAR(50)
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
                          tag_name VARCHAR(50) NOT NULL UNIQUE,
                          create_date DATETIME DEFAULT CURRENT_TIMESTAMP,
                          modified_date DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                          create_by VARCHAR(50),
                          modified_by VARCHAR(50)
);

CREATE TABLE job_tag_mapping (
                                 job_id INT,
                                 tag_id INT,
                                 PRIMARY KEY (job_id, tag_id),
                                 FOREIGN KEY (job_id) REFERENCES jobs(job_id) ON DELETE CASCADE,
                                 FOREIGN KEY (tag_id) REFERENCES job_tags(tag_id) ON DELETE CASCADE
);

CREATE TABLE applications (
                              application_id VARCHAR(36) PRIMARY KEY,
                              job_id INT NOT NULL,
                              job_seeker_id INT NOT NULL,
                              applied_at DATETIME DEFAULT CURRENT_TIMESTAMP,
                              status VARCHAR(50),
                              create_date DATETIME DEFAULT CURRENT_TIMESTAMP,
                              modified_date DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                              create_by VARCHAR(50),
                              modified_by VARCHAR(50),
                              FOREIGN KEY (job_id) REFERENCES jobs(job_id) ON DELETE CASCADE,
                              FOREIGN KEY (job_seeker_id) REFERENCES job_seekers(job_seeker_id) ON DELETE CASCADE
);

CREATE TABLE application_history (
                                     history_id INT AUTO_INCREMENT PRIMARY KEY,
                                     application_id VARCHAR(36) NOT NULL,
                                     status VARCHAR(50),
                                     updated_at DATETIME DEFAULT CURRENT_TIMESTAMP,
                                     create_date DATETIME DEFAULT CURRENT_TIMESTAMP,
                                     modified_date DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                                     create_by VARCHAR(50),
                                     modified_by VARCHAR(50),
                                     FOREIGN KEY (application_id) REFERENCES applications(application_id) ON DELETE CASCADE
);

CREATE TABLE favorites (
                           favorite_id INT AUTO_INCREMENT PRIMARY KEY,
                           job_seeker_id INT NOT NULL,
                           job_id INT NOT NULL,
                           create_date DATETIME DEFAULT CURRENT_TIMESTAMP,
                           modified_date DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                           create_by VARCHAR(50),
                           modified_by VARCHAR(50),
                           FOREIGN KEY (job_seeker_id) REFERENCES job_seekers(job_seeker_id) ON DELETE CASCADE,
                           FOREIGN KEY (job_id) REFERENCES jobs(job_id) ON DELETE CASCADE
);

CREATE TABLE candidate_skills (
                                  skill_id INT AUTO_INCREMENT PRIMARY KEY,
                                  job_seeker_id INT NOT NULL,
                                  skill_name VARCHAR(100),
                                  proficiency_level VARCHAR(100),
                                  create_date DATETIME DEFAULT CURRENT_TIMESTAMP,
                                  modified_date DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                                  create_by VARCHAR(50),
                                  modified_by VARCHAR(50),
                                  FOREIGN KEY (job_seeker_id) REFERENCES job_seekers(job_seeker_id) ON DELETE CASCADE
);

CREATE TABLE reviews (
                         review_id INT AUTO_INCREMENT PRIMARY KEY,
                         company_id INT NOT NULL,
                         account_id VARCHAR(36) NOT NULL,
                         rating INT CHECK (rating BETWEEN 1 AND 5),
                         review_text TEXT,
                         create_date DATETIME DEFAULT CURRENT_TIMESTAMP,
                         modified_date DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                         create_by VARCHAR(50),
                         modified_by VARCHAR(50),
                         FOREIGN KEY (company_id) REFERENCES companies(company_id) ON DELETE CASCADE,
                         FOREIGN KEY (account_id) REFERENCES accounts(account_id) ON DELETE CASCADE
);

CREATE TABLE notifications (
                               notification_id VARCHAR(36) PRIMARY KEY,
                               account_id VARCHAR(36) NOT NULL,
                               message TEXT,
                               is_read BOOLEAN DEFAULT FALSE,
                               create_date DATETIME DEFAULT CURRENT_TIMESTAMP,
                               modified_date DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                               create_by VARCHAR(50),
                               modified_by VARCHAR(50),
                               FOREIGN KEY (account_id) REFERENCES accounts(account_id) ON DELETE CASCADE
);

-- =====================
-- MODULE 3: AI FEATURES
-- =====================
CREATE TABLE job_recommendations (
                                     recommendation_id VARCHAR(36) PRIMARY KEY,
                                     job_seeker_id INT NOT NULL,
                                     job_id INT NOT NULL,
                                     score DECIMAL(5,4) NOT NULL,
                                     create_date DATETIME DEFAULT CURRENT_TIMESTAMP,
                                     modified_date DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                                     create_by VARCHAR(50),
                                     modified_by VARCHAR(50),
                                     FOREIGN KEY (job_seeker_id) REFERENCES job_seekers(job_seeker_id) ON DELETE CASCADE,
                                     FOREIGN KEY (job_id) REFERENCES jobs(job_id) ON DELETE CASCADE
);
CREATE INDEX idx_recommendation_score
    ON job_recommendations(job_seeker_id, score DESC);

CREATE TABLE parsed_cvs (
                            cv_id VARCHAR(36) PRIMARY KEY,
                            job_seeker_id INT NOT NULL,
                            file_url VARCHAR(255),
                            extracted_text LONGTEXT,
                            embedding JSON,
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
                         modified_date DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                         create_by VARCHAR(50),
                         modified_by VARCHAR(50),
                         FOREIGN KEY (account_id) REFERENCES accounts(account_id) ON DELETE CASCADE,
                         FOREIGN KEY (job_id) REFERENCES jobs(job_id) ON DELETE CASCADE
);

-- =====================
-- INDEXES
-- =====================
CREATE INDEX idx_account_email ON accounts(email);
CREATE INDEX idx_company_name ON companies(company_name);
CREATE INDEX idx_job_title ON jobs(title);
CREATE INDEX idx_application_status ON applications(status);
