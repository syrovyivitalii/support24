-- V1__init_project.sql

-- Create the tbl_system_users table
CREATE TABLE tbl_system_users (
  id UUID PRIMARY KEY,
  email TEXT NOT NULL,
  password TEXT NOT NULL,
  role TEXT NOT NULL CHECK (role IN ('ROLE_SUPER_ADMIN', 'ROLE_ADMIN', 'ROLE_USER')),
  created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  verify BOOLEAN DEFAULT FALSE
);

-- Create the tbl_tasks table
CREATE TABLE tbl_tasks (
   id UUID PRIMARY KEY,
   name TEXT NOT NULL,
   description TEXT,
   created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
   updated_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
   due_date TIMESTAMP,
   status TEXT NOT NULL CHECK (status IN ('COMPLETED', 'INPROGRESS', 'UNCOMPLETED')),
   priority TEXT NOT NULL CHECK (priority IN ('HIGH', 'MEDIUM', 'LOW')),
   created_for_id UUID NOT NULL,
   create_by_id UUID NOT NULL,
   FOREIGN KEY (created_for_id) REFERENCES tbl_system_users(id),
   FOREIGN KEY (create_by_id) REFERENCES tbl_system_users(id)
);

-- Create indices for the foreign keys to improve query performance
CREATE INDEX idx_tasks_created_for_id ON tbl_tasks(created_for_id);
CREATE INDEX idx_tasks_create_by_id ON tbl_tasks(create_by_id);
