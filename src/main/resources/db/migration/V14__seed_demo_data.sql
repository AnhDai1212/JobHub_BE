-- Seed demo data based on provided dump.
-- Safe to re-run: clear existing data, then insert the known dataset.

SET FOREIGN_KEY_CHECKS = 0;

DELETE FROM application_history;
DELETE FROM applications;
DELETE FROM favorites;
DELETE FROM candidate_skills;
DELETE FROM job_tag_mapping;
DELETE FROM job_category_mapping;
DELETE FROM job_requirements;
DELETE FROM job_recommendations;
DELETE FROM parsed_cvs;
DELETE FROM ai_logs;
DELETE FROM recruiter_documents;
DELETE FROM recruiter_consultations;
DELETE FROM recruiters;
DELETE FROM job_seekers;
DELETE FROM jobs;
DELETE FROM companies;
DELETE FROM account_roles;
DELETE FROM notifications;
DELETE FROM accounts;
DELETE FROM roles;
DELETE FROM job_tags;
DELETE FROM job_categories;

SET FOREIGN_KEY_CHECKS = 1;

INSERT INTO roles (role_id, role_name, role_description, create_date, modified_date, create_by, modified_by) VALUES
    (1, 'ADMIN', 'System administrator', '2026-01-03 18:09:24', '2026-01-03 18:09:24', 'SYSTEM', NULL),
    (2, 'RECRUITER', 'Recruiter user', '2026-01-03 18:09:24', '2026-01-03 18:09:24', 'SYSTEM', NULL),
    (3, 'JOB_SEEKER', 'Job seeker user', '2026-01-03 18:09:24', '2026-01-03 18:09:24', 'SYSTEM', NULL);

INSERT INTO accounts (account_id, email, password, last_login, status, create_date, modified_date, create_by, modified_by) VALUES
    ('0459cb86-7134-4ff0-982f-4a63b5b2dc52', 'nhdphuc2003@gmail.com', '$2a$10$oZ0hkTnuYvSbL0YVfg4k7u4QvWzgq3geGDbXf3NxaSkeny2Ff/H.q', NULL, 'ACTIVE', '2026-01-04 01:15:41', '2026-01-04 01:15:41', 'anonymousUser', 'anonymousUser'),
    ('099f040d-10f0-4e59-89fc-eaf0011f9b99', 'thaolv.qw1@gmail.com', '$2a$10$PmFRunOFb5EW4cRhf.NbDuQoQvCYJpHNSfvUhNaM5KlXwatdijro6', NULL, 'ACTIVE', '2026-01-04 17:17:12', '2026-01-04 17:17:12', 'anonymousUser', 'anonymousUser'),
    ('0ac7bd32-4b0b-4fc5-8b56-14bbdfa6e9d9', 'vanthaottgl15@gmail.com', '$2a$10$b6lrDeKYzhdC5LAStQD2EO7FdUNadWS3a.1iZHfXZd9376sHNjrXG', NULL, 'ACTIVE', '2026-01-04 02:38:56', '2026-01-04 02:38:56', 'anonymousUser', 'anonymousUser'),
    ('2855066a-5694-45d3-ba3c-6c26f0c7ca1d', 'thaolv.working@gmail.com', '$2a$10$V2ixKRFHxQ5LGjplkNPhx.JF/AlGWf1vPTDxXcXlF79Fmx1PoY2Gy', NULL, 'ACTIVE', '2026-01-06 22:54:23', '2026-01-06 22:54:23', 'anonymousUser', 'anonymousUser'),
    ('2b580238-009d-4138-8c6e-47dfb64401e3', 'thaolv.music@gmail.com', '$2a$10$D5/2vech5kr.ufor6xPiNOWNdb4oY3S0pYsHqZ7zPq4EJZKhQe6Ha', NULL, 'ACTIVE', '2026-01-04 11:00:30', '2026-01-04 11:00:30', 'anonymousUser', 'anonymousUser'),
    ('2e255281-23fc-4ebe-af62-364b875415e9', 'thaolv.learning@gmail.com', '$2a$10$eB.E8irzjkDvTHlfg0TXCeg7rLkM/vcIOUX2uUyACekgFUVA2IKtS', NULL, 'ACTIVE', '2026-01-04 15:03:34', '2026-01-04 15:03:34', 'anonymousUser', 'anonymousUser'),
    ('5595e344-0b74-4aa2-85c6-af11cb003c4e', 'thaolv.2003@gmail.com', '$2a$10$TwH90PVq0bwVOxBmM7CmyuDJKcit4QOIeBG9aN3mBgyf6VnV1toFq', NULL, 'ACTIVE', '2026-01-04 14:46:35', '2026-01-04 14:46:35', 'anonymousUser', 'anonymousUser'),
    ('56ce8f92-cb98-4447-9b48-1fad3c91f826', 'thaolv.fancy@gmail.com', '$2a$10$0vFFAkRb6w8MsiRP8YWXxuNeVMlFfIloLfQtwMcSciAEhxLjLJpGi', NULL, 'ACTIVE', '2026-01-04 10:45:16', '2026-01-04 10:45:16', 'anonymousUser', 'anonymousUser'),
    ('964786da-8a39-41e9-aba3-ca82adf98202', 'admin', '$2a$10$BTDK99pktZwmTOCRxLsntOeCgMotlqtOByFRUw1ZeNsvSCqvf2JvW', NULL, 'ACTIVE', '2026-01-04 01:09:33', '2026-01-04 01:09:33', 'system', 'system'),
    ('98fbfb12-da6b-4f25-bbb8-e58aa6e2eb5e', 'vanthaoa3glk42@gmail.com', '$2a$10$./BjJqsfzj.WoGGLv8nzfuR4GR1LwQ3XntDQG5RT0wLz6kj41lbZC', NULL, 'ACTIVE', '2026-01-04 09:53:11', '2026-01-04 09:53:11', 'anonymousUser', 'anonymousUser');

