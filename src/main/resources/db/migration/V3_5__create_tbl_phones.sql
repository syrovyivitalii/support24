ALTER TABLE tbl_system_users DROP COLUMN phone;

CREATE TYPE phone_statuses AS ENUM ('ACTIVE', 'NONACTIVE');
CREATE TYPE phone_types AS ENUM ('Робочий', 'Домашній','Мобільний','ІР');

CREATE TABLE tbl_phones (
    id UUID PRIMARY KEY,
    user_id UUID NOT NULL,
    phone VARCHAR NOT NULL,
    phone_type phone_types NOT NULL DEFAULT 'Мобільний',
    created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    status phone_statuses NOT NULL DEFAULT 'ACTIVE'
);