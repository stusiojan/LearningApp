set /p "oraclePassword=Oracle password (haslo): "

cd %~dp0
for /f "delims=" %%a in (config.properties) do set %%a
type nul > "temp.txt"

sqlplus system/password as sysdba @%~dp0sql_files\get_oracle_home.sql %~dp0/temp.txt

for /f "delims=" %%a in (temp.txt) do set oraclePath=%%a\oradata\& goto :next
:next
del "temp.txt"

mkdir %oraclePath%%databaseName%
mkdir %oraclePath%%testDatabaseName%

cd %~dp0/sql_files
sqlplus system/%oraclePassword% as sysdba @%~dp0sql_files\setup.sql %databaseName% %testDatabaseName% %databaseUser% %databasePassword% %oraclePath%

pause