ALTER SESSION SET NLS_DATE_FORMAT = 'YYYY/MM/DD HH24:MI:SS';



INSERT INTO users (login, hash, salt) VALUES ('user1', 'e308ccb52a8d432a3bf80fdf727e6dedbc6d893906d81ed7e3907d282c13fa0d', '12345678abcdefgh');
    INSERT INTO categories (cat_name, cat_description, cat_tasks_done) VALUES ('Niemiecki', 'Język niemiecki', 0);

    INSERT INTO categories (cat_name, cat_description, cat_tasks_done) VALUES ('Python', NULL, 3);
        INSERT INTO milestones (mil_name, date_added, deadline, mil_completed, mil_description, mil_tasks_done, user_id, cat_id) VALUES ('Podstawy języka', TO_DATE('2024/02/02'), TO_DATE('2024/03/02'), TO_DATE('2024/02/28'), 'Najważniejsze elementy języka', 3, 1, 2);
            INSERT INTO tasks (task_name, task_completed, task_description, mil_id) VALUES ('Typy danych',          TO_DATE('2024/02/04'), 'Ciągi znaków, liczby całkowite i zmiennoprzecinkowe', 1);
            INSERT INTO tasks (task_name, task_completed, task_description, mil_id) VALUES ('Pętle',                TO_DATE('2024/02/11'), 'Pętle for i while',                                   1);
            INSERT INTO tasks (task_name, task_completed, task_description, mil_id) VALUES ('Instrukcje warunkowe', TO_DATE('2024/02/28'), NULL,                                                  1);

        INSERT INTO milestones (mil_name, date_added, deadline, mil_completed, mil_description, mil_tasks_done, user_id, cat_id) VALUES ('Programowanie obiektowe', sysdate, sysdate, sysdate, '', 0, 1, 2);



INSERT INTO users (login, hash, salt) VALUES ('user2', '22957f4668107d34db8e6aed36b4610091e12e311c44ef38fe37ed7a735d17e9', '1234567890123456');



