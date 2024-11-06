# A9_Buchhaltung

<a name="readme-top"></a>
Author: Ganea Marcus-Alin <br>
LBS Eibiswald | 2aAPC <br>
Erstellt am 06.11.2024

Diese Anwendung ist die neunte Übung vom Labor ITL12...<br>

## Das Ziel der Übung
### •	Erstelle eine einfache Haushaltskosten App (Einnahmen-/Ausgabenrechner) mit Datenbank
### •	Klassenstruktur planen
### •	Umsetzung in JAVA



<div>•	Wie sieht die Datenbankstruktur aus? <div/>
<div>•	Wie wird die Oberfläche gestaltet?<div/>
<div>•	Welche Klassen werden benötigt?<div/>
<div>•	Welche Methoden werden in der Klasse benötigt?<div/>
<div>•	ERROR Handling: Unbedingt auf mögliche Eingabefehler reagieren!<div/>
<div>o	https://www.jetbrains.com/help/idea/connecting-to-a-database.html#<div/>
<div>•	Vorgang beim Einbinden von Daten aus der Datenbank dokumentieren<div/>
<div>•	Sequenzielles Auslesen aller Datensätze aus einer Tabelle (Employee)<div/>



## Installation

```cmd

git checkout origin/master
```
## oder
```cmd
git clone https://github.com/MarcusGanea/A9_Buchhaltung
```
<p align="right">(<a href="#readme-top">back to top</a>)</p>

## Verwendung
Technologien im Einsatz:
[![Java][java.com]][java-url]


<p align="right">(<a href="#readme-top">back to top</a>)</p>

## Screenshots ausführung

### Klassen
![image](https://github.com/user-attachments/assets/f53b012f-a0b5-485a-9e24-96a420c890cc)

### Mariadb Connector zuerst heruntergeladen
![image](https://github.com/user-attachments/assets/ab5174ea-13e8-486e-8f56-007aced0deaa)

### Dannach in Project Structure mit IntelliJ verbunden
![image](https://github.com/user-attachments/assets/ccbfd622-49ac-49db-927d-bcd2b9f7558e)

### Den SQL-Code zum MariaDB geeigneten SQL-Code umgeschrieben und dann in PHPmyAdmin ausgeführt zum erstellen der DB
![image](https://github.com/user-attachments/assets/637c2a3d-6db0-4652-82f6-b6dbc696076a)

### Die Verbindung zur DB passiert auf der Main.java
```java
import java.sql.Connection;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        // Database connection URL
        String dbUrl = "jdbc:mariadb://127.0.0.1:3306/itl12_db";
        String username = "root";
        String userPassword = "";
        Scanner inputScanner = new Scanner(System.in);



        // Attempt to connect to the database
        try (Connection connection = Connector.getConnection(dbUrl, username, userPassword)) {
            if (connection != null) {
                System.out.println("Verbindung zur MariaDB erfolgreich hergestellt!");
                Tables.selectAllFromPersonTable(connection);
            } else {
                System.out.println("Verbindung fehlgeschlagen.");
            }
        } catch (Exception ex) {
            System.err.println("Fehler: " + ex.getMessage());
        }
    }
}
```

### Ausführung der Abfrage von IntilliJ
![image](https://github.com/user-attachments/assets/a8befb85-3062-4bd2-bf9f-a681fa08e87f)





<p align="right">(<a href="#readme-top">back to top</a>)</p>


<!-- MARKDOWN LINKS & IMAGES -->
<!-- https://www.markdownguide.org/basic-syntax/#reference-style-links -->
[java.com]: https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white
[java-url]: https://www.java.com/de/
[product-screenshot]: Screen.png
