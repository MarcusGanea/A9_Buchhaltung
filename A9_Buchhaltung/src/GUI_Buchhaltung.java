import javax.swing.*;
import javax.swing.table.TableRowSorter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Date;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.SwingConstants;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.Color;
import java.awt.Component;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;
public class GUI_Buchhaltung {
    private JPanel jpanel1; // Hauptpanel der GUI
    private JTable tabelleJT; // Tabelle zur Anzeige der Buchungen
    private JTextField betragTF; // Textfeld für den Betrag
    private JTextField infoTF; // Textfeld für Informationen zur Buchung
    private JButton bestaetigenBTN; // Button zum Bestätigen einer neuen Buchung
    private JComboBox<Kategorie> kategorieCB; // ComboBox zur Auswahl der Kategorie
    private JButton loeschenBTN; // Button zum Löschen einer Buchung
    private JScrollPane scrollSP; // ScrollPane für die Tabelle
    private JLabel summeJL; // Label zur Anzeige der Gesamtsumme
    private JMenu settingsMenu; // Menü für Einstellungen
    private JMenuBar menue; // Menüleiste
    private DatabaseManager dbManager; // Datenbankmanager

    public GUI_Buchhaltung(DatabaseManager dbManager) {
        this.dbManager = dbManager; // Initialisierung des Datenbankmanagers

        menue = new JMenuBar(); // Erstellung der Menüleiste
        settingsMenu = new JMenu("Settings"); // Erstellung des Einstellungsmenüs
        JMenuItem kategorieHinzufuegen = new JMenuItem("Kategorie hinzufügen"); // Menüeintrag zum Hinzufügen einer Kategorie
        menue.add(settingsMenu); // Hinzufügen des Einstellungsmenüs zur Menüleiste
        settingsMenu.add(kategorieHinzufuegen); // Hinzufügen des Menüeintrags zum Einstellungsmenü

        kategorieHinzufuegen.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                KategorieHinzufuegen dialog = new KategorieHinzufuegen(dbManager, GUI_Buchhaltung.this);
                dialog.pack(); // Anpassen der Dialoggröße
                dialog.setVisible(true); // Sichtbar machen des Dialogs
            }
        });

        loeschenBTN.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    deleteSelectedBuchung(); // Löschen der ausgewählten Buchung
                } catch (SQLException ex) {
                    ex.printStackTrace(); // Fehlerbehandlung
                }
            }
        });

        bestaetigenBTN.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    addBuchung(); // Hinzufügen einer neuen Buchung
                } catch (SQLException ex) {
                    ex.printStackTrace(); // Fehlerbehandlung
                }
            }
        });

        // wenn man Enter drückt, wird addBuchung() ausgeführt
        betragTF.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    addBuchung(); // Führt die gleiche Funktionalität wie der Bestätigen-Button aus
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), JComponent.WHEN_FOCUSED);

        // wenn man Enter drückt, wird addBuchung() ausgeführt
        infoTF.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    addBuchung(); // Führt die gleiche Funktionalität wie der Bestätigen-Button aus
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), JComponent.WHEN_FOCUSED);

        // Laden der Kategorien in die ComboBox und Erstellen der Kategoriemaps
        Map<Integer, String> kategorieMap = new HashMap<>();
        Map<Integer, Integer> einAuszahlungMap = new HashMap<>();
        try {
            for (Kategorie kategorie : dbManager.getAllKategorien()) {
                kategorieCB.addItem(kategorie); // Hinzufügen der Kategorie zur ComboBox
                kategorieMap.put(kategorie.getId(), kategorie.getBezeichnung()); // Hinzufügen zur Kategoriemap
                einAuszahlungMap.put(kategorie.getId(), kategorie.getEinAuszahlung()); // Hinzufügen zur Ein-/Auszahlungsmap
            }
        } catch (SQLException ex) {
            ex.printStackTrace(); // Fehlerbehandlung
        }

        // Initialisierung der Tabelle mit BuchungTableModel
        try {
            List<Buchung> buchungen = dbManager.getAllBuchungen(); // Abrufen aller Buchungen
            BuchungTableModel model = new BuchungTableModel(buchungen, kategorieMap, einAuszahlungMap); // Erstellen des TableModels

            tabelleJT = new JTable(model) {
                @Override
                public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
                    Component c = super.prepareRenderer(renderer, row, column);
                    BuchungTableModel model = (BuchungTableModel) getModel();
                    Buchung buchung = model.getBuchungAt(convertRowIndexToModel(row));
                    if ("Einnahme".equals(buchung.getTyp())) {
                        c.setBackground(new Color(144, 238, 144)); // Light Green (Pastel Green)
                    } else if ("Ausgabe".equals(buchung.getTyp())) {
                        c.setBackground(new Color(255, 182, 193)); // Light Pink (Pastel Red)
                    } else {
                        c.setBackground(getBackground());
                    }
                    return c;
                }
            };
            TableRowSorter<BuchungTableModel> sorter = new TableRowSorter<>(model); // Erstellen des Sorters
            tabelleJT.setRowSorter(sorter); // Setzen des Sorters

            // Verstecken der ID-Spalte
            tabelleJT.getColumnModel().getColumn(0).setMinWidth(0);
            tabelleJT.getColumnModel().getColumn(0).setMaxWidth(0);
            tabelleJT.getColumnModel().getColumn(0).setWidth(0);

            // Center the text in the "Typ" column
            DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
            centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
            tabelleJT.getColumnModel().getColumn(5).setCellRenderer(centerRenderer);

            scrollSP.setViewportView(tabelleJT); // Setzen der Tabelle in die ScrollPane

            // Aktualisieren der Gesamtsumme
            updateSumme(buchungen);
        } catch (SQLException ex) {
            ex.printStackTrace(); // Fehlerbehandlung
        }

        addRightClickMenu(); // Hinzufügen des Rechtsklickmenüs
    }

    private void deleteSelectedBuchung() throws SQLException {
        int selectedRow = tabelleJT.getSelectedRow(); // Abrufen der ausgewählten Zeile
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(null, "Bitte wählen Sie einen Datensatz aus."); // Fehlermeldung, wenn keine Zeile ausgewählt ist
            return;
        }

        BuchungTableModel model = (BuchungTableModel) tabelleJT.getModel(); // Abrufen des TableModels
        Buchung selectedBuchung = model.getBuchungAt(selectedRow); // Abrufen der ausgewählten Buchung
        Date currentDate = new Date(); // Aktuelles Datum

        // Überprüfen, ob die ausgewählte Buchung heute erstellt wurde
        if (isSameDay(selectedBuchung.getDatum(), currentDate)) {
            dbManager.deleteBuchung(selectedBuchung.getId()); // Löschen der Buchung
            updateTable(); // Aktualisieren der Tabelle und der Gesamtsumme
        } else {
            JOptionPane.showMessageDialog(null, "Nur Einträge, die am gleichen Tag erstellt wurden, können gelöscht werden."); // Fehlermeldung
        }
    }

    private boolean isSameDay(Date date1, Date date2) {
        Calendar cal1 = Calendar.getInstance(); // Kalenderinstanz für das erste Datum
        Calendar cal2 = Calendar.getInstance(); // Kalenderinstanz für das zweite Datum
        cal1.setTime(date1); // Setzen des ersten Datums
        cal2.setTime(date2); // Setzen des zweiten Datums
        return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR); // Überprüfen, ob die Daten am gleichen Tag sind
    }

    private void addBuchung() throws SQLException {
        String betragText = betragTF.getText(); // Abrufen des Betrags aus dem Textfeld
        String info = infoTF.getText(); // Abrufen der Informationen aus dem Textfeld

        if (betragText.isEmpty() || info.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Bitte füllen Sie alle Felder aus."); // Fehlermeldung, wenn Felder leer sind
            return;
        }

        try {
            double betrag = Double.parseDouble(betragText); // Parsen des Betrags
            Kategorie selectedKategorie = (Kategorie) kategorieCB.getSelectedItem(); // Abrufen der ausgewählten Kategorie
            int kategorieID = selectedKategorie.getId(); // Abrufen der Kategorie-ID
            String typ = selectedKategorie.getEinAuszahlung() == 1 ? "Einnahme" : "Ausgabe"; // Setzen des Typs basierend auf Ein-/Auszahlung
            Date datum = new Date(); // Aktuelles Datum

            Buchung buchung = new Buchung(); // Erstellen einer neuen Buchung
            buchung.setBetrag(betrag); // Setzen des Betrags
            buchung.setInfo(info); // Setzen der Informationen
            buchung.setKategorieID(kategorieID); // Setzen der Kategorie-ID
            buchung.setTyp(typ); // Setzen des Typs
            buchung.setDatum(datum); // Setzen des Datums

            dbManager.insertBuchung(buchung); // Einfügen der Buchung in die Datenbank
            updateTable(); // Aktualisieren der Tabelle

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Bitte geben Sie einen gültigen Betrag ein."); // Fehlermeldung bei ungültigem Betrag
        }
    }

    private void updateTable() throws SQLException {
        // Abrufen aller Buchungen und Aktualisieren des TableModels
        List<Buchung> buchungen = dbManager.getAllBuchungen(); // Abrufen aller Buchungen
        // Aktualisieren des TableModels mit neuen Daten
        ((BuchungTableModel) tabelleJT.getModel()).setBuchungen(buchungen); // Setzen der Buchungen im TableModel
        // Aktualisieren der Gesamtsumme
        updateSumme(buchungen); // Aktualisieren der Gesamtsumme
    }

    private void updateSumme(List<Buchung> buchungen) {
        double summe = 0; // Initialisierung der Summe
        for (Buchung buchung : buchungen) {
            if ("Einnahme".equals(buchung.getTyp())) {
                summe += buchung.getBetrag(); // Hinzufügen des Betrags bei Einnahme
            } else if ("Ausgabe".equals(buchung.getTyp())) {
                summe -= buchung.getBetrag(); // Abziehen des Betrags bei Ausgabe
            }
        }
        summeJL.setText(String.format("%.2f €", summe)); // Setzen der Gesamtsumme im Label
    }

    public void updateKategorieComboBox() throws SQLException {
        kategorieCB.removeAllItems(); // Entfernen aller Einträge in der ComboBox
        for (Kategorie kategorie : dbManager.getAllKategorien()) {
            kategorieCB.addItem(kategorie); // Hinzufügen der Kategorien zur ComboBox
        }
    }

    public JPanel getPanel() {
        return jpanel1; // Rückgabe des Hauptpanels
    }

    public JMenuBar getMenue() {
        return menue; // Rückgabe der Menüleiste
    }

    private void addRightClickMenu() {
        JPopupMenu popupMenu = new JPopupMenu(); // Erstellen des Popup-Menüs
        JMenuItem deleteItem = new JMenuItem("Zeile löschen"); // Menüeintrag zum Löschen einer Zeile
        popupMenu.add(deleteItem); // Hinzufügen des Menüeintrags zum Popup-Menü

        deleteItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = tabelleJT.getSelectedRow(); // Abrufen der ausgewählten Zeile
                if (selectedRow != -1) {
                    try {
                        // Abrufen der ID und des Datums aus der Tabelle
                        int id = (int) tabelleJT.getValueAt(selectedRow, 0); // Abrufen der ID
                        Date datum = (Date) tabelleJT.getValueAt(selectedRow, 1); // Abrufen des Datums

                        // Überprüfen, ob die ausgewählte Buchung heute erstellt wurde
                        Date currentDate = new Date(); // Aktuelles Datum
                        if (isSameDay(datum, currentDate)) {
                            dbManager.deleteBuchung(id); // Löschen der Buchung
                            updateTable(); // Aktualisieren der Tabelle und der Gesamtsumme
                        } else {
                            JOptionPane.showMessageDialog(null, "Nur Einträge, die am gleichen Tag erstellt wurden, können gelöscht werden."); // Fehlermeldung
                        }
                    } catch (SQLException ex) {
                        ex.printStackTrace(); // Fehlerbehandlung
                    }
                }
            }
        });

        tabelleJT.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (e.isPopupTrigger()) {
                    showPopup(e); // Anzeigen des Popup-Menüs bei Rechtsklick
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (e.isPopupTrigger()) {
                    showPopup(e); // Anzeigen des Popup-Menüs bei Rechtsklick
                }
            }

            private void showPopup(MouseEvent e) {
                int row = tabelleJT.rowAtPoint(e.getPoint()); // Abrufen der Zeile an der Klickposition
                tabelleJT.setRowSelectionInterval(row, row); // Setzen der Zeilenauswahl
                popupMenu.show(e.getComponent(), e.getX(), e.getY()); // Anzeigen des Popup-Menüs
            }
        });
    }
}