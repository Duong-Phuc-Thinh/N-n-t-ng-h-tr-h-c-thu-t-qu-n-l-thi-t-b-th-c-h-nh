USE smart_academic_lab;

ALTER TABLE mentoring_sessions
    ADD COLUMN requested_equipment_name VARCHAR(150) NULL AFTER lecturer_note,
    ADD COLUMN requested_equipment_quantity INT NULL AFTER requested_equipment_name,
    ADD COLUMN requested_equipment_note TEXT NULL AFTER requested_equipment_quantity;
