CREATE TYPE device_status AS ENUM ('ACTIVE', 'WRITTENOFF');
ALTER TABLE tbl_devices ADD COLUMN status device_status NOT NULL DEFAULT 'ACTIVE';