INSERT INTO account_roles (account_id, role_id) VALUES
    ('964786da-8a39-41e9-aba3-ca82adf98202', 1),
    ('0459cb86-7134-4ff0-982f-4a63b5b2dc52', 2),
    ('0ac7bd32-4b0b-4fc5-8b56-14bbdfa6e9d9', 2),
    ('2e255281-23fc-4ebe-af62-364b875415e9', 2),
    ('5595e344-0b74-4aa2-85c6-af11cb003c4e', 2),
    ('98fbfb12-da6b-4f25-bbb8-e58aa6e2eb5e', 2),
    ('0ac7bd32-4b0b-4fc5-8b56-14bbdfa6e9d9', 3),
    ('2b580238-009d-4138-8c6e-47dfb64401e3', 3),
    ('56ce8f92-cb98-4447-9b48-1fad3c91f826', 3);

INSERT INTO companies (company_id, company_name, location, website, avatar_url, is_approved, create_date, modified_date, create_by, modified_by, introduction) VALUES
    (2, 'Tập đoàn Vingroup', 'Hà Nội', 'https://vingroup.net', 'http://res.cloudinary.com/duxkk3hzk/image/upload/v1767467911/gynupe725ulnpapg6emg.png', 1, '2026-01-04 02:18:28', '2026-01-04 02:26:02', '0459cb86-7134-4ff0-982f-4a63b5b2dc52', '964786da-8a39-41e9-aba3-ca82adf98202', 'Tập đoàn Vingroup là tập đoàn kinh tế tư nhân đa ngành lớn nhất Việt Nam, hoạt động trong 3 lĩnh vực cốt lõi: Công nghệ – Công nghiệp, Thương mại Dịch vụ, Thiện nguyện Xã hội. Với sứ mệnh \'Vì một cuộc sống tốt đẹp hơn cho người Việt\', Vingroup luôn tiên phong dẫn dắt các xu hướng tiêu dùng và công nghệ tại thị trường Việt Nam cũng như vươn tầm quốc tế.'),
    (4, 'Tập đoàn Viettel', 'Hà Nội', 'https://viettel.com.vn', 'http://res.cloudinary.com/duxkk3hzk/image/upload/v1767495482/h638vuc1cbqs3wcy9pgw.png', 1, '2026-01-04 09:57:59', '2026-01-04 10:46:44', '98fbfb12-da6b-4f25-bbb8-e58aa6e2eb5e', '964786da-8a39-41e9-aba3-ca82adf98202', 'Tập đoàn Công nghiệp - Viễn thông Quân đội (Viettel) là doanh nghiệp viễn thông lớn nhất Việt Nam. Viettel không chỉ dừng lại ở dịch vụ kết nối mà còn mở rộng mạnh mẽ sang lĩnh vực nghiên cứu sản xuất công nghiệp công nghệ cao, an ninh mạng và các giải pháp số toàn diện cho Chính phủ và doanh nghiệp toàn cầu.'),
    (5, 'VNG Corporation', 'TP. Hồ Chí Minh', 'https://vng.com.vn', 'http://res.cloudinary.com/duxkk3hzk/image/upload/v1767506123/wobrcpj7lcr4yz3ade0f.png', 1, '2026-01-04 12:55:20', '2026-01-04 12:56:25', '0ac7bd32-4b0b-4fc5-8b56-14bbdfa6e9d9', '964786da-8a39-41e9-aba3-ca82adf98202', 'VNG là công ty công nghệ \'kỳ lân\' đầu tiên của Việt Nam, nổi tiếng với các nền tảng như Zalo, Zing và mảng phát hành game quy mô lớn. Với văn hóa \'Đón nhận thách thức\', VNG không ngừng đầu tư vào nghiên cứu và phát triển để mở rộng sang lĩnh vực thanh toán điện tử (ZaloPay) và các giải pháp AI Cloud.'),
    (6, 'Vinamilk', 'TP. Hồ Chí Minh', 'https://vinamilk.com.vn', 'http://res.cloudinary.com/duxkk3hzk/image/upload/v1767512933/povr9w0dgmjfvlhdf0aq.png', 1, '2026-01-04 14:48:49', '2026-01-04 16:06:58', '5595e344-0b74-4aa2-85c6-af11cb003c4e', '964786da-8a39-41e9-aba3-ca82adf98202', 'Vinamilk là biểu tượng của ngành sữa Việt Nam với hơn 45 năm lịch sử. Công ty sở hữu hệ thống trang trại đạt chuẩn Global G.A.P lớn nhất châu Á và mạng lưới nhà máy hiện đại. Vinamilk luôn chú trọng vào đổi mới sáng tạo và ứng dụng công nghệ trong sản xuất để mang lại nguồn dinh dưỡng chất lượng quốc tế cho cộng đồng.'),
    (7, 'Tập đoàn Sun Group', 'Đà Nẵng', 'https://sungroup.com.vn', 'http://res.cloudinary.com/duxkk3hzk/image/upload/v1767513924/kmbwowm9zieibo8s2cgu.webp', 1, '2026-01-04 15:05:21', '2026-01-04 16:06:59', '2e255281-23fc-4ebe-af62-364b875415e9', '964786da-8a39-41e9-aba3-ca82adf98202', 'Sun Group là tập đoàn hàng đầu trong lĩnh vực Du lịch nghỉ dưỡng, Vui chơi giải trí và Bất động sản cao cấp. Sun Group đang số hóa quản trị vận hành các khu du lịch và khách sạn sang trọng, nhằm mang lại dịch vụ hoàn hảo nhất cho du khách từ khắp nơi trên thế giới.');

