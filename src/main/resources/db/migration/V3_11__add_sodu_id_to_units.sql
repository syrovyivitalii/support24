-- Step 1: Add the column as nullable
ALTER TABLE tbl_units ADD COLUMN sodu_id INT;

-- Step 2: Update existing rows with a default value (replace 'your_default_value' accordingly)
UPDATE tbl_units SET sodu_id = 1 WHERE sodu_id IS NULL;

-- Step 3: Alter the column to NOT NULL
ALTER TABLE tbl_units ALTER COLUMN sodu_id SET NOT NULL;
