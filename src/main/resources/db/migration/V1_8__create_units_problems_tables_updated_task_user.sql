-- V1_8

CREATE TYPE unit_type AS ENUM ('ГУ', 'РУ', 'ДПРЗ','ДПРЧ','ДПРП');
CREATE TABLE tbl_units (
    id UUID PRIMARY KEY,
    unit_name TEXT NOT NULL,
    unit_type unit_type
);

CREATE TABLE tbl_common_problems(
    id UUID PRIMARY KEY,
    problem TEXT NOT NULL
);

ALTER TABLE tbl_system_users ADD COLUMN user_unit UUID;

ALTER TABLE tbl_tasks RENAME COLUMN created_for_id TO assigned_for_id;
ALTER TABLE tbl_tasks ADD COLUMN assigned_by_id UUID;
ALTER TABLE tbl_tasks ADD COLUMN problem_type UUID;

ALTER TABLE tbl_system_users DROP COLUMN role;
CREATE TYPE user_role AS ENUM ('ROLE_SYSTEM_ADMIN', 'ROLE_SUPER_ADMIN', 'ROLE_ADMIN', 'ROLE_USER');
ALTER TABLE tbl_system_users ADD COLUMN role user_role NOT NULL DEFAULT 'ROLE_USER';

ALTER TABLE tbl_tasks DROP CONSTRAINT tbl_tasks_created_for_id_fkey;
ALTER TABLE tbl_tasks DROP CONSTRAINT tbl_tasks_create_by_id_fkey;

ALTER TABLE tbl_tasks
    ADD CONSTRAINT tbl_tasks_assigned_for_id_fkey
        FOREIGN KEY (assigned_for_id) REFERENCES tbl_system_users(id);
ALTER TABLE tbl_tasks
    ADD CONSTRAINT tbl_tasks_assigned_by_id_fkey
        FOREIGN KEY (assigned_by_id) REFERENCES tbl_system_users(id);
ALTER TABLE tbl_tasks
    ADD CONSTRAINT tbl_tasks_problem_type_fkey
        FOREIGN KEY (problem_type) REFERENCES tbl_common_problems(id);

ALTER TABLE tbl_system_users
    ADD CONSTRAINT tbl_system_users_user_unit_fkey
        FOREIGN KEY (user_unit) REFERENCES tbl_units(id);
