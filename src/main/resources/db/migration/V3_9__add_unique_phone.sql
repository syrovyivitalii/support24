ALTER TABLE support24.tbl_phones
    ADD CONSTRAINT unique_user_phone UNIQUE (user_id, phone);