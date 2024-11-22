-- Drop existing columns
ALTER TABLE tbl_devices DROP COLUMN user_name;
ALTER TABLE tbl_devices DROP COLUMN user_position;

-- Add a new column with a foreign key constraint
ALTER TABLE tbl_devices
    ADD COLUMN user_id UUID,
ADD CONSTRAINT fk_device_user FOREIGN KEY (user_id) REFERENCES tbl_system_users(id);
