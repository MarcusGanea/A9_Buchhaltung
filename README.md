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
<br><br>
[![Java][java.com]][java-url]


<p align="right">(<a href="#readme-top">back to top</a>)</p>

## Screenshots ausführung

### Klassen
![image](https://github.com/user-attachments/assets/1c983bdd-c8e6-4346-91bb-4c3cde615a33)


### Das Hauptfenster
![image](https://github.com/user-attachments/assets/4fa3f3ae-2e9a-471c-b011-c93db605bf9f)

### Eine Buchung eingetragen
![image](https://github.com/user-attachments/assets/18d1db10-2ede-46ef-826e-21e5ecf43b30)

### Wenn man ohne das ausfüllen der Felder bestätigt
![image](https://github.com/user-attachments/assets/d4c3f2aa-27f6-46bb-8c11-81c30777bd41)


### Man kann einen Eintrag mit dem Löschen Button oder mit Rechtsklick->löschen einen Eintag löschen
![image](https://github.com/user-attachments/assets/7b5da211-ef3e-4d00-b38b-faa2e72f3a20)

### Aber es kann nur am gleichen Tag gelöscht werden an dem man es erstellt hat sonnst wird es nicht erlaubt
![image](https://github.com/user-attachments/assets/8a176171-2620-4042-ae9e-5fae1f818183)


### Man kann auch nach jeder einzelnen Spalte sortieren
![image](https://github.com/user-attachments/assets/efad9fe1-cc40-41fe-8968-6c186b71edb1)

### Kategorien kann man in Settings hinzufügen
![image](https://github.com/user-attachments/assets/3a65c36b-86a5-4abf-bdb6-42c9a234ab85)

### Kategorien hinzufügen Fenster
![image](https://github.com/user-attachments/assets/f4744646-9b55-4540-9bde-3deaf066bc21)

### Man kann es dannach gleich in der CB auswählen
![image](https://github.com/user-attachments/assets/8fc6914e-3456-4a1e-8672-76efb1d63003)



### Die Verbindung zur DB passiert auf der Main.java
```java
import java.sql.SQLException;
import java.util.Scanner;
import javax.swing.JFrame;

public class Main {

    public static void main(String[] args) {
        // Database connection URL
        String dbUrl = "jdbc:mariadb://127.0.0.1:3306/buchhaltung";
        String username = "root";
        String userPassword = "";
        Scanner inputScanner = new Scanner(System.in);

        try {
            DatabaseManager dbManager = new DatabaseManager(dbUrl, username, userPassword);
            GUI_Buchhaltung gui = new GUI_Buchhaltung(dbManager);
            JFrame frame = new JFrame("Buchhaltung");
            frame.setContentPane(gui.getPanel());
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(600, 1000);
            frame.setMinimumSize(frame.getSize());
            frame.pack();
            frame.setVisible(true);
            frame.setJMenuBar(gui.getMenue());
        } catch (SQLException ex) {
            System.err.println("Fehler: " + ex.getMessage());
        }
    }
}
```






<p align="right">(<a href="#readme-top">back to top</a>)</p>


<!-- MARKDOWN LINKS & IMAGES -->
<!-- https://www.markdownguide.org/basic-syntax/#reference-style-links -->
[java.com]: https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white
[java-url]: https://www.java.com/de/
[product-screenshot]: Screen.png
