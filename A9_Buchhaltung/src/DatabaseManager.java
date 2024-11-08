import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseManager {
    private Connection connection; // Verbindung zur Datenbank

    // Konstruktor, der die Verbindung zur Datenbank herstellt
    public DatabaseManager(String dbUrl, String username, String password) throws SQLException {
        connection = DriverManager.getConnection(dbUrl, username, password);
    }

    // Methode zum Schließen der Datenbankverbindung
    public void close() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }

    // Methode zum Löschen einer Buchung anhand der ID
    public void deleteBuchung(int id) throws SQLException {
        String query = "DELETE FROM buchungen WHERE ID = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, id); // Setzen der ID im PreparedStatement
            pstmt.executeUpdate(); // Ausführen der Löschanweisung
        }
    }

    // Methode zum Abrufen aller Buchungen aus der Datenbank
    public List<Buchung> getAllBuchungen() throws SQLException {
        List<Buchung> buchungen = new ArrayList<>();
        String query = "SELECT * FROM buchungen JOIN kategorie ON buchungen.KategorieID = kategorie.ID";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                Buchung buchung = new Buchung();
                buchung.setId(rs.getInt("ID")); // Setzen der ID der Buchung
                buchung.setDatum(rs.getDate("Datum")); // Setzen des Datums der Buchung
                buchung.setInfo(rs.getString("Info")); // Setzen der Informationen zur Buchung
                buchung.setBetrag(rs.getDouble("Betrag")); // Setzen des Betrags der Buchung
                buchung.setKategorieID(rs.getInt("KategorieID")); // Setzen der Kategorie-ID der Buchung
                buchung.setTyp(rs.getInt("Ein/Auszahlung") == 1 ? "Einnahme" : "Ausgabe"); // Setzen des Typs der Buchung
                buchungen.add(buchung); // Hinzufügen der Buchung zur Liste
            }
        }
        return buchungen;
    }

    // Methode zum Abrufen aller Kategorien aus der Datenbank
    public List<Kategorie> getAllKategorien() throws SQLException {
        List<Kategorie> kategorien = new ArrayList<>();
        String query = "SELECT * FROM kategorie";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                Kategorie kategorie = new Kategorie();
                kategorie.setId(rs.getInt("ID")); // Setzen der ID der Kategorie
                kategorie.setBezeichnung(rs.getString("Bezeichnung")); // Setzen der Bezeichnung der Kategorie
                kategorie.setKurzbeschreibung(rs.getString("Kurzbeschreibung")); // Setzen der Kurzbeschreibung der Kategorie
                kategorie.setEinAuszahlung(rs.getInt("Ein/Auszahlung")); // Setzen des Ein-/Auszahlungswerts der Kategorie
                kategorien.add(kategorie); // Hinzufügen der Kategorie zur Liste
            }
        }
        return kategorien;
    }

    // Methode zum Einfügen einer neuen Buchung in die Datenbank
    public void insertBuchung(Buchung buchung) throws SQLException {
        String query = "INSERT INTO buchungen (Datum, Info, Betrag, KategorieID) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setDate(1, new java.sql.Date(buchung.getDatum().getTime())); // Setzen des Datums im PreparedStatement
            pstmt.setString(2, buchung.getInfo()); // Setzen der Informationen im PreparedStatement
            pstmt.setDouble(3, buchung.getBetrag()); // Setzen des Betrags im PreparedStatement
            pstmt.setInt(4, buchung.getKategorieID()); // Setzen der Kategorie-ID im PreparedStatement
            pstmt.executeUpdate(); // Ausführen der Einfügeanweisung
        }
    }

    // Methode zum Einfügen einer neuen Kategorie in die Datenbank
    public void insertKategorie(Kategorie kategorie) throws SQLException {
        String query = "INSERT INTO kategorie (Bezeichnung, Kurzbeschreibung, `Ein/Auszahlung`) VALUES (?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, kategorie.getBezeichnung()); // Setzen der Bezeichnung im PreparedStatement
            pstmt.setString(2, kategorie.getKurzbeschreibung()); // Setzen der Kurzbeschreibung im PreparedStatement
            pstmt.setInt(3, kategorie.getEinAuszahlung()); // Setzen des Ein-/Auszahlungswerts im PreparedStatement
            pstmt.executeUpdate(); // Ausführen der Einfügeanweisung
        }
    }
}