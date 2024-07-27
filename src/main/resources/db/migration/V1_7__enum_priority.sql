ALTER TABLE tbl_tasks
DROP COLUMN priority;

CREATE TYPE task_priority AS ENUM ('HIGH', 'MEDIUM', 'LOW');
ALTER TABLE tbl_tasks ADD COLUMN priority task_priority NOT NULL DEFAULT 'LOW';