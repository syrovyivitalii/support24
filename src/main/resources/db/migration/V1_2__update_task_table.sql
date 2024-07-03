-- V2__update_task_table.sql

-- Remove NOT NULL constraints and rename columns
ALTER TABLE tbl_tasks
    ALTER COLUMN status DROP NOT NULL,
    ALTER COLUMN priority DROP NOT NULL,
    ALTER COLUMN created_for_id DROP NOT NULL,
    ALTER COLUMN create_by_id DROP NOT NULL;

-- Rename column create_by_id to created_by_id
ALTER TABLE tbl_tasks RENAME COLUMN create_by_id TO created_by_id;

-- Rename index to match the new column name
ALTER INDEX idx_tasks_create_by_id RENAME TO idx_tasks_created_by_id;
