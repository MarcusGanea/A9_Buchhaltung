
import javax.swing.table.AbstractTableModel;
import java.util.List;
import java.util.Map;

public class BuchungTableModel extends AbstractTableModel {
    private List<Buchung> buchungen; // Liste der Buchungen
    private Map<Integer, String> kategorieMap; // Map für Kategorien
    private Map<Integer, Integer> einAuszahlungMap; // Map für Ein-/Auszahlungen

    // Konstruktor, der die Buchungen und Maps initialisiert
    public BuchungTableModel(List<Buchung> buchungen, Map<Integer, String> kategorieMap, Map<Integer, Integer> einAuszahlungMap) {
        this.buchungen = buchungen;
        this.kategorieMap = kategorieMap;
        this.einAuszahlungMap = einAuszahlungMap;
    }

    // Methode, die die Anzahl der Zeilen in der Tabelle zurückgibt
    @Override
    public int getRowCount() {
        return buchungen.size();
    }

    // Methode, die die Anzahl der Spalten in der Tabelle zurückgibt
    @Override
    public int getColumnCount() {
        return 6; // Anzahl der Spalten inklusive der versteckten ID-Spalte
    }

    // Methode, die den Wert einer Zelle in der Tabelle zurückgibt
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Buchung buchung = buchungen.get(rowIndex); // Buchung an der gegebenen Zeile
        switch (columnIndex) {
            case 0:
                return buchung.getId(); // Versteckte ID-Spalte
            case 1:
                return buchung.getDatum(); // Datum der Buchung
            case 2:
                return kategorieMap.get(buchung.getKategorieID()); // Kategorie der Buchung
            case 3:
                return buchung.getInfo(); // Informationen zur Buchung
            case 4:
                return buchung.getBetrag(); // Betrag der Buchung
            case 5:
                return einAuszahlungMap.get(buchung.getKategorieID()) == 1 ? "Einnahme" : "Ausgabe"; // Typ der Buchung
            default:
                return null; // Standardfall
        }
    }

    // Methode, die den Namen einer Spalte zurückgibt
    @Override
    public String getColumnName(int column) {
        switch (column) {
            case 0:
                return "ID"; // Versteckte ID-Spalte
            case 1:
                return "Datum"; // Spalte für das Datum
            case 2:
                return "Kategorie"; // Spalte für die Kategorie
            case 3:
                return "Notiz"; // Spalte für die Notiz
            case 4:
                return "Betrag"; // Spalte für den Betrag
            case 5:
                return "Typ"; // Spalte für den Typ
            default:
                return ""; // Standardfall
        }
    }

    // Methode, die die Klasse einer Spalte zurückgibt
    @Override
    public Class<?> getColumnClass(int columnIndex) {
        switch (columnIndex) {
            case 0:
                return Integer.class; // Versteckte ID-Spalte
            case 1:
                return java.util.Date.class; // Spalte für das Datum
            case 2:
                return String.class; // Spalte für die Kategorie
            case 3:
                return String.class; // Spalte für die Notiz
            case 4:
                return Double.class; // Spalte für den Betrag
            case 5:
                return String.class; // Spalte für den Typ
            default:
                return Object.class; // Standardfall
        }
    }

    // Methode zum Entfernen einer Zeile aus der Tabelle
    public void removeRow(int row) {
        buchungen.remove(row); // Entfernt die Buchung aus der Liste
        fireTableRowsDeleted(row, row); // Benachrichtigt die Tabelle über die gelöschte Zeile
    }

    // Methode zum Abrufen einer Buchung an einer bestimmten Zeile
    public Buchung getBuchungAt(int rowIndex) {
        return buchungen.get(rowIndex); // Gibt die Buchung an der gegebenen Zeile zurück
    }

    // Methode zum Setzen der Buchungen und Aktualisieren der Tabelle
    public void setBuchungen(List<Buchung> buchungen) {
        this.buchungen = buchungen; // Setzt die Buchungen
        fireTableDataChanged(); // Benachrichtigt die Tabelle über die geänderten Daten
    }
}