INSERT INTO recruiters (recruiter_id, account_id, company_id, position, phone, avatar_url, status, create_date, modified_date, create_by, modified_by) VALUES
    (2, '0459cb86-7134-4ff0-982f-4a63b5b2dc52', 2, 'HR', '0399567737', NULL, 'APPROVED', '2026-01-03 19:18:28', '2026-01-03 19:26:02', NULL, NULL),
    (4, '98fbfb12-da6b-4f25-bbb8-e58aa6e2eb5e', 4, 'HR', '0936744386', 'http://res.cloudinary.com/duxkk3hzk/image/upload/v1767505324/bp17kshkrgdyoedcq3p5.jpg', 'APPROVED', '2026-01-04 02:57:59', '2026-01-04 05:42:03', NULL, NULL),
    (5, '0ac7bd32-4b0b-4fc5-8b56-14bbdfa6e9d9', 5, 'HR', '0822154060', NULL, 'APPROVED', '2026-01-04 05:55:20', '2026-01-04 05:56:25', NULL, NULL),
    (6, '5595e344-0b74-4aa2-85c6-af11cb003c4e', 6, 'HR', '0939468199', NULL, 'APPROVED', '2026-01-04 07:48:48', '2026-01-04 09:06:58', NULL, NULL),
    (7, '2e255281-23fc-4ebe-af62-364b875415e9', 7, 'HR', '0922849168', NULL, 'PENDING', '2026-01-04 08:05:21', '2026-01-04 10:18:56', NULL, NULL);

INSERT INTO jobs (job_id, company_id, recruiter_id, title, description, location, status, min_salary, max_salary, job_type, deadline, create_date, modified_date, create_by, modified_by, parsed_jd_json, jd_file_url, embedding) VALUES
    (1, 2, 2, 'Senior Java Developer', '- Design and develop core backend services.\n- Optimize database queries and system logic.\n- Collaborate with the team to ensure software quality.\n- Participate in code reviews and technical documentation.', 'Hà Nội', 'OPEN', 35, 60, 'FULL_TIME', '2026-06-29', '2026-01-04 02:27:50', '2026-01-04 02:28:36', '0459cb86-7134-4ff0-982f-4a63b5b2dc52', '0459cb86-7134-4ff0-982f-4a63b5b2dc52', '{}', NULL, NULL),
    (2, 4, 4, 'Mobile App Developer', '- Develop and maintain Viettel\'s mobile apps.\n- Implement new features for high-traffic apps.\n- Optimize app performance and user experience.\n- Fix bugs and ensure mobile security.', 'Hà Nội', 'OPEN', 28, 50, 'FULL_TIME', '2026-06-29', '2026-01-04 10:51:52', '2026-01-06 22:36:58', '98fbfb12-da6b-4f25-bbb8-e58aa6e2eb5e', '98fbfb12-da6b-4f25-bbb8-e58aa6e2eb5e', '{"SKILLS": ["JavaScript", "PHP", "Java", "React", "Next.js", "Laravel", "Flask", "Bootstrap", "DOM Manipulation", "RESTful API", "UI/UX Fundamentals", "Git", "GitHub", "Trello", "Postman", "XAMPP", "Laragon", "VS Code", "Communication", "Figma", "Adobe Photoshop", "HTML", "CSS", "JavaScript", "Laravel", "PHP", "MySQL", "HTML/CSS", "JavaScript", "Bootstrap", "Git", "Kotlin", "Android SDK", "MVVM Architecture", "Retrofit", "Room\\nDatabase", "Coroutines", "RecyclerView", "Git", "Kotlin", "Flutter", "Dart", "HERE Maps API", "Firebase Authentication", "Geolocator", "REST API", "Git"], "JOBPOST": ["Front-end Developer", "Information Technology Engineer", "Software Engineering", "FE Developer", "Android Developer", "Mobile Developer"]}', 'job-jd/2/b35f7e63-d49a-47ad-9769-45e18f67a5ff-LE-VAN-THAO-FE.pdf', NULL),
    (3, 4, 4, 'Network Engineer', '- Operate and monitor core network systems.\n- Ensure 24/7 network stability for services.\n- Install and configure network hardware.\n- Resolve network-related incidents and issues.', 'TP HCM', 'OPEN', 20, 40, 'FULL_TIME', '2026-04-09', '2026-01-04 12:33:21', '2026-01-06 10:45:39', '98fbfb12-da6b-4f25-bbb8-e58aa6e2eb5e', '98fbfb12-da6b-4f25-bbb8-e58aa6e2eb5e', '{"SKILLS": ["Java", "Javascript", "Typescript", "PHP", "Laravel", "ExpressJS", "NestJS", "MySQL", "MongoDB", "PostgreSQL", "Redis", "JWT", "Puppeteer", "Git", "RESTful API", "Nodejs", "ExpressJS", "JWT", "Redis", "Mongodb", "NodeMailer", "Puppeteer", "ExcelJS", "Laravel", "Laravel", "MySQL", "VNPay"]}', 'job-jd/3/d60947c9-3000-49d8-a749-4e86ee55863a-CV_DoanQuocHuyNodeJS.pdf', NULL),
    (4, 5, 5, 'UI/UX Designer', '- Design interfaces for VNG\'s social apps.\n- Create high-fidelity prototypes for features.\n- Research user behavior to improve UX.\n- Collaborate with product teams on design.', 'TP HCM', 'OPEN', 22, 45, 'FULL_TIME', '2026-05-19', '2026-01-04 13:02:51', '2026-01-04 13:02:54', '0ac7bd32-4b0b-4fc5-8b56-14bbdfa6e9d9', '0ac7bd32-4b0b-4fc5-8b56-14bbdfa6e9d9', '{}', NULL, NULL),
    (5, 6, 6, 'Frontend Developer', '- Develop web interfaces for online portals.  \n- Optimize web performance and SEO.  \n- Implement responsive designs for mobile/web.  \n- Collaborate with backend for API integration.', 'TP HCM', 'OPEN', 22, 40, 'FULL_TIME', '2026-03-30', '2026-01-04 16:10:39', '2026-01-04 16:10:42', '5595e344-0b74-4aa2-85c6-af11cb003c4e', '5595e344-0b74-4aa2-85c6-af11cb003c4e', '{}', NULL, NULL),
    (6, 4, 4, 'Cyber Security Specialist', '- Perform regular security audits for systems. \n- Monitor and respond to cyber security alerts.  \n- Develop and implement security policies.  \n- Protect national-level network infrastructure.', 'Đà Nẵng', 'OPEN', 40, 80, 'FULL_TIME', '2026-07-14', '2026-01-07 12:20:00', '2026-01-07 12:20:02', '98fbfb12-da6b-4f25-bbb8-e58aa6e2eb5e', '98fbfb12-da6b-4f25-bbb8-e58aa6e2eb5e', '{}', NULL, NULL);

