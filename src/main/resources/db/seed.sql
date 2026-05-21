USE smart_academic_lab;

INSERT INTO departments (code, name, description)
VALUES
    ('CNTT', 'Cong nghe thong tin', 'Khoa phu trach cac nganh phan mem, mang may tinh va he thong thong tin'),
    ('DTVT', 'Dien tu vien thong', 'Khoa phu trach mach dien tu, truyen thong va he thong nhung'),
    ('QTKD', 'Quan tri kinh doanh', 'Khoa phu trach quan tri, marketing va khoi nghiep'),
    ('NN', 'Ngoai ngu', 'Khoa phu trach ngoai ngu va ky nang giao tiep'),
    ('KHCB', 'Khoa hoc co ban', 'Khoa phu trach toan, ly va cac mon co so')
ON DUPLICATE KEY UPDATE
    name = VALUES(name),
    description = VALUES(description),
    active = TRUE,
    updated_at = CURRENT_TIMESTAMP;

INSERT INTO lab_room_types (code, name, description)
VALUES
    ('COMPUTER', 'Phong may tinh', 'Phong thuc hanh lap trinh, co so du lieu va mang may tinh'),
    ('ELECTRONICS', 'Phong dien tu', 'Phong thuc hanh mach dien tu va he thong nhung'),
    ('NETWORK', 'Phong mang', 'Phong thuc hanh cau hinh thiet bi mang va bao mat'),
    ('MULTIMEDIA', 'Phong da phuong tien', 'Phong thuc hanh thiet ke, video va xu ly anh'),
    ('GENERAL', 'Phong thuc hanh tong hop', 'Phong ho tro cac buoi thuc hanh va seminar')
ON DUPLICATE KEY UPDATE
    name = VALUES(name),
    description = VALUES(description),
    active = TRUE,
    updated_at = CURRENT_TIMESTAMP;

INSERT INTO majors (department_id, code, name, description)
SELECT d.id, v.code, v.name, v.description
FROM departments d
JOIN (
    SELECT 'CNTT' AS department_code, '7480103' AS code, 'Ky thuat phan mem' AS name, 'Phat trien va van hanh phan mem ung dung' AS description
    UNION ALL SELECT 'CNTT', '7480104', 'He thong thong tin', 'Phan tich, thiet ke va quan tri he thong thong tin'
    UNION ALL SELECT 'CNTT', '7480102', 'An toan thong tin', 'Bao mat he thong va du lieu'
    UNION ALL SELECT 'CNTT', '7480107', 'Tri tue nhan tao', 'Hoc may, khai pha du lieu va ung dung AI'
    UNION ALL SELECT 'DTVT', '7520207', 'Dien tu vien thong', 'Mach dien tu, truyen thong va he thong nhung'
    UNION ALL SELECT 'QTKD', '7340101', 'Quan tri kinh doanh', 'Quan tri doanh nghiep, marketing va khoi nghiep'
    UNION ALL SELECT 'NN', '7220201', 'Ngon ngu Anh', 'Ngoai ngu ung dung va giao tiep hoc thuat'
) v ON v.department_code = d.code
ON DUPLICATE KEY UPDATE
    department_id = VALUES(department_id),
    name = VALUES(name),
    description = VALUES(description),
    active = TRUE,
    updated_at = CURRENT_TIMESTAMP;
