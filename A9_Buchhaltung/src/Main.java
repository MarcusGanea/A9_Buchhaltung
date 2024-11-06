import java.sql.Connection;
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