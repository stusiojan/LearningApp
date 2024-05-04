ALTER SESSION SET DB_CREATE_FILE_DEST = 'C:\app\kapic\product\21c\oradata\XE\learning_app_db';
CREATE PLUGGABLE DATABASE learning_app_db ADMIN USER learning_app_user IDENTIFIED BY u49L9n#$2Wii;
ALTER SESSION SET CONTAINER = learning_app_db;
STARTUP;
@sql_files/create_tables.sql
@sql_files/add_procedures.sql

ALTER SESSION SET CONTAINER = cdb$root;

ALTER SESSION SET DB_CREATE_FILE_DEST = 'C:\app\kapic\product\21c\oradata\XE\learning_app_db_test';
CREATE PLUGGABLE DATABASE learning_app_db_test ADMIN USER learning_app_user IDENTIFIED BY u49L9n#$2Wii;
ALTER SESSION SET CONTAINER = learning_app_db_test;
STARTUP;
@sql_files/create_tables.sql
@sql_files/add_procedures.sql
@sql_files/test_data.sql
quit;