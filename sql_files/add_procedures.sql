CREATE OR REPLACE FUNCTION get_user (arg_login users.login%TYPE)
RETURN users.salt%TYPE
AS
    result users.salt%TYPE;
BEGIN
    SELECT salt INTO result FROM users WHERE login = arg_login;
    RETURN result;
EXCEPTION WHEN no_data_found THEN 
    RETURN 'EMPTY';
END;
/

CREATE OR REPLACE FUNCTION task_count(offset_date DATE)
RETURN tasks.task_id%TYPE
AS
    result tasks.task_id%TYPE;
BEGIN
    SELECT COUNT(*) INTO result 
    FROM   tasks NATURAL JOIN milestones
    WHERE  task_completed IS NULL AND offset_date > deadline;
    
    RETURN result;
END;
/

CREATE OR REPLACE FUNCTION task_count_overdue RETURN tasks.task_id%TYPE AS BEGIN RETURN task_count(sysdate);                END;
/
CREATE OR REPLACE FUNCTION task_count_week    RETURN tasks.task_id%TYPE AS BEGIN RETURN task_count(sysdate + 7);            END;
/
CREATE OR REPLACE FUNCTION task_count_month   RETURN tasks.task_id%TYPE AS BEGIN RETURN task_count(ADD_MONTHS(sysdate, 1)); END;
/

CREATE OR REPLACE PROCEDURE update_completion (arg_id milestones.mil_id%TYPE)
AS
    mst_old_done  milestones.mil_tasks_done%TYPE;
    mst_old_count milestones.mil_tasks_all%TYPE;
    mst_completed milestones.mil_completed%TYPE;
BEGIN
    SELECT mil_tasks_done, mil_tasks_all, mil_completed
    INTO   mst_old_done,   mst_old_count, mst_completed
    FROM   milestones
    WHERE  mil_id = arg_id;
    
    IF mst_old_done = mst_old_count AND mst_completed IS     NULL THEN
        UPDATE milestones SET mil_completed = sysdate WHERE mil_id = arg_id;
    END IF;
    IF mst_old_done < mst_old_count AND mst_completed IS NOT NULL THEN
        UPDATE milestones SET mil_completed = NULL    WHERE mil_id = arg_id;
    END IF;
END;
/

CREATE OR REPLACE TRIGGER tg_new_task
AFTER INSERT ON tasks FOR EACH ROW
DECLARE
    mst_old_count milestones.mil_tasks_all%TYPE;
    ctg_id        categories.cat_id%TYPE;
    ctg_old_count categories.cat_tasks_all%TYPE;
BEGIN
    SELECT mil_tasks_all, cat_id, cat_tasks_all
    INTO   mst_old_count, ctg_id, ctg_old_count
    FROM   milestones NATURAL JOIN categories
    WHERE  mil_id = :new.mil_id;
    
    UPDATE milestones SET mil_tasks_all = 1 + mst_old_count WHERE mil_id = :new.mil_id;
    UPDATE categories SET cat_tasks_all = 1 + ctg_old_count WHERE cat_id = ctg_id;
    update_completion(:new.mil_id);
END;
/

CREATE OR REPLACE TRIGGER tg_del_task
BEFORE DELETE ON tasks FOR EACH ROW
DECLARE
    mst_old_done  milestones.mil_tasks_done%TYPE;
    mst_old_count milestones.mil_tasks_all%TYPE;
    ctg_id        categories.cat_id%TYPE;
    ctg_old_done  categories.cat_tasks_done%TYPE;
    ctg_old_count categories.cat_tasks_all%TYPE;
BEGIN
    SELECT mil_tasks_done, mil_tasks_all, cat_id, cat_tasks_done, cat_tasks_all
    INTO   mst_old_done,   mst_old_count, ctg_id, ctg_old_done,   ctg_old_count
    FROM   milestones NATURAL JOIN categories
    WHERE  mil_id = :old.mil_id;
    
    UPDATE milestones SET mil_tasks_all = mst_old_count - 1 WHERE mil_id = :old.mil_id;
    UPDATE categories SET cat_tasks_all = ctg_old_count - 1 WHERE cat_id = ctg_id;
    
    IF :old.task_completed IS NOT NULL THEN
        UPDATE milestones SET mil_tasks_done = mst_old_done - 1 WHERE mil_id = :old.mil_id;
        UPDATE categories SET cat_tasks_done = ctg_old_done - 1 WHERE cat_id = ctg_id;
    END IF;
    update_completion(:old.mil_id);
END;
/

CREATE OR REPLACE PROCEDURE switch_task_done (arg_id tasks.task_id%TYPE)
AS
    mst_id        milestones.mil_id%TYPE;
    mst_old_done  milestones.mil_tasks_done%TYPE;
    mst_old_count milestones.mil_tasks_all%TYPE;
    ctg_id        categories.cat_id%TYPE;
    ctg_old_done  categories.cat_tasks_done%TYPE;
    ctg_old_count categories.cat_tasks_all%TYPE;
    tsk_completed tasks.task_completed%TYPE;
BEGIN
    SELECT mil_id, mil_tasks_done, mil_tasks_all, cat_id, cat_tasks_done, cat_tasks_all, task_completed
    INTO   mst_id, mst_old_done,   mst_old_count, ctg_id, ctg_old_done,   ctg_old_count, tsk_completed
    FROM   tasks NATURAL JOIN milestones NATURAL JOIN categories
    WHERE  task_id = arg_id;
    
    IF tsk_completed IS NULL THEN
        UPDATE tasks      SET task_completed = sysdate          WHERE task_id = arg_id;
        UPDATE milestones SET mil_tasks_done = mst_old_done + 1 WHERE mil_id  = mst_id;
        UPDATE categories SET cat_tasks_done = ctg_old_done + 1 WHERE cat_id  = ctg_id;
    ELSE
        UPDATE tasks      SET task_completed = NULL             WHERE task_id = arg_id;
        UPDATE milestones SET mil_tasks_done = mst_old_done - 1 WHERE mil_id = mst_id;
        UPDATE categories SET cat_tasks_done = ctg_old_done - 1 WHERE cat_id = ctg_id;
    END IF;
    update_completion(mst_id);
END;
/

CREATE OR REPLACE PROCEDURE create_empty_milestone (arg_name milestones.mil_name%TYPE, arg_user users.user_id%TYPE, arg_category categories.cat_id%TYPE) AS
BEGIN
    INSERT INTO milestones (mil_name, date_added, deadline, mil_completed, user_id, cat_id) VALUES (arg_name, sysdate, sysdate, sysdate, arg_user, arg_category);
END;
/
