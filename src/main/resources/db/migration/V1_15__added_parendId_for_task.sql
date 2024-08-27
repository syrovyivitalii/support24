ALTER TABLE tbl_tasks ADD COLUMN parent_id UUID;
ALTER TABLE tbl_tasks ADD FOREIGN KEY (parent_id) REFERENCES tbl_tasks(id);

CREATE TYPE task_type AS ENUM ('TASK', 'SUBTASK');
ALTER TABLE tbl_tasks ADD COLUMN task_type task_type NOT NULL DEFAULT 'TASK';

