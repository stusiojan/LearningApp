BEGIN
    EXECUTE IMMEDIATE 'DROP TABLE users CASCADE CONSTRAINTS PURGE';
    EXECUTE IMMEDIATE 'DROP TABLE categories CASCADE CONSTRAINTS PURGE';
    EXECUTE IMMEDIATE 'DROP TABLE milestones CASCADE CONSTRAINTS PURGE';
    EXECUTE IMMEDIATE 'DROP TABLE tasks CASCADE CONSTRAINTS PURGE';
EXCEPTION
    WHEN OTHERS THEN
        IF SQLCODE != -942 THEN RAISE;
    END IF;
END;
/

CREATE TABLE users
(
    user_id          INTEGER         GENERATED BY DEFAULT ON NULL AS IDENTITY CONSTRAINT user_pk PRIMARY KEY,
    login            VARCHAR2 (20)   NOT NULL CONSTRAINT login_uni UNIQUE,
    hash             VARCHAR2 (64)   NOT NULL,
    salt             VARCHAR2 (16)   NOT NULL
);

CREATE TABLE categories 
(
    cat_id           INTEGER         GENERATED BY DEFAULT ON NULL AS IDENTITY CONSTRAINT cat_pk PRIMARY KEY,
    cat_name         VARCHAR2 (50)   NOT NULL,
    cat_description  VARCHAR2 (200),
    cat_tasks_all    INTEGER         DEFAULT 0 NOT NULL,
    cat_tasks_done   INTEGER         DEFAULT 0 NOT NULL
);

CREATE TABLE milestones
(
    mil_id           INTEGER         GENERATED BY DEFAULT ON NULL AS IDENTITY CONSTRAINT mil_pk PRIMARY KEY,
    mil_name         VARCHAR2 (50)   NOT NULL,
    date_added       DATE            NOT NULL,
    deadline         DATE            NOT NULL,
    mil_completed    DATE,
    mil_description  VARCHAR2 (200),
    mil_tasks_all    INTEGER         DEFAULT 0 NOT NULL,
    mil_tasks_done   INTEGER         DEFAULT 0 NOT NULL,
    user_id          INTEGER         NOT NULL,
    cat_id           INTEGER         NOT NULL
);

ALTER TABLE milestones ADD CONSTRAINT mil_name_uni UNIQUE(mil_name, cat_id, user_id);

ALTER TABLE milestones ADD CONSTRAINT user_fk FOREIGN KEY (user_id) REFERENCES users (user_id)     ON DELETE CASCADE;
ALTER TABLE milestones ADD CONSTRAINT cat_fk  FOREIGN KEY (cat_id)  REFERENCES categories (cat_id) ON DELETE CASCADE;

CREATE TABLE tasks
(
    task_id          INTEGER         GENERATED BY DEFAULT ON NULL AS IDENTITY CONSTRAINT task_pk PRIMARY KEY,
    task_name        VARCHAR2 (50)   NOT NULL,
    task_completed   DATE,
    task_description VARCHAR2 (200),
    mil_id           INTEGER         NOT NULL
);

ALTER TABLE tasks ADD CONSTRAINT task_name_uni UNIQUE (task_name, mil_id);

ALTER TABLE tasks ADD CONSTRAINT mil_fk FOREIGN KEY (mil_id) REFERENCES milestones (mil_id) ON DELETE CASCADE;

COMMIT;
