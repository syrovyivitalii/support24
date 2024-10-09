ALTER TABLE tbl_units ADD COLUMN location varchar;
ALTER TABLE tbl_units ADD COLUMN street varchar;
ALTER TABLE tbl_units DROP COLUMN group_id;

CREATE TYPE unit_status AS ENUM ('ACTIVE', 'DELETED');
ALTER TABLE tbl_units ADD COLUMN status unit_status NOT NULL DEFAULT 'ACTIVE';