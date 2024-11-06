CREATE TABLE tbl_ranks (
    id UUID PRIMARY KEY,
    rank_name VARCHAR,
    created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TYPE shift_type AS ENUM ('Перша', 'Друга', 'Третя', 'Четверта', 'Відсутня');

ALTER TABLE tbl_system_users
    ADD COLUMN phone VARCHAR;

ALTER TABLE tbl_system_users
    ADD COLUMN rank_id UUID,

    ADD FOREIGN KEY (rank_id) REFERENCES tbl_ranks(id);

ALTER TABLE tbl_system_users
    ADD COLUMN shift shift_type NOT NULL DEFAULT 'Відсутня';

CREATE TABLE tbl_nabat_groups (
    id UUID PRIMARY KEY,
    unit_id UUID NOT NULL,
    group_name VARCHAR NOT NULL,
    created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (unit_id) REFERENCES tbl_units(id)
);

CREATE TABLE tbl_nabat (
    id UUID PRIMARY KEY,
    nabat_group_id UUID NOT NULL,
    user_id UUID NOT NULL,
    created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (nabat_group_id) REFERENCES tbl_nabat_groups(id),
    FOREIGN KEY (user_id) REFERENCES tbl_system_users(id)
);
