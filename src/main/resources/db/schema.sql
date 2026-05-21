CREATE DATABASE IF NOT EXISTS smart_academic_lab
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE smart_academic_lab;

DROP TABLE IF EXISTS borrowing_details;
DROP TABLE IF EXISTS borrowing_records;
DROP TABLE IF EXISTS academic_evaluations;
DROP TABLE IF EXISTS mentoring_sessions;
DROP TABLE IF EXISTS equipments;
DROP TABLE IF EXISTS lecturers;
DROP TABLE IF EXISTS user_profiles;
DROP TABLE IF EXISTS majors;
DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS lab_room_types;
DROP TABLE IF EXISTS departments;

CREATE TABLE departments (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    code VARCHAR(30) NOT NULL UNIQUE,
    name VARCHAR(150) NOT NULL,
    description TEXT,
    active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE = InnoDB;

CREATE TABLE majors (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    department_id BIGINT NOT NULL,
    code VARCHAR(30) NOT NULL UNIQUE,
    name VARCHAR(150) NOT NULL,
    description TEXT,
    active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_majors_department
        FOREIGN KEY (department_id) REFERENCES departments(id)
        ON DELETE RESTRICT
) ENGINE = InnoDB;

CREATE TABLE lab_room_types (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    code VARCHAR(30) NOT NULL UNIQUE,
    name VARCHAR(150) NOT NULL,
    description TEXT,
    active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE = InnoDB;

CREATE TABLE users (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    email VARCHAR(120) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    full_name VARCHAR(120) NOT NULL,
    phone VARCHAR(20),
    role VARCHAR(20) NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT chk_users_role CHECK (role IN ('STUDENT', 'LECTURER', 'ADMIN')),
    CONSTRAINT chk_users_status CHECK (status IN ('ACTIVE', 'INACTIVE', 'LOCKED'))
) ENGINE = InnoDB;

CREATE TABLE user_profiles (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL UNIQUE,
    department_id BIGINT,
    student_code VARCHAR(30) UNIQUE,
    class_name VARCHAR(50),
    date_of_birth DATE,
    gender VARCHAR(20),
    address VARCHAR(255),
    avatar_url VARCHAR(255),
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_user_profiles_user
        FOREIGN KEY (user_id) REFERENCES users(id)
        ON DELETE CASCADE,
    CONSTRAINT fk_user_profiles_department
        FOREIGN KEY (department_id) REFERENCES departments(id)
        ON DELETE SET NULL
) ENGINE = InnoDB;

CREATE TABLE lecturers (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL UNIQUE,
    department_id BIGINT NOT NULL,
    lecturer_code VARCHAR(30) NOT NULL UNIQUE,
    academic_title VARCHAR(80),
    specialization VARCHAR(150),
    office_room VARCHAR(50),
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_lecturers_user
        FOREIGN KEY (user_id) REFERENCES users(id)
        ON DELETE CASCADE,
    CONSTRAINT fk_lecturers_department
        FOREIGN KEY (department_id) REFERENCES departments(id)
        ON DELETE RESTRICT
) ENGINE = InnoDB;

CREATE TABLE equipments (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    lab_room_type_id BIGINT NOT NULL,
    code VARCHAR(50) NOT NULL UNIQUE,
    name VARCHAR(150) NOT NULL,
    description TEXT,
    brand VARCHAR(100),
    model VARCHAR(100),
    quantity_total INT NOT NULL,
    quantity_available INT NOT NULL,
    active BOOLEAN NOT NULL DEFAULT TRUE,
    status VARCHAR(20) NOT NULL DEFAULT 'AVAILABLE',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_equipments_lab_room_type
        FOREIGN KEY (lab_room_type_id) REFERENCES lab_room_types(id)
        ON DELETE RESTRICT,
    CONSTRAINT chk_equipments_quantity CHECK (quantity_total >= 0 AND quantity_available >= 0 AND quantity_available <= quantity_total),
    CONSTRAINT chk_equipments_status CHECK (status IN ('AVAILABLE', 'BORROWED', 'MAINTENANCE', 'DAMAGED', 'RETIRED'))
) ENGINE = InnoDB;

CREATE TABLE mentoring_sessions (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    student_id BIGINT NOT NULL,
    lecturer_id BIGINT NOT NULL,
    major_id BIGINT,
    topic VARCHAR(150) NOT NULL,
    description TEXT,
    scheduled_at DATETIME NOT NULL,
    duration_minutes INT NOT NULL,
    location VARCHAR(120),
    status VARCHAR(20) NOT NULL DEFAULT 'PENDING',
    cancellation_reason TEXT,
    lecturer_note TEXT,
    requested_equipment_name VARCHAR(150),
    requested_equipment_quantity INT,
    requested_equipment_note TEXT,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_mentoring_sessions_student
        FOREIGN KEY (student_id) REFERENCES users(id)
        ON DELETE RESTRICT,
    CONSTRAINT fk_mentoring_sessions_lecturer
        FOREIGN KEY (lecturer_id) REFERENCES lecturers(id)
        ON DELETE RESTRICT,
    CONSTRAINT fk_mentoring_sessions_major
        FOREIGN KEY (major_id) REFERENCES majors(id)
        ON DELETE SET NULL,
    CONSTRAINT chk_mentoring_sessions_duration CHECK (duration_minutes > 0),
    CONSTRAINT chk_mentoring_sessions_status CHECK (status IN ('PENDING', 'APPROVED', 'COMPLETED', 'CANCELLED', 'REJECTED'))
) ENGINE = InnoDB;

CREATE TABLE academic_evaluations (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    student_id BIGINT NOT NULL,
    lecturer_id BIGINT NOT NULL,
    mentoring_session_id BIGINT,
    score INT NOT NULL,
    grade VARCHAR(20) NOT NULL,
    comment TEXT NOT NULL,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_academic_evaluations_student
        FOREIGN KEY (student_id) REFERENCES users(id)
        ON DELETE RESTRICT,
    CONSTRAINT fk_academic_evaluations_lecturer
        FOREIGN KEY (lecturer_id) REFERENCES lecturers(id)
        ON DELETE RESTRICT,
    CONSTRAINT fk_academic_evaluations_mentoring_session
        FOREIGN KEY (mentoring_session_id) REFERENCES mentoring_sessions(id)
        ON DELETE SET NULL,
    CONSTRAINT chk_academic_evaluations_score CHECK (score BETWEEN 0 AND 100),
    CONSTRAINT chk_academic_evaluations_grade CHECK (grade IN ('EXCELLENT', 'GOOD', 'AVERAGE', 'WEAK'))
) ENGINE = InnoDB;

CREATE TABLE borrowing_records (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    student_id BIGINT NOT NULL,
    approved_by_lecturer_id BIGINT,
    exported_by_admin_id BIGINT,
    mentoring_session_id BIGINT,
    purpose VARCHAR(200) NOT NULL,
    borrow_date DATE NOT NULL,
    expected_return_date DATE NOT NULL,
    actual_return_date DATE,
    status VARCHAR(20) NOT NULL DEFAULT 'PENDING',
    note TEXT,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_borrowing_records_student
        FOREIGN KEY (student_id) REFERENCES users(id)
        ON DELETE RESTRICT,
    CONSTRAINT fk_borrowing_records_approved_by_lecturer
        FOREIGN KEY (approved_by_lecturer_id) REFERENCES lecturers(id)
        ON DELETE SET NULL,
    CONSTRAINT fk_borrowing_records_exported_by_admin
        FOREIGN KEY (exported_by_admin_id) REFERENCES users(id)
        ON DELETE SET NULL,
    CONSTRAINT fk_borrowing_records_mentoring_session
        FOREIGN KEY (mentoring_session_id) REFERENCES mentoring_sessions(id)
        ON DELETE SET NULL,
    CONSTRAINT chk_borrowing_records_dates CHECK (expected_return_date >= borrow_date),
    CONSTRAINT chk_borrowing_records_status CHECK (status IN ('PENDING', 'APPROVED', 'WAITING_ALLOCATION', 'ISSUED', 'EXPORTED', 'RETURNED', 'CANCELLED', 'REJECTED', 'OVERDUE'))
) ENGINE = InnoDB;

CREATE TABLE borrowing_details (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    borrowing_record_id BIGINT NOT NULL,
    equipment_id BIGINT NOT NULL,
    quantity INT NOT NULL,
    returned_quantity INT NOT NULL DEFAULT 0,
    status VARCHAR(20) NOT NULL DEFAULT 'BORROWING',
    condition_note TEXT,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_borrowing_details_record
        FOREIGN KEY (borrowing_record_id) REFERENCES borrowing_records(id)
        ON DELETE CASCADE,
    CONSTRAINT fk_borrowing_details_equipment
        FOREIGN KEY (equipment_id) REFERENCES equipments(id)
        ON DELETE RESTRICT,
    CONSTRAINT chk_borrowing_details_quantity CHECK (quantity > 0 AND returned_quantity >= 0 AND returned_quantity <= quantity),
    CONSTRAINT chk_borrowing_details_status CHECK (status IN ('BORROWING', 'RETURNED', 'DAMAGED', 'LOST'))
) ENGINE = InnoDB;

CREATE INDEX idx_users_role ON users(role);
CREATE INDEX idx_user_profiles_department ON user_profiles(department_id);
CREATE INDEX idx_lecturers_department ON lecturers(department_id);
CREATE INDEX idx_majors_department ON majors(department_id);
CREATE INDEX idx_equipments_lab_room_type ON equipments(lab_room_type_id);
CREATE INDEX idx_mentoring_sessions_student ON mentoring_sessions(student_id);
CREATE INDEX idx_mentoring_sessions_lecturer ON mentoring_sessions(lecturer_id);
CREATE INDEX idx_mentoring_sessions_major ON mentoring_sessions(major_id);
CREATE INDEX idx_mentoring_sessions_status ON mentoring_sessions(status);
CREATE INDEX idx_academic_evaluations_student ON academic_evaluations(student_id);
CREATE INDEX idx_borrowing_records_student ON borrowing_records(student_id);
CREATE INDEX idx_borrowing_records_status ON borrowing_records(status);
CREATE INDEX idx_borrowing_details_record ON borrowing_details(borrowing_record_id);
CREATE INDEX idx_borrowing_details_equipment ON borrowing_details(equipment_id);