INSERT INTO users (login, hash, salt) VALUES ('user3', 'd7111d2b45efda000f1d09a014f4d5763d5981f32126541ef7dda8e46c7fdf15', 'a1b2c3d4e5f6g7h8');
    INSERT INTO categories (cat_name, cat_description, cat_tasks_done) VALUES ('Bazy Danych 2', 'Projektowanie aplikacji z bazami danych przy użyciu zaawansowanych mechanizmów', 6);
        INSERT INTO milestones (mil_name, date_added, deadline, mil_completed, mil_description, mil_tasks_done, user_id, cat_id) VALUES ('Egzamin', TO_DATE('2024/02/26 20:11:34'), TO_DATE('2024/06/17'), TO_DATE('2024/03/02 21:24:20'), 'Egzamin - wszystkie wykłady', 6, 3, 3);
            INSERT INTO tasks (task_name, task_completed, task_description, mil_id) VALUES ('Wykład - wprowadzenie',     TO_DATE('2024/03/01 20:17:34'), NULL, 3);
            INSERT INTO tasks (task_name, task_completed, task_description, mil_id) VALUES ('Wykład - model pojęciowy',  TO_DATE('2024/03/01 20:17:38'), NULL, 3);
            INSERT INTO tasks (task_name, task_completed, task_description, mil_id) VALUES ('Wykład - model relacyjny',  TO_DATE('2024/03/01 20:17:43'), NULL, 3);
            INSERT INTO tasks (task_name, task_completed, task_description, mil_id) VALUES ('Wykład - hurtownie danych', TO_DATE('2024/03/02 21:24:02'), NULL, 3);
            INSERT INTO tasks (task_name, task_completed, task_description, mil_id) VALUES ('Wykład - model fizyczny',   TO_DATE('2024/03/02 21:24:13'), NULL, 3);
            INSERT INTO tasks (task_name, task_completed, task_description, mil_id) VALUES ('Wykład - NoSQL',            TO_DATE('2024/03/02 21:24:20'), NULL, 3);

        INSERT INTO milestones (mil_name, date_added, deadline, mil_completed, mil_description, mil_tasks_done, user_id, cat_id) VALUES ('Projekt', TO_DATE('2024/02/26 20:11:54'), TO_DATE('2024/06/24'), TO_DATE('2024/02/26 20:11:54'), 'Projekt - baza danych z aplikacją', 0, 3, 3);
            INSERT INTO tasks (task_name, task_completed, task_description, mil_id) VALUES ('Frontend panelu kategorii', NULL,                           NULL, 4);
            INSERT INTO tasks (task_name, task_completed, task_description, mil_id) VALUES ('Frontend panelu logowania', NULL,                           NULL, 4);
            INSERT INTO tasks (task_name, task_completed, task_description, mil_id) VALUES ('Przetwarzanie ResultSetów', NULL,                           NULL, 4);

    INSERT INTO categories (cat_name, cat_description, cat_tasks_done) VALUES ('Sztuka wytwarzania oprogramowania', 'Poszerzanie wiedzy umożliwiającej pisanie lepszego kodu', 7);
        INSERT INTO milestones (mil_name, date_added, deadline, mil_completed, mil_description, mil_tasks_done, user_id, cat_id) VALUES ('Kolokwium 1', TO_DATE('2024/02/26 20:21:54'), TO_DATE('2024/04/15'), NULL, NULL, 5, 3, 4);
            INSERT INTO tasks (task_name, task_completed, task_description, mil_id) VALUES ('Wykład - wstęp',                             TO_DATE('2024/02/19 21:37:00'), 'Potok wytwarzania oprogramowania, narzędzia, OOP, kompozycja, dziedziczenie', 5);
            INSERT INTO tasks (task_name, task_completed, task_description, mil_id) VALUES ('Wykład - standardy kodowania i SOLID',       TO_DATE('2024/02/26 20:20:10'), NULL,                                                                          5);
            INSERT INTO tasks (task_name, task_completed, task_description, mil_id) VALUES ('Wykład - obiektowe wzorce projektowe 1',     TO_DATE('2024/03/04 19:24:20'), NULL,                                                                          5);
            INSERT INTO tasks (task_name, task_completed, task_description, mil_id) VALUES ('Wykład - obiektowe wzorce projektowe 2',     TO_DATE('2024/03/18 21:54:20'), NULL,                                                                          5);
            INSERT INTO tasks (task_name, task_completed, task_description, mil_id) VALUES ('Wykład - UML',                               TO_DATE('2024/03/25 23:24:05'), 'Diagram klas, diagram sekwencji',                                             5);
            INSERT INTO tasks (task_name, task_completed, task_description, mil_id) VALUES ('Wykład - cykl wytwarzania oprogramowania 1', NULL,                           NULL,                                                                          5);
            INSERT INTO tasks (task_name, task_completed, task_description, mil_id) VALUES ('Wykład - cykl wytwarzania oprogramowania 2', NULL,                           NULL,                                                                          5);

        INSERT INTO milestones (mil_name, date_added, deadline, mil_completed, mil_description, mil_tasks_done, user_id, cat_id) VALUES ('Kolokwium 2', TO_DATE('2024/02/26 20:21:58'), TO_DATE('2024/06/03'), NULL, NULL, 2, 3, 4);
            INSERT INTO tasks (task_name, task_completed, task_description, mil_id) VALUES ('Wykład - ocena jakości kodu i testów',       TO_DATE('2024/04/22'),          'Analiza statyczna kodu, metryki',                                             6);
            INSERT INTO tasks (task_name, task_completed, task_description, mil_id) VALUES ('Wykład - standardy kodowania i SOLID',       TO_DATE('2024/05/06'),          NULL,                                                                          6);
            INSERT INTO tasks (task_name, task_completed, task_description, mil_id) VALUES ('Wykład - monitorowanie i analiza aplikacji', NULL,                           NULL,                                                                          6);
            INSERT INTO tasks (task_name, task_completed, task_description, mil_id) VALUES ('Wykład - refaktoring',                       NULL,                           'Praca z kodem zastanym',                                                      6);
            INSERT INTO tasks (task_name, task_completed, task_description, mil_id) VALUES ('Wykład - współbieżne wzorce 1',              NULL,                           NULL,                                                                          6);
            INSERT INTO tasks (task_name, task_completed, task_description, mil_id) VALUES ('Wykład - współbieżne wzorce 2',              NULL,                           NULL,                                                                          6);


    INSERT INTO categories (cat_name, cat_description, cat_tasks_done) VALUES ('Systemy komputerowe w sterowaniu i pomiarach', 'Praca z mikrokontrolerami i systemami czasu rzeczywistego', 6);
        INSERT INTO milestones (mil_name, date_added, deadline, mil_completed, mil_description, mil_tasks_done, user_id, cat_id) VALUES ('Kolokwium 1', TO_DATE('2024/02/26 20:22:12'), TO_DATE('2024/04/08'), TO_DATE('2024/03/25 23:24:05'), NULL, 6, 3, 5);
            INSERT INTO tasks (task_name, task_completed, task_description, mil_id) VALUES ('Wykład - wstęp',                                TO_DATE('2024/02/19 21:37:00'), NULL, 7);
            INSERT INTO tasks (task_name, task_completed, task_description, mil_id) VALUES ('Wykład - Buildroot',                            TO_DATE('2024/02/26 20:20:10'), NULL, 7);
            INSERT INTO tasks (task_name, task_completed, task_description, mil_id) VALUES ('Wykład - OpenWRT',                              TO_DATE('2024/03/04 19:24:20'), NULL, 7);
            INSERT INTO tasks (task_name, task_completed, task_description, mil_id) VALUES ('Wykład - wprowadzenie do systemów embedded',    TO_DATE('2024/03/11 21:54:20'), NULL, 7);
            INSERT INTO tasks (task_name, task_completed, task_description, mil_id) VALUES ('Wykład - software systemów wbudowanych',        TO_DATE('2024/03/18 23:24:05'), NULL, 7);
            INSERT INTO tasks (task_name, task_completed, task_description, mil_id) VALUES ('Wykład - projektowanie systemów wbudowanych',   TO_DATE('2024/03/25 23:24:05'), NULL, 7);

        INSERT INTO milestones (mil_name, date_added, deadline, mil_completed, mil_description, mil_tasks_done, user_id, cat_id) VALUES ('Kolokwium 2', TO_DATE('2024/02/26 20:22:19'), TO_DATE('2024/06/03'), TO_DATE('2024/02/26 20:22:19'), NULL, 0, 3, 5);

    INSERT INTO categories (cat_name, cat_description, cat_tasks_done) VALUES ('Wprowadzenie do systemów zarządzania', 'Systemy zarządzania, programowanie liniowe oraz modelowanie procesów biznesowych i harmonogramów', 0);

    INSERT INTO categories (cat_name, cat_description, cat_tasks_done) VALUES ('Bezpieczeństwo systemów i sieci', 'Informacje z zakresu kryptografii i cyberbezpieczeństwa', 10);
        INSERT INTO milestones (mil_name, date_added, deadline, mil_completed, mil_description, mil_tasks_done, user_id, cat_id) VALUES ('Egzamin', TO_DATE('2024/02/26 20:22:52'), TO_DATE('2024/06/29'), NULL, NULL, 10, 3, 7);
            INSERT INTO tasks (task_name, task_completed, task_description, mil_id) VALUES ('Wykład - wstęp',                             TO_DATE('2024/02/22'), 'Zakres przedmiotu',                                                  9);
            INSERT INTO tasks (task_name, task_completed, task_description, mil_id) VALUES ('Wykład - szyfry',                            TO_DATE('2024/02/29'), 'Szyfry symetryczne i asymetryczne',                                  9);
            INSERT INTO tasks (task_name, task_completed, task_description, mil_id) VALUES ('Wykład - funkcje skrótu',                    TO_DATE('2024/03/07'), 'MAC, SHA',                                                           9);
            INSERT INTO tasks (task_name, task_completed, task_description, mil_id) VALUES ('Wykład - PKI',                               TO_DATE('2024/03/14'), 'Certyfikaty i GPG',                                                  9);
            INSERT INTO tasks (task_name, task_completed, task_description, mil_id) VALUES ('Wykład - zagrożenia sieciowe',               TO_DATE('2024/03/21'), 'Exploity i wyszukiwanie informacji o sieci',                         9);
            INSERT INTO tasks (task_name, task_completed, task_description, mil_id) VALUES ('Wykład - systemy zabezpieczające sieć',      TO_DATE('2024/03/28'), 'Intrusion Detection/Protection System',                              9);
            INSERT INTO tasks (task_name, task_completed, task_description, mil_id) VALUES ('Wykład - bezpieczeństwo aplikacji C i C++',  TO_DATE('2024/04/04'), 'Buffer i heap overflow',                                             9);
            INSERT INTO tasks (task_name, task_completed, task_description, mil_id) VALUES ('Wykład - malware 1',                         TO_DATE('2024/04/11'), 'Typy malware',                                                       9);
            INSERT INTO tasks (task_name, task_completed, task_description, mil_id) VALUES ('Wykład - malware 2',                         TO_DATE('2024/04/18'), 'Słynne ataki hakerskie',                                             9);
            INSERT INTO tasks (task_name, task_completed, task_description, mil_id) VALUES ('Wykład - bezpieczeństwo aplikacji webowych', TO_DATE('2024/04/25'), 'XSS i CSRF',                                                         9);
            INSERT INTO tasks (task_name, task_completed, task_description, mil_id) VALUES ('Wykład - VPN, bezpieczeństwo OS',            NULL,                  'Kontrola dostępu w systemach operacyjnych',                          9);
            INSERT INTO tasks (task_name, task_completed, task_description, mil_id) VALUES ('Wykład - bezpieczeństwo systemów ICS',       NULL,                  'Zagrożenia dla systemów przemysłowych i ich zwalczanie',             9);
            INSERT INTO tasks (task_name, task_completed, task_description, mil_id) VALUES ('Wykład - logowanie i monitorowanie OS',      NULL,                  'Logowanie i monitorowanie na Windowsie i Linuxie',                   9);
            INSERT INTO tasks (task_name, task_completed, task_description, mil_id) VALUES ('Wykład - systemy HoneyPot',                  NULL,                  'Systemy wabiące hakerów',                                            9);
            INSERT INTO tasks (task_name, task_completed, task_description, mil_id) VALUES ('Wykład - polityka bezpieczeństwa',           NULL,                  'Reguły bezpieczeństwa, reagowanie na incydenty',                     9);


COMMIT;
