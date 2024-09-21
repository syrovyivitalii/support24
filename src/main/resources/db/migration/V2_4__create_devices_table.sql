CREATE TYPE device_type AS ENUM ('Мережевий', 'Радіотехнічний', 'Периферійний', 'Компютерний', 'Звуковий');

CREATE TABLE tbl_devices (
    id UUID PRIMARY KEY,
    device_name VARCHAR NOT NULL,
    device_type device_type NOT NULL DEFAULT 'Компютерний',
    inventory_number VARCHAR NOT NULL,
    decree_number VARCHAR,
    mac_adress VARCHAR,
    ip_adress VARCHAR,
    production_year INT,
    note VARCHAR,
    unit_id UUID NOT NULL,
    created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (unit_id) REFERENCES tbl_units(id)
);
