ALTER SESSION SET DB_CREATE_FILE_DEST = '&5&1';
CREATE PLUGGABLE DATABASE &1 ADMIN USER &3 IDENTIFIED BY &4;
ALTER SESSION SET CONTAINER = &1;
STARTUP;
GRANT ALL PRIVILEGES TO &3;

ALTER SESSION SET CONTAINER = cdb$root;

ALTER SESSION SET DB_CREATE_FILE_DEST = '&5&2';
CREATE PLUGGABLE DATABASE &2 ADMIN USER &3 IDENTIFIED BY &4;
ALTER SESSION SET CONTAINER = &2;
STARTUP;
GRANT ALL PRIVILEGES TO &3;

ALTER SESSION SET CONTAINER = cdb$root;
ALTER PLUGGABLE DATABASE ALL SAVE STATE;

CONNECT &3/&4@localhost:1521/&1;
@create_tables.sql
@add_procedures.sql

CONNECT &3/&4@localhost:1521/&2;
@create_tables.sql
@add_procedures.sql
@test_data.sql

QUIT;