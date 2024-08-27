CREATE OR REPLACE FUNCTION update_parent_task_status()
RETURNS TRIGGER AS $$
BEGIN
    -- Check if the updated task is a subtask
    IF NEW.parent_id IS NOT NULL THEN
        -- Check if all subtasks have status COMPLETED
        IF NOT EXISTS (
            SELECT 1
            FROM tbl_tasks
            WHERE parent_id = NEW.parent_id
            AND status != 'COMPLETED'::task_status
        ) THEN
            -- Update the parent task's status to COMPLETED
UPDATE tbl_tasks
SET status = 'COMPLETED'::task_status,
                updated_date = CURRENT_TIMESTAMP
WHERE id = NEW.parent_id;
END IF;
END IF;

RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_update_parent_status
    AFTER UPDATE OF status
    ON tbl_tasks
    FOR EACH ROW
    EXECUTE FUNCTION update_parent_task_status();