INSERT INTO job_seekers (job_seeker_id, account_id, full_name, dob, phone, address, cv_url, avatar_url, bio, create_date, modified_date, create_by, modified_by) VALUES
    (2, '2b580238-009d-4138-8c6e-47dfb64401e3', 'Le Van Thao', '2003-08-06', '0947180074', 'Đà Nẵng', 'jobseeker-cv/2/f65638d7-37a0-489c-97c7-81732083ea78-LE-VAN-THAO-FE.pdf', 'http://res.cloudinary.com/duxkk3hzk/image/upload/v1767499686/r3lszxs7g1qb9vyjszku.jpg', 'Nhiều kinh nghiệm về FE', '2026-01-04 11:03:22', '2026-01-04 11:18:34', '2b580238-009d-4138-8c6e-47dfb64401e3', '2b580238-009d-4138-8c6e-47dfb64401e3'),
    (3, '56ce8f92-cb98-4447-9b48-1fad3c91f826', 'Từ Anh Đài', '2003-02-01', '0944331898', 'Đà Nẵng', 'jobseeker-cv/3/4f241e8a-dbd9-433d-ba8e-14d943877f33-CV_DoanQuocHuyNodeJS.pdf', 'http://res.cloudinary.com/duxkk3hzk/image/upload/v1767503949/ad1x3gva2khvlej5wds5.jpg', 'Nhiều kinh nghiệm BE', '2026-01-04 12:15:01', '2026-01-04 12:19:08', '56ce8f92-cb98-4447-9b48-1fad3c91f826', '56ce8f92-cb98-4447-9b48-1fad3c91f826');

