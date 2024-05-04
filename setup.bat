set /p "oraclePassword=Oracle password (haslo): "

cd %~dp0
for /f "delims=" %%a in (config.properties) do set %%a

cd %oraclePath%
mkdir %databaseName%
mkdir %testDatabaseName%

cd %~dp0/sql_files
sqlplus system/%oraclePassword% as sysdba @setup.sql %databaseName% %testDatabaseName% %databaseUser% %databasePassword% %oraclePath%

pause