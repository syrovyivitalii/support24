CREATE TABLE tbl_notification_log (
    id UUID PRIMARY KEY,
    nabat_group_id UUID NOT NULL,
    notification_id UUID NOT NULL,
    message VARCHAR,
    json_request TEXT,
    json_response TEXT,
    notified_by_id UUID,
    created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    FOREIGN KEY (nabat_group_id) REFERENCES tbl_nabat_groups(id),
    FOREIGN KEY (notified_by_id) REFERENCES tbl_system_users(id)
);