-- V3__update_task_table.sql

-- ADD columns name and position for users

ALTER TABLE tbl_system_users
    ADD name varchar;

ALTER TABLE tbl_system_users
    ADD job_title varchar;
