ALTER PLUGGABLE DATABASE &1 CLOSE;
DROP PLUGGABLE DATABASE &1 INCLUDING DATAFILES;
ALTER PLUGGABLE DATABASE &2 CLOSE;
DROP PLUGGABLE DATABASE &2 INCLUDING DATAFILES;
QUIT;