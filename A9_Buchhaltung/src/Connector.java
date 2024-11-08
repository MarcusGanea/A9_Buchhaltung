import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

// Diese Klasse stellt eine Verbindung zur Datenbank her
public class Connector {
    // Methode zum Abrufen einer Datenbankverbindung
    public static Connection getConnection(String url, String user, String password) throws SQLException {
        return DriverManager.getConnection(url, user, password); // Stellt die Verbindung zur Datenbank her
    }
}