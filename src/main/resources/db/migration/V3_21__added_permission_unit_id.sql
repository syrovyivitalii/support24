ALTER TABLE tbl_system_users ADD COLUMN permission_unit_id UUID,
ADD CONSTRAINT fk_user_permission_unit FOREIGN KEY (permission_unit_id) REFERENCES tbl_units(id);