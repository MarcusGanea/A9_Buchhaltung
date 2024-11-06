import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseManager {
    private Connection connection;

    public DatabaseManager(String dbUrl, String username, String password) throws SQLException {
        connection = DriverManager.getConnection(dbUrl, username, password);
    }

    public void close() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }

    public void deleteBuchung(int id) throws SQLException {
        String query = "DELETE FROM buchungen WHERE ID = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        }
    }

    public List<Buchung> getAllBuchungen() throws SQLException {
        List<Buchung> buchungen = new ArrayList<>();
        String query = "SELECT * FROM buchungen JOIN kategorie ON buchungen.KategorieID = kategorie.ID";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                Buchung buchung = new Buchung();
                buchung.setId(rs.getInt("ID"));
                buchung.setDatum(rs.getDate("Datum"));
                buchung.setInfo(rs.getString("Info"));
                buchung.setBetrag(rs.getDouble("Betrag"));
                buchung.setKategorieID(rs.getInt("KategorieID"));
                buchung.setTyp(rs.getInt("Ein/Auszahlung") == 1 ? "Einnahme" : "Ausgabe");
                buchungen.add(buchung);
            }
        }
        return buchungen;
    }

    public List<Kategorie> getAllKategorien() throws SQLException {
        List<Kategorie> kategorien = new ArrayList<>();
        String query = "SELECT * FROM kategorie";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                Kategorie kategorie = new Kategorie();
                kategorie.setId(rs.getInt("ID"));
                kategorie.setBezeichnung(rs.getString("Bezeichnung"));
                kategorie.setKurzbeschreibung(rs.getString("Kurzbeschreibung"));
                kategorie.setEinAuszahlung(rs.getInt("Ein/Auszahlung"));
                kategorien.add(kategorie);
            }
        }
        return kategorien;
    }

    public void insertBuchung(Buchung buchung) throws SQLException {
        String query = "INSERT INTO buchungen (Datum, Info, Betrag, KategorieID) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setDate(1, new java.sql.Date(buchung.getDatum().getTime()));
            pstmt.setString(2, buchung.getInfo());
            pstmt.setDouble(3, buchung.getBetrag());
            pstmt.setInt(4, buchung.getKategorieID());
            pstmt.executeUpdate();
        }
    }

    public void insertKategorie(Kategorie kategorie) throws SQLException {
        String query = "INSERT INTO kategorie (Bezeichnung, Kurzbeschreibung, `Ein/Auszahlung`) VALUES (?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, kategorie.getBezeichnung());
            pstmt.setString(2, kategorie.getKurzbeschreibung());
            pstmt.setInt(3, kategorie.getEinAuszahlung());
            pstmt.executeUpdate();
        }
    }
}