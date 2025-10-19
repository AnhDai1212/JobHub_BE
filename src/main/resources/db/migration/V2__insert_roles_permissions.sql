-- ===========================================
-- VERSION 2 - INITIAL ROLES & PERMISSIONS DATA
-- ===========================================
-- Author : Tu Anh Dai
-- Date   : 2025-10-19
-- Description:
--   - Insert default roles: ADMIN, RECRUITER, JOB_SEEKER
--   - Insert basic permissions for each role
-- ===========================================

-- =====================
-- INSERT ROLES
-- =====================
INSERT INTO roles (role_name, role_description, create_by)
VALUES
    ('ADMIN', 'System administrator with full access', 'SYSTEM'),
    ('RECRUITER', 'Recruiter who can post and manage jobs', 'SYSTEM'),
    ('JOB_SEEKER', 'Job seeker who can apply for jobs', 'SYSTEM');

-- =====================
-- INSERT PERMISSIONS
-- =====================
INSERT INTO permissions (permission_name, permission_description, create_by)
VALUES
    ('MANAGE_USERS', 'Create, update, and delete user accounts', 'SYSTEM'),
    ('MANAGE_JOBS', 'Create, update, and delete job postings', 'SYSTEM'),
    ('APPLY_JOBS', 'Apply to job postings', 'SYSTEM'),
    ('VIEW_DASHBOARD', 'Access admin or recruiter dashboard', 'SYSTEM');

-- =====================
-- ASSIGN PERMISSIONS TO ROLES
-- =====================

-- ADMIN: Full permissions
INSERT INTO role_permissions (role_id, permission_id)
SELECT r.role_id, p.permission_id
FROM roles r
         JOIN permissions p ON p.permission_name IN ('MANAGE_USERS', 'MANAGE_JOBS', 'APPLY_JOBS', 'VIEW_DASHBOARD')
WHERE r.role_name = 'ADMIN';

-- RECRUITER: Manage jobs and view dashboard
INSERT INTO role_permissions (role_id, permission_id)
SELECT r.role_id, p.permission_id
FROM roles r
         JOIN permissions p ON p.permission_name IN ('MANAGE_JOBS', 'VIEW_DASHBOARD')
WHERE r.role_name = 'RECRUITER';

-- JOB_SEEKER: Apply for jobs only
INSERT INTO role_permissions (role_id, permission_id)
SELECT r.role_id, p.permission_id
FROM roles r
         JOIN permissions p ON p.permission_name IN ('APPLY_JOBS')
WHERE r.role_name = 'JOB_SEEKER';
