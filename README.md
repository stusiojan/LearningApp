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

Uwaga: nazwy baz danych oraz loginu i hasła do nich można dostosować - w takim przypadku należy podmienić wartości w pliku `config.properties`. Hasło należy zacząć literą i unikać znaku `%`. Instrukcja zakłada użycie domyślnych wartości.

Pobrać i zainstalować Javę w wersji co najmniej 21: https://www.oracle.com/java/technologies/downloads/

Pobrać i zainstalować bazę danych Oracle: https://www.oracle.com/database/technologies/oracle-database-software-downloads.html <br>
Instalacja była testowana na Oracle Database 21c XE i EE. Przy instalacji Oracle trzeba zapamiętać wybrane hasło - będzie potrzebne później.
W przypadku instalacji na EE należy pozostawić domyślne opcje i wybrać hasło.

Uruchomić plik `setup.bat` - skrypt poprosi o hasło. Baza danych powinna być gotowa do pracy. Jeżeli zajdzie potrzeba jej usunięcia, można skorzystać ze skryptu `drop.bat`.

Plik jar można uruchomić kliknięciem lub terminalową komendą: `java -jar LearningApp-1.0-jar-with-dependencies.jar`

W przypadku samodzielnej kompilacji można skorzystać z IDE (np. IntelliJ IDEA Community Edition: https://www.jetbrains.com/idea/download/?fromIDE=&section=windows) i znaleźć opcję "mvn package". <br>
Można też samodzielnie zainstalować Mavena: https://phoenixnap.com/kb/install-maven-windows i w folderze z pom.xml uruchomić cmd i wpisać komendę `mvn package` - plik `...-jar-with-dependencies` pojawi się w folderze `target`.
