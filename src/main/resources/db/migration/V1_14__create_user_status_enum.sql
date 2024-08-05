CREATE TYPE system_users_status AS ENUM ('ACTIVE', 'DELETED');
ALTER TABLE tbl_system_users ADD COLUMN status system_users_status NOT NULL DEFAULT 'ACTIVE';