INSERT INTO parsed_cvs (cv_id, job_seeker_id, file_url, extracted_text, embedding, parsed_json, create_date, modified_date, create_by, modified_by) VALUES
    ('6cafe0f2-3c0d-439e-9356-c2e2495d5145', 2, 'jobseeker-cv/2/df210230-473e-4091-ad4f-074ea1b08b06-LE-VAN-THAO-FE.pdf', 'LE VAN THAO
Front-end Developer
Personal Information
📅
06/08/2003

0947180074
✉
vanthaoa3glk42@gmail.com

https://github.com/thaolv-03

Cam Le, Da Nang
Skills
Programming Languages:
JavaScript, PHP, Java
Frameworks & Libraries:
React, Next.js (basic), Laravel,
Flask, Bootstrap, Tailwind CSS
Web Concepts:
DOM Manipulation, RESTful API
Consumption, State Management (basic),
UI/UX Fundamentals, Mobile-friendly
Layouts
Version Control & Collaboration:
Git, GitHub, Trello
Development Tools:
Postman, XAMPP, Laragon, VS Code,
Visual Studio
Soft Skills:
Problem-Solving, Time Management,
Adaptability, Communication, Self-
learning
Additional Skills:
Figma (basic), Adobe Photoshop (basic)
English Communication:
Career Objective
Final-year Software Engineering student with hands-on experience in
front-end web development using technologies such as HTML, CSS,
JavaScript, and modern frameworks. Experienced through internships and
real-world projects, with a strong foundation in user interface
development, problem solving, and teamwork. Seeking a Fresher Front-
end Developer position to apply and grow my skills in a dynamic and
collaborative working environment.
Education
Vietnam-Korea University of Information and
Communication Technology
9/2021 - Present
Information Technology Engineer
Major: Software Engineering
GPA: 3.15
Practical Experience
Internship | Fancy Media Da Nang
6/2025 - 8/2025
Web Developer Intern
• Participated in developing an attendance management system using QR
code and Face ID.
• Focused on building the front-end interface using modern front-end
frameworks and technologies.
• Developed and optimized UI components for employee check-in/check-
out, QR scanning, and real-time attendance tracking.
• Worked directly with a local database to handle data operations and
ensure smooth front-end data interaction.
• Used Git for version control and managed the project independently
through GitHub.
Projects
Keyboard Sales System
10/2022 - 12/2022
Team size: 2 | Role: FE Developer
GitHub: https://github.com/huywzz/SalesBanPhim
Tech Stack: Laravel, PHP, MySQL, HTML/CSS, JavaScript, Bootstrap, Git
• Developed a web-based application for selling keyboards, including
authentication, product management, web image and banner
management, checkout, payment, and user management.
• Implemented checkout and online payment functionality using VNPAY,
including sending confirmation emails after orders.
• Managed CRUD operations for products and user accounts.
• Collaborated in a team of 2 using Git for version control and joint
development.
eCard Mobile App
03/2023 - 05/2023
Team size: 2 | Role: Android Developer
GitHub: https://github.com/lvtdel/eCard

g
Able to read technical documents and
communicate in English at work.
Tech Stack: Kotlin, Android SDK, MVVM Architecture, Retrofit, Room
Database, Coroutines, RecyclerView, Git
• Built an Android app for creating and managing digital e-cards with
organized storage and smooth navigation.
• Implemented MVVM architecture with Room Database for offline support
and scalable code structure.
• Used Retrofit and Kotlin Coroutines for efficient data handling and
asynchronous operations.
• Collaborated in a 2-member team using Git for version control and
coordinated feature development.
Thaga Taxi App
10/2024 - 12/2024
Team size: 1 | Role: Mobile Developer
GitHub: https://github.com/thaolv-03/thaga_taxi
Tech Stack: Flutter, Dart, HERE Maps API, Firebase Authentication,
Firebase Firestore, Geolocator, REST API, Git
• Built a mobile ride-hailing app with user authentication, ride booking,
driver tracking, and trip history.
• Integrated HERE Maps API for map display, location search, routing, and
distance estimation.
• Implemented real-time location updates using Geolocator and
synchronized data via Firebase Firestore.
• Developed a clean Flutter UI with scalable architecture and managed the
project using Git.
© topcv.vn
', NULL, '{"NAME": "Le Van Thao", "AWARDS": [], "DEGREE": "", "SKILLS": ["Front-end Developer", "JavaScript", "PHP", "React", "Next.js", "Laravel", "Flask", "Bootstrap", "Tailwind CSS", "Adobe Photoshop (basic)"], "CONTACT": "0947180074", "LANGUAGE": ["English"], "LOCATION": "Da Nang, Viet Nam", "WORKED AS": ["Internship"], "UNIVERSITY": "Vietnam-Korea University of Information and Communication Technology", "DESIGNATION": "FE", "COLLEGE NAME": "Vietnam-Korea University of Information and Communication Technology", "CERTIFICATION": [], "EMAIL ADDRESS": "vanthaoa3glk42@gmail.com", "LINKEDIN LINK": "https://www.linkedin.com/in/thaolv03/", "YEAR OF GRADUATION": "2026", "COMPANIES WORKED AT": ["Fancy Media"], "YEARS OF EXPERIENCE": "1"}', '2026-01-04 11:18:34', '2026-01-04 11:18:34', '2b580238-009d-4138-8c6e-47dfb64401e3', '2b580238-009d-4138-8c6e-47dfb64401e3'),
    ('6d6dabc7-0a3f-4898-a105-c6324a6b428b', 3, 'jobseeker-cv/3/f3b2a0e6-7cfc-4b29-bd91-1f55303fc915-CV_DoanQuocHuyNodeJS.pdf', ' 
 
PROFILE 
Address: 
Da Nang, Viet Nam 
 
Phone: 
0867028723 
 
Email: 
dqh1005@gmail.com 
 
Github: 
https://github.com/huywzz 
EDUCATION 
2021-2026 
The University of Danang, 
Vietnam-Korea University of 
Information and 
Communication Technology 
 
 
DOAN QUOC HUY 
WEB DEVELOPER  
OBJECTIVE 
Short-term: Become a Back-end developer and work in a 
professional environment. 
Long-term: Learn and develop myself to achieve higher standards 
of readiness to take on tasks and challenges 
SKILLS 
Programming languages: Java, Javascript, Typescript, PHP 
Framework: Laravel, ExpressJS, NestJS 
Database: MySQL,  MongoDB, PostgreSQL, Redis 
Other:JWT, Puppeteer, Git 
 
PROJECT 
Build RESTful API for Ecommerce App | Persional  
Apr 27, 2024 - May 20, 2024 
Description: 
Provide API for basic ecommerce system with main functions such 
as: authentication, checkout, order, product manage. 
Technology: Nodejs, ExpressJS, JWT, Redis,Mongodb, NodeMailer 
 
Highlight: 
− Implement Mutex using Redis to handle the case where a 
product has many users ordering at the same time 
− Add more products from reading Excel files. 
− Verify user using Email OTP. 
− Authentication with Refresh Token. 
 
Source code: https://github.com/huywzz/Order-Pessimistic- 
 
Build mini project: Web scraper | Persional  
Apr 15, 2024 - Apr 24, 2024 
Description: 
This project focuses on building an efficient web crawler to extract 
valuable information from web pages. This tool will be designed 
to: 
− Extract data. 
− Store collected data Excel file. 
Technology: Puppeteer, ExcelJS 
Source code: https://github.com/huywzz/Order-Pessimistic-
/tree/main/crawl-data 
 
Build a keyboard sales system using Laravel | Group Work 

 
 
 
Oct 31, 2022 – Dec 20, 2022 
Description: 
Build a website to sell keyboards with functions such as 
authentication, product management, web image and banner 
management, checkout, payment, user management, etc. 
Teamsize: 2 
Technology: Laravel, MySQL 
 
My responsibilities: 
− Checkout,Payment,send mail after user order 
sussess,online payment using VNPay 
− Authentication 
− Product management 
Source code: https://github.com/huywzz/SalesBanPhim 
 
 ', NULL, '{"NAME": "Tu Anh Dai", "AWARDS": [], "DEGREE": "", "SKILLS": ["Java", "Javascript", "Typescript", "PHP", "Laravel", "ExpressJS", "NestJS", "MySQL", "MongoDB", "PostgreSQL", "Redis", "JWT", "Puppeteer", "Git"], "CONTACT": "0944331831", "LANGUAGE": ["English"], "LOCATION": "Da Nang, Viet Nam", "WORKED AS": ["Intership", "BE"], "UNIVERSITY": "The University of Danang, Vietnam-Korea University of Information and Communication Technology", "DESIGNATION": "BE", "COLLEGE NAME": "Vietnam-Korea University of Information and Communication Technology", "CERTIFICATION": [], "EMAIL ADDRESS": "daita0102@gmail.com", "LINKEDIN LINK": "https://www.linkedin.com/in/daita0102", "YEAR OF GRADUATION": "2026", "COMPANIES WORKED AT": ["Rikkeisoft"], "YEARS OF EXPERIENCE": "1"}', '2026-01-04 12:16:49', '2026-01-04 12:16:49', '56ce8f92-cb98-4447-9b48-1fad3c91f826', '56ce8f92-cb98-4447-9b48-1fad3c91f826');

INSERT INTO applications (application_id, job_id, job_seeker_id, applied_at, status, parsed_cv_id, matching_score) VALUES
    ('40cc3e42-b56a-4f69-a19f-18ebd9e96f33', 2, 3, '2026-01-04 12:17:08', 'REVIEWING', '6d6dabc7-0a3f-4898-a105-c6324a6b428b', NULL),
    ('a64ddbd4-a855-4f7c-8011-9020cccc4f80', 3, 3, '2026-01-07 12:17:39', 'APPLIED', '6d6dabc7-0a3f-4898-a105-c6324a6b428b', 31.8200),
    ('c1364a68-62f7-418c-8a00-131351e9b1eb', 2, 2, '2026-01-04 11:22:36', 'APPLIED', '6cafe0f2-3c0d-439e-9356-c2e2495d5145', NULL);

INSERT INTO application_history (history_id, application_id, status, note, updated_at) VALUES
    (1, 'c1364a68-62f7-418c-8a00-131351e9b1eb', 'APPLIED', 'Applied by job seeker', '2026-01-04 11:22:36'),
    (2, '40cc3e42-b56a-4f69-a19f-18ebd9e96f33', 'APPLIED', 'Applied by job seeker', '2026-01-04 12:17:08'),
    (3, '40cc3e42-b56a-4f69-a19f-18ebd9e96f33', 'REVIEWING', NULL, '2026-01-04 17:05:14'),
    (4, 'a64ddbd4-a855-4f7c-8011-9020cccc4f80', 'APPLIED', 'Applied by job seeker', '2026-01-07 12:17:43');

INSERT INTO candidate_skills (skill_id, job_seeker_id, skill_name, proficiency_level, years_of_experience, last_used_year, is_primary, certificate, description, create_date, modified_date) VALUES
    (1, 3, 'CI/CD', NULL, NULL, NULL, 0, NULL, NULL, '2026-01-04 05:18:51', '2026-01-04 05:18:51'),
    (2, 3, 'NodeJS', NULL, NULL, NULL, 0, NULL, NULL, '2026-01-04 05:18:51', '2026-01-04 05:18:51'),
    (3, 3, 'Java', NULL, NULL, NULL, 0, NULL, NULL, '2026-01-04 05:18:51', '2026-01-04 05:18:51'),
    (4, 3, 'Alibaba Cloud', NULL, NULL, NULL, 0, NULL, NULL, '2026-01-05 05:36:07', '2026-01-05 05:36:07'),
    (5, 2, 'Figma', NULL, NULL, NULL, 0, NULL, NULL, '2026-01-06 15:55:13', '2026-01-06 15:55:13'),
    (6, 2, 'JavaScript', NULL, NULL, NULL, 0, NULL, NULL, '2026-01-06 15:55:13', '2026-01-06 15:55:13');

INSERT INTO favorites (favorite_id, job_seeker_id, job_id) VALUES
    (1, 2, 2),
    (2, 2, 1),
    (3, 3, 3),
    (4, 3, 5);

INSERT INTO job_categories (category_id, category_name) VALUES
    (1, 'Software Development'),
    (2, 'Data & Analytics'),
    (3, 'Cloud & DevOps'),
    (4, 'AI & Machine Learning'),
    (5, 'Cyber Security'),
    (6, 'UI/UX Design'),
    (7, 'Product Management'),
    (8, 'Project Management'),
    (9, 'Game Development'),
    (10, 'IT Support & Networking'),
    (11, 'Business Analyst'),
    (12, 'Marketing & Communications'),
    (13, 'Sales & Business Development'),
    (14, 'Finance & Accounting'),
    (15, 'Human Resources'),
    (16, 'Engineering & Manufacturing'),
    (17, 'Logistics & Supply Chain'),
    (18, 'Aviation Services'),
    (19, 'Construction & Architecture'),
    (20, 'Legal & Compliance'),
    (21, 'Agriculture & Food Tech');

INSERT INTO job_category_mapping (job_id, category_id) VALUES
    (1, 1),
    (2, 1),
    (3, 10),
    (4, 6),
    (5, 1),
    (6, 5);

INSERT INTO job_requirements (requirement_id, job_id, requirement_text, display_order, create_date, modified_date, create_by, modified_by) VALUES
    (2, 1, '- At least 5 years of experience in Java development.', 1, '2026-01-04 02:28:36', '2026-01-04 02:28:36', '0459cb86-7134-4ff0-982f-4a63b5b2dc52', '0459cb86-7134-4ff0-982f-4a63b5b2dc52'),
    (3, 1, '- Proficient in Spring Boot and Microservices architecture.', 2, '2026-01-04 02:28:36', '2026-01-04 02:28:36', '0459cb86-7134-4ff0-982f-4a63b5b2dc52', '0459cb86-7134-4ff0-982f-4a63b5b2dc52'),
    (4, 1, '- Strong knowledge of SQL and NoSQL databases.', 3, '2026-01-04 02:28:36', '2026-01-04 02:28:36', '0459cb86-7134-4ff0-982f-4a63b5b2dc52', '0459cb86-7134-4ff0-982f-4a63b5b2dc52'),
    (5, 1, '- Experience in optimizing system performance.', 4, '2026-01-04 02:28:36', '2026-01-04 02:28:36', '0459cb86-7134-4ff0-982f-4a63b5b2dc52', '0459cb86-7134-4ff0-982f-4a63b5b2dc52'),
    (6, 2, '- Strong experience in Flutter development.', 1, '2026-01-04 10:51:52', '2026-01-04 10:51:52', '98fbfb12-da6b-4f25-bbb8-e58aa6e2eb5e', '98fbfb12-da6b-4f25-bbb8-e58aa6e2eb5e'),
    (7, 2, '- Experience in publishing apps on Google Play/App Store.', 2, '2026-01-04 10:51:53', '2026-01-04 10:51:53', '98fbfb12-da6b-4f25-bbb8-e58aa6e2eb5e', '98fbfb12-da6b-4f25-bbb8-e58aa6e2eb5e'),
    (8, 2, '- Knowledge of state management in Flutter.', 3, '2026-01-04 10:51:53', '2026-01-04 10:51:53', '98fbfb12-da6b-4f25-bbb8-e58aa6e2eb5e', '98fbfb12-da6b-4f25-bbb8-e58aa6e2eb5e'),
    (9, 2, '- Familiar with RESTful APIs integration.', 4, '2026-01-04 10:51:53', '2026-01-04 10:51:53', '98fbfb12-da6b-4f25-bbb8-e58aa6e2eb5e', '98fbfb12-da6b-4f25-bbb8-e58aa6e2eb5e'),
    (10, 3, '- Certified in CCNA or CCNP.', 1, '2026-01-04 12:33:22', '2026-01-04 12:33:22', '98fbfb12-da6b-4f25-bbb8-e58aa6e2eb5e', '98fbfb12-da6b-4f25-bbb8-e58aa6e2eb5e'),
    (11, 3, '- Experience in managing telco network infrastructure.', 2, '2026-01-04 12:33:22', '2026-01-04 12:33:22', '98fbfb12-da6b-4f25-bbb8-e58aa6e2eb5e', '98fbfb12-da6b-4f25-bbb8-e58aa6e2eb5e'),
    (12, 3, '- Strong network troubleshooting skills.', 3, '2026-01-04 12:33:22', '2026-01-04 12:33:22', '98fbfb12-da6b-4f25-bbb8-e58aa6e2eb5e', '98fbfb12-da6b-4f25-bbb8-e58aa6e2eb5e'),
    (13, 3, '- Knowledge of network security protocols.', 4, '2026-01-04 12:33:22', '2026-01-04 12:33:22', '98fbfb12-da6b-4f25-bbb8-e58aa6e2eb5e', '98fbfb12-da6b-4f25-bbb8-e58aa6e2eb5e'),
    (14, 4, '- Strong portfolio in mobile app design.', 1, '2026-01-04 13:02:51', '2026-01-04 13:02:51', '0ac7bd32-4b0b-4fc5-8b56-14bbdfa6e9d9', '0ac7bd32-4b0b-4fc5-8b56-14bbdfa6e9d9'),
    (15, 4, '- Proficiency in Figma and design tools.', 2, '2026-01-04 13:02:51', '2026-01-04 13:02:51', '0ac7bd32-4b0b-4fc5-8b56-14bbdfa6e9d9', '0ac7bd32-4b0b-4fc5-8b56-14bbdfa6e9d9'),
    (16, 4, '- Ability to conduct user research and testing.', 3, '2026-01-04 13:02:51', '2026-01-04 13:02:51', '0ac7bd32-4b0b-4fc5-8b56-14bbdfa6e9d9', '0ac7bd32-4b0b-4fc5-8b56-14bbdfa6e9d9'),
    (17, 4, '- Strong visual design and aesthetic sense.', 4, '2026-01-04 13:02:51', '2026-01-04 13:02:51', '0ac7bd32-4b0b-4fc5-8b56-14bbdfa6e9d9', '0ac7bd32-4b0b-4fc5-8b56-14bbdfa6e9d9'),
    (18, 5, '- Strong skills in VueJS and JavaScript.', 1, '2026-01-04 16:10:40', '2026-01-04 16:10:40', '5595e344-0b74-4aa2-85c6-af11cb003c4e', '5595e344-0b74-4aa2-85c6-af11cb003c4e'),
    (19, 5, '- Experience in building e-commerce interfaces.', 2, '2026-01-04 16:10:40', '2026-01-04 16:10:40', '5595e344-0b74-4aa2-85c6-af11cb003c4e', '5595e344-0b74-4aa2-85c6-af11cb003c4e'),
    (20, 5, '- Knowledge of HTML, CSS, and web accessibility.', 3, '2026-01-04 16:10:40', '2026-01-04 16:10:40', '5595e344-0b74-4aa2-85c6-af11cb003c4e', '5595e344-0b74-4aa2-85c6-af11cb003c4e'),
    (21, 5, '- Familiar with Git and team collaboration.', 4, '2026-01-04 16:10:40', '2026-01-04 16:10:40', '5595e344-0b74-4aa2-85c6-af11cb003c4e', '5595e344-0b74-4aa2-85c6-af11cb003c4e'),
    (22, 6, '- Experience in vulnerability assessment (Pentest).', 1, '2026-01-07 12:20:00', '2026-01-07 12:20:00', '98fbfb12-da6b-4f25-bbb8-e58aa6e2eb5e', '98fbfb12-da6b-4f25-bbb8-e58aa6e2eb5e'),
    (23, 6, '- Certified in OSCP, CEH, or CISSP.', 2, '2026-01-07 12:20:00', '2026-01-07 12:20:00', '98fbfb12-da6b-4f25-bbb8-e58aa6e2eb5e', '98fbfb12-da6b-4f25-bbb8-e58aa6e2eb5e'),
    (24, 6, '- Knowledge of network security and firewalls.', 3, '2026-01-07 12:20:00', '2026-01-07 12:20:00', '98fbfb12-da6b-4f25-bbb8-e58aa6e2eb5e', '98fbfb12-da6b-4f25-bbb8-e58aa6e2eb5e'),
    (25, 6, '- Ability to handle security incidents.', 4, '2026-01-07 12:20:00', '2026-01-07 12:20:00', '98fbfb12-da6b-4f25-bbb8-e58aa6e2eb5e', '98fbfb12-da6b-4f25-bbb8-e58aa6e2eb5e');

INSERT INTO job_tags (tag_id, tag_name) VALUES
    (1, 'Java'),
    (2, 'Spring Boot'),
    (3, 'Microservices'),
    (4, 'ReactJS'),
    (5, 'TypeScript'),
    (6, 'TailwindCSS'),
    (7, 'NodeJS'),
    (8, '.NET Core'),
    (9, 'C#'),
    (10, 'Golang'),
    (11, 'Python'),
    (12, 'C++'),
    (13, 'PHP'),
    (14, 'Laravel'),
    (15, 'Magento'),
    (16, 'VueJS'),
    (17, 'JavaScript'),
    (18, 'HTML/CSS'),
    (19, 'Swift'),
    (20, 'Kotlin'),
    (21, 'Flutter'),
    (22, 'Dart'),
    (30, 'SQL'),
    (31, 'PostgreSQL'),
    (32, 'Oracle'),
    (33, 'SQL Server'),
    (34, 'MongoDB'),
    (35, 'Redis'),
    (36, 'Kafka'),
    (37, 'Spark'),
    (38, 'Hadoop'),
    (39, 'ETL'),
    (40, 'Data Warehouse'),
    (41, 'PowerBI'),
    (42, 'Tableau'),
    (43, 'Machine Learning'),
    (44, 'NLP'),
    (45, 'Computer Vision'),
    (46, 'PyTorch'),
    (47, 'AI'),
    (48, 'AWS'),
    (49, 'Alibaba Cloud'),
    (50, 'Kubernetes'),
    (51, 'Docker'),
    (52, 'Terraform'),
    (53, 'Ansible'),
    (54, 'Jenkins'),
    (55, 'CI/CD'),
    (56, 'SRE'),
    (60, 'Cyber Security'),
    (61, 'SOC'),
    (62, 'Pentest'),
    (63, 'SIEM'),
    (64, 'Firewall'),
    (65, 'IDS/IPS'),
    (66, 'Network Security'),
    (67, 'CCNA'),
    (68, 'CCNP'),
    (69, 'Automation Test'),
    (70, 'Selenium'),
    (71, 'Appium'),
    (72, 'JMeter'),
    (73, 'QA'),
    (80, 'Agile'),
    (81, 'Scrum'),
    (82, 'PMP'),
    (83, 'Project Management'),
    (84, 'Product Strategy'),
    (85, 'Roadmap'),
    (86, 'Requirement Analysis'),
    (87, 'UML'),
    (88, 'Figma'),
    (89, 'Prototyping'),
    (90, 'UX Research'),
    (91, 'Interaction Design'),
    (92, 'Visual Design'),
    (93, 'Game Logic'),
    (94, 'Unity'),
    (100, 'Sales'),
    (101, 'Negotiation'),
    (102, 'B2B Sales'),
    (103, 'Real Estate'),
    (104, 'Digital Marketing'),
    (105, 'SEO'),
    (106, 'Facebook Ads'),
    (107, 'Google Ads'),
    (108, 'Branding'),
    (109, 'Content Marketing'),
    (110, 'Performance Marketing'),
    (111, 'Affiliate Marketing'),
    (112, 'Finance'),
    (113, 'Accounting'),
    (114, 'Tax'),
    (115, 'IFRS'),
    (116, 'Financial Modeling'),
    (117, 'M&A'),
    (118, 'Risk Management'),
    (120, 'SAP'),
    (121, 'ERP'),
    (122, 'Supply Chain'),
    (123, 'Logistics'),
    (124, 'Warehouse Management'),
    (125, 'PLC'),
    (126, 'SCADA'),
    (127, 'AutoCAD'),
    (128, 'SolidWorks'),
    (129, 'Revit'),
    (130, 'FEA'),
    (131, 'RTOS'),
    (132, 'CAN Bus'),
    (133, 'Embedded Systems'),
    (134, 'Battery Tech'),
    (135, 'R&D'),
    (136, 'Food Safety'),
    (137, 'QC'),
    (140, 'Pilot License'),
    (141, 'ICAO'),
    (142, 'English'),
    (143, 'Customer Service'),
    (144, 'Recruiting'),
    (145, 'Tech Sourcing'),
    (146, 'LinkedIn');

INSERT INTO job_tag_mapping (job_id, tag_id) VALUES
    (1, 1),
    (1, 2),
    (1, 3),
    (2, 21),
    (2, 22),
    (3, 66),
    (3, 67),
    (3, 68),
    (4, 88),
    (4, 89),
    (4, 90),
    (5, 16),
    (5, 17),
    (5, 18),
    (6, 61),
    (6, 62),
    (6, 63);

INSERT INTO recruiter_consultations (consultation_id, recruiter_id, hiring_position, industry, budget, currency, notes, create_date, modified_date) VALUES
    (2, 2, 'Senior Java Developer', 'Software Development', 35000000, 'VND/tháng', 'Required 5+ years experience; Performance-based year-end bonus.', '2026-01-03 19:21:27', '2026-01-03 19:21:27'),
    (4, 4, 'Cyber Security Specialist', 'Cyber Security', 35000000, 'VND/tháng', 'OSCP/CEH certifications preferred; Specialized allowances for telecom sector.', '2026-01-04 03:33:44', '2026-01-04 03:33:44'),
    (5, 5, 'Senior Golang Developer', 'Software Development', 45000000, 'VND/tháng', 'ESOP opportunities; Modern office environment at VNG Campus with 5-star canteen.', '2026-01-04 05:55:49', '2026-01-04 05:55:49'),
    (6, 7, 'Fullstack Developer', 'Software Development', 28000000, 'VND/tháng', 'Ready to travel to resort sites; Accommodation support for staff at project locations.', '2026-01-04 08:06:57', '2026-01-04 08:06:57'),
    (7, 6, 'Food Technologist', 'Agriculture & Food Tech', 18000000, 'VND/tháng', 'Working in modern plants; Quarterly dairy product welfare for employees.', '2026-01-04 08:08:19', '2026-01-04 08:08:19');

INSERT INTO recruiter_documents (document_id, recruiter_id, file_url, file_name, content_type, create_date, modified_date) VALUES
    (1, 2, 'recruiter-docs/2/d6fe0a3c-e555-47b0-8c13-04660c44f34a-Vingroup_logo.svg.png', 'Vingroup_logo.svg.png', 'image/png', '2026-01-03 19:26:09', '2026-01-03 19:26:09'),
    (3, 4, 'recruiter-docs/4/0b12f09d-98fe-43eb-9ca4-fb7d3b0ea3bf-Viettel_logo_2021.svg.png', 'Viettel_logo_2021.svg.png', 'image/png', '2026-01-04 03:46:00', '2026-01-04 03:46:00'),
    (4, 5, 'recruiter-docs/5/7fe39952-3919-4d93-a4f9-0c844a10b0e8-VNG_Corp._logo.svg.png', 'VNG_Corp._logo.svg.png', 'image/png', '2026-01-04 05:55:54', '2026-01-04 05:55:54'),
    (5, 7, 'recruiter-docs/7/527e71fd-bed1-4163-bb58-91091d837db0-vinamilk-seeklogo.png', 'vinamilk-seeklogo.png', 'image/png', '2026-01-04 08:07:04', '2026-01-04 08:07:04'),
    (6, 6, 'recruiter-docs/6/2b7d513a-bef2-43de-a776-24c1a28694d8-vinamilk-seeklogo.png', 'vinamilk-seeklogo.png', 'image/png', '2026-01-04 08:08:25', '2026-01-04 08:08:25');
