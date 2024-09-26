-- Add the parent_unit_id column
ALTER TABLE tbl_units ADD COLUMN parent_unit_id uuid;

-- Add the foreign key constraint (initially not validated for safety)
ALTER TABLE tbl_units
    ADD CONSTRAINT FK_tbl_units_ParentID
        FOREIGN KEY (parent_unit_id)
            REFERENCES support24.tbl_units (id);