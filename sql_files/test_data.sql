ALTER SESSION SET NLS_DATE_FORMAT = 'YYYY/MM/DD HH24:MI:SS';

INSERT INTO users (login, hash, salt) VALUES ('user1', 'AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA', '12345678abcdefgh');
    INSERT INTO categories (cat_name, cat_description) VALUES ('Niemiecki', 'Język niemiecki');
    INSERT INTO categories (cat_name, cat_description) VALUES ('Python', NULL);
        INSERT INTO milestones (mil_name, date_added, deadline, mil_completed, mil_description, user_id, cat_id) VALUES ('Podstawy języka', TO_DATE('2024/02/02'), TO_DATE('2024/03/02'), TO_DATE('2024/02/28'), 'Najważniejsze elementy języka', 1, 2);
            INSERT INTO tasks (task_name, task_completed, task_description, mil_id) VALUES ('Typy danych', TO_DATE('2024/02/04'), 'Ciągi znaków, liczby całkowite i zmiennoprzecinkowe', 1);
            INSERT INTO tasks (task_name, task_completed, task_description, mil_id) VALUES ('Pętle', TO_DATE('2024/02/11'), 'Pętle for i while', 1);
            INSERT INTO tasks (task_name, task_completed, task_description, mil_id) VALUES ('Instrukcje warunkowe', TO_DATE('2024/02/28'), NULL, 1);      
        INSERT INTO milestones (mil_name, date_added, deadline, mil_completed, mil_description, user_id, cat_id) VALUES ('Programowanie obiektowe', sysdate, sysdate, sysdate, '', 1, 2);

INSERT INTO users (login, hash, salt) VALUES ('user2', 'AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA', '1234567890123456');


INSERT INTO users (login, hash, salt) VALUES ('user3', 'AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA', 'a1b2c3d4e5f6g7h8');
    INSERT INTO categories (cat_name, cat_description) VALUES ('Bazy Danych 2', 'Projektowanie aplikacji z bazami danych przy użyciu zaawansowanych mechanizmów');
        INSERT INTO milestones (mil_name, date_added, deadline, mil_completed, mil_description, user_id, cat_id) VALUES ('Egzamin', sysdate, TO_DATE('2024/06/17'), TO_DATE('2024/03/02 21:24:20'), 'Egzamin - wszystkie wykłady', 3, 1);
            INSERT INTO tasks (task_name, task_completed, task_description, mil_id) VALUES ('Wykład - wprowadzenie', TO_DATE('2024/03/01 20:17:34'), NULL, 3);
            INSERT INTO tasks (task_name, task_completed, task_description, mil_id) VALUES ('Wykład - model pojęciowy', TO_DATE('2024/03/01 20:17:38'), NULL, 3);
            INSERT INTO tasks (task_name, task_completed, task_description, mil_id) VALUES ('Wykład - model relacyjny', TO_DATE('2024/03/01 20:17:43'), NULL, 3);
            INSERT INTO tasks (task_name, task_completed, task_description, mil_id) VALUES ('Wykład - hurtownie danych', TO_DATE('2024/03/02 21:24:02'), NULL, 3);
            INSERT INTO tasks (task_name, task_completed, task_description, mil_id) VALUES ('Wykład - model fizyczny', TO_DATE('2024/03/02 21:24:13'), NULL, 3);
            INSERT INTO tasks (task_name, task_completed, task_description, mil_id) VALUES ('Wykład - NoSQL', TO_DATE('2024/03/02 21:24:20'), NULL, 3);
        INSERT INTO milestones (mil_name, date_added, deadline, mil_completed, mil_description, user_id, cat_id) VALUES ('Projekt', sysdate, TO_DATE('2024/05/20'), sysdate, 'Zrobić resztę', 3, 1);
    INSERT INTO categories (cat_name, cat_description) VALUES ('Sztuka wytwarzania oprogramowania', 'Poszerzanie wiedzy umożliwiającej pisanie lepszego kodu');
    INSERT INTO categories (cat_name, cat_description) VALUES ('Systemy komputerowe w sterowaniu i pomiarach', 'Praca z mikrokontrolerami i systemami czasu rzeczywistego');
    INSERT INTO categories (cat_name, cat_description) VALUES ('Wstęp do multimediów', 'Informacje z zakresu sygnałów, kodowania, wizji, dźwięku i obrazu');
    INSERT INTO categories (cat_name, cat_description) VALUES ('Wprowadzenie do systemów zarządzania', 'Systemy zarządzania, programowanie liniowe oraz modelowanie procesów biznesowych i harmonogramów');
    INSERT INTO categories (cat_name, cat_description) VALUES ('Bezpieczeństwo systemów i sieci', 'Informacje z zakresu kryptografii i cyberbezpieczeństwa');


COMMIT;
