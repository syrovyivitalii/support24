ALTER TABLE tbl_system_users DROP COLUMN job_title;

CREATE TABLE tbl_positions(
    id UUID PRIMARY KEY,
    position_name VARCHAR UNIQUE NOT NULL,
    created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

ALTER TABLE tbl_system_users ADD COLUMN position_id UUID,
                                ADD FOREIGN KEY (position_id) REFERENCES tbl_positions(id);