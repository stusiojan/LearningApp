set /p "oraclePassword=Oracle password (haslo): "

cd %~dp0
for /f "delims=" %%a in (config.properties) do set %%a

cd sql_files
sqlplus system/%oraclePassword% as sysdba @drop.sql %databaseName% %testDatabaseName%

pause