ALTER SESSION SET NLS_DATE_FORMAT = 'YYYY/MM/DD';

INSERT INTO categories (cat_name, cat_description) VALUES ('Bazy Danych 2', 'Projektowanie aplikacji z bazami danych przy użyciu zaawansowanych mechanizmów');
INSERT INTO users (login, hash, salt) VALUES ('Kacper', 'AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA', 'AAAAAAAAAAAAAAAA');
INSERT INTO milestones (mil_name, date_added, deadline, mil_completed, mil_description, user_id, cat_id) VALUES ('Model fizyczny', sysdate, TO_DATE('2024/05/20'), NULL, 'Zrobić resztę', 1, 1);
INSERT INTO tasks (task_name, task_completed, task_description, mil_id) VALUES ('Postawienie bazy danych', NULL, 'Napisać zapytania tworzące bazę danych i wstawiające minimalną ilość danych', 1);

COMMIT;
