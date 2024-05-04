# LearningApp
Tracking of learning progress

## Milestone 1

W plikach `Barker_e1.pdf` oraz `Bachman_e1.pdf` znajduje się szkic, którym definiujemy model pojęciowy. Model w notacji Bachmana został tu umieszczony, żeby pokazać typy atrybutów.

### Usecase:

kategoria - etap (milestone) - zadanie

    Matura z matematyki -> Trygonometria, Stereometria, Kombinatoryka, ... -> Zadania dla milestone Trygonometria ( tw. cosinusow, ... )

    Python -> Typy Danych, Pętle, ... -> Zadania dla milestone Typy Danych ( słownik, lista, ... )

    Niemiecki -> Słownictwo, Strona Bierna, ... -> Zadania dla milestone Słownictwo ( słownictwo z rozdziału 1, ... )

W folderze `OracleDataModeler` zamieszczone są pliki umożliwiające otworzenie modelu w Oracle Data Modeler.

## Milestone 2

W plikach `Barker_e2.pdf` oraz `Bachman_e2.pdf` znajduje się zaktualizowany szkic.

# Instalacja: Windows

Uwaga: nazwy baz danych oraz loginu i hasła do nich można dostosować - w takim przypadku należy podmienić wartości w plikach `config.properties` i `setup.sql` na własne oraz inaczej nazwać foldery na nowe bazy. Instrukcja zakłada użycie domyślnych wartości.

Pobrać i zainstalować Javę w wersji co najmniej 21: https://www.oracle.com/java/technologies/downloads/

Pobrać i zainstalować bazę danych Oracle: https://www.oracle.com/database/technologies/oracle-database-software-downloads.html <br>
Przy instalacji Oracle trzeba zapamiętać wybrane hasło i ścieżkę instalacji - będą potrzebne później.
Dalsze instrukcje zakładają wybranie Oracle Database 21c XE i mogą nie działać dla innych wersji.

Wyszukać w instalowanych plikach folder pdbseed (jeżeli nie ma, można spróbować pdb$seed) i w folderze nadrzędnym (obok pcbseed) utworzyć dwa kolejne - `learning_app_db` i `learning_app_db_test`. Przekleić ścieżki do folderów na miejsce przykładowych w pliku `setup.sql`.

Otworzyć terminal (`cmd.exe`) i wpisać komendy: <br>
`cd C:\Users\kapic\IdeaProjects\LearningApp` (należy podać własną ścieżkę do folderu)<br>
`sqlplus system/password @setup.sql` (zamiast password - hasło podane przy instalacji Oracle)

Baza danych powinna być gotowa do pracy.

Plik jar można uruchomić kliknięciem lub terminalową komendą: `java -jar LearningApp-1.0-jar-with-dependencies.jar`



Jeżeli z jakiegoś powodu w przyszłości byłaby potrzeba usunąć bazy danych, można to zrobić, wpisując ponownie do konsoli `sqlplus system/password`, a potem podając następujące komendy:

`ALTER PLUGGABLE DATABASE learning_app_db CLOSE;` <br>
`DROP PLUGGABLE DATABASE learning_app_db INCLUDING DATAFILES;` <br>
`ALTER PLUGGABLE DATABASE learning_app_db CLOSE;` <br>
`DROP PLUGGABLE DATABASE learning_app_db INCLUDING DATAFILES;` <br>

W przypadku samodzielnej kompilacji można skorzystać z IDE (np. IntelliJ IDEA Community Edition: https://www.jetbrains.com/idea/download/?fromIDE=&section=windows) i znaleźć opcję "mvn package" lub samodzielnie zainstalować Mavena: https://phoenixnap.com/kb/install-maven-windows i w folderze z pom.xml uruchomić cmd i wpisać komendę `mvn package` - plik jar-with-dependencies pojawi się w folderze `target`.
