import javax.swing.table.AbstractTableModel;
import java.util.List;
import java.util.Map;

public class BuchungTableModel extends AbstractTableModel {
    private List<Buchung> buchungen;
    private Map<Integer, String> kategorieMap;
    private Map<Integer, Integer> einAuszahlungMap;

    public BuchungTableModel(List<Buchung> buchungen, Map<Integer, String> kategorieMap, Map<Integer, Integer> einAuszahlungMap) {
        this.buchungen = buchungen;
        this.kategorieMap = kategorieMap;
        this.einAuszahlungMap = einAuszahlungMap;
    }

    @Override
    public int getRowCount() {
        return buchungen.size();
    }

    @Override
    public int getColumnCount() {
        return 6; // Include the hidden ID column
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Buchung buchung = buchungen.get(rowIndex);
        switch (columnIndex) {
            case 0:
                return buchung.getId(); // Hidden ID column
            case 1:
                return buchung.getDatum();
            case 2:
                return kategorieMap.get(buchung.getKategorieID());
            case 3:
                return buchung.getInfo();
            case 4:
                return buchung.getBetrag();
            case 5:
                return einAuszahlungMap.get(buchung.getKategorieID()) == 1 ? "Einnahme" : "Ausgabe";
            default:
                return null;
        }
    }

    @Override
    public String getColumnName(int column) {
        switch (column) {
            case 0:
                return "ID"; // Hidden ID column
            case 1:
                return "Datum";
            case 2:
                return "Kategorie";
            case 3:
                return "Notiz";
            case 4:
                return "Betrag";
            case 5:
                return "Typ";
            default:
                return "";
        }
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        switch (columnIndex) {
            case 0:
                return Integer.class; // Hidden ID column
            case 1:
                return java.util.Date.class;
            case 2:
                return String.class;
            case 3:
                return String.class;
            case 4:
                return Double.class;
            case 5:
                return String.class;
            default:
                return Object.class;
        }
    }

    public void removeRow(int row) {
        buchungen.remove(row);
        fireTableRowsDeleted(row, row);
    }

    public Buchung getBuchungAt(int rowIndex) {
        return buchungen.get(rowIndex);
    }

    public void setBuchungen(List<Buchung> buchungen) {
        this.buchungen = buchungen;
        fireTableDataChanged();
    }
}