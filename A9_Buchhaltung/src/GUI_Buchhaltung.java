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

public class GUI_Buchhaltung {
    private JPanel jpanel1;
    private JTable tabelleJT;
    private JTextField betragTF;
    private JTextField infoTF;
    private JButton bestaetigenBTN;
    private JComboBox<Kategorie> kategorieCB;
    private JButton loeschenBTN;
    private JScrollPane scrollSP;
    private JLabel summeJL;
    private JMenu settingsMenu;
    private JMenuBar menue;
    private DatabaseManager dbManager;

    public GUI_Buchhaltung(DatabaseManager dbManager) {
        this.dbManager = dbManager;

        menue = new JMenuBar();
        settingsMenu = new JMenu("Settings");
        JMenuItem kategorieHinzufuegen = new JMenuItem("Kategorie hinzufügen");
        menue.add(settingsMenu);
        settingsMenu.add(kategorieHinzufuegen);

        kategorieHinzufuegen.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new KategorieHinzufuegen(dbManager, GUI_Buchhaltung.this);
            }
        });

        loeschenBTN.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    deleteSelectedBuchung();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        });

        bestaetigenBTN.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    addBuchung();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        });

        kategorieHinzufuegen.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                KategorieHinzufuegen dialog = new KategorieHinzufuegen(dbManager, GUI_Buchhaltung.this);
                dialog.pack(); // Adjust the dialog size to fit its content
                dialog.setVisible(true); // Make the dialog visible
            }
        });

        // Load categories into the combo box and create the category maps
        Map<Integer, String> kategorieMap = new HashMap<>();
        Map<Integer, Integer> einAuszahlungMap = new HashMap<>();
        try {
            for (Kategorie kategorie : dbManager.getAllKategorien()) {
                kategorieCB.addItem(kategorie);
                kategorieMap.put(kategorie.getId(), kategorie.getBezeichnung());
                einAuszahlungMap.put(kategorie.getId(), kategorie.getEinAuszahlung());
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        // Initialize the table with BuchungTableModel
        try {
            List<Buchung> buchungen = dbManager.getAllBuchungen();
            BuchungTableModel model = new BuchungTableModel(buchungen, kategorieMap, einAuszahlungMap);
            tabelleJT = new JTable(model);
            TableRowSorter<BuchungTableModel> sorter = new TableRowSorter<>(model);
            tabelleJT.setRowSorter(sorter);

            // Hide the ID column
            tabelleJT.getColumnModel().getColumn(0).setMinWidth(0);
            tabelleJT.getColumnModel().getColumn(0).setMaxWidth(0);
            tabelleJT.getColumnModel().getColumn(0).setWidth(0);

            scrollSP.setViewportView(tabelleJT); // Set the JTable to the JScrollPane

            // Update the total sum
            updateSumme(buchungen);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        addRightClickMenu();
    }

    private void deleteSelectedBuchung() throws SQLException {
        int selectedRow = tabelleJT.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(null, "Bitte wählen Sie einen Datensatz aus.");
            return;
        }

        BuchungTableModel model = (BuchungTableModel) tabelleJT.getModel();
        Buchung selectedBuchung = model.getBuchungAt(selectedRow);
        Date currentDate = new Date();

        // Check if the selected Buchung was created today
        if (isSameDay(selectedBuchung.getDatum(), currentDate)) {
            dbManager.deleteBuchung(selectedBuchung.getId());
            updateTable(); // Refresh the table and update the total sum
        } else {
            JOptionPane.showMessageDialog(null, "Nur Einträge, die am gleichen Tag erstellt wurden, können gelöscht werden.");
        }
    }

    private boolean isSameDay(Date date1, Date date2) {
        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        cal1.setTime(date1);
        cal2.setTime(date2);
        return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR);
    }

    private void addBuchung() throws SQLException {
        String betragText = betragTF.getText();
        String info = infoTF.getText();

        if (betragText.isEmpty() || info.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Bitte füllen Sie alle Felder aus.");
            return;
        }

        try {
            double betrag = Double.parseDouble(betragText);
            Kategorie selectedKategorie = (Kategorie) kategorieCB.getSelectedItem();
            int kategorieID = selectedKategorie.getId();
            String typ = selectedKategorie.getEinAuszahlung() == 1 ? "Einnahme" : "Ausgabe"; // Set the typ field based on einAuszahlung
            Date datum = new Date(); // Current date

            Buchung buchung = new Buchung();
            buchung.setBetrag(betrag);
            buchung.setInfo(info);
            buchung.setKategorieID(kategorieID);
            buchung.setTyp(typ); // Set the typ field
            buchung.setDatum(datum);

            dbManager.insertBuchung(buchung);
            updateTable();

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Bitte geben Sie einen gültigen Betrag ein.");
        }
    }

    private void updateTable() throws SQLException {
        // Fetch all Buchungen and update the table model
        List<Buchung> buchungen = dbManager.getAllBuchungen();
        // Update the table model with new data
        ((BuchungTableModel) tabelleJT.getModel()).setBuchungen(buchungen);
        // Update the total sum
        updateSumme(buchungen);
    }


    private void updateSumme(List<Buchung> buchungen) {
        double summe = 0;
        for (Buchung buchung : buchungen) {
            if ("Einnahme".equals(buchung.getTyp())) {
                summe += buchung.getBetrag();
            } else if ("Ausgabe".equals(buchung.getTyp())) {
                summe -= buchung.getBetrag();
            }
        }
        summeJL.setText(String.format("%.2f €", summe));
    }

    public void updateKategorieComboBox() throws SQLException {
        kategorieCB.removeAllItems();
        for (Kategorie kategorie : dbManager.getAllKategorien()) {
            kategorieCB.addItem(kategorie);
        }
    }

    public JPanel getPanel() {
        return jpanel1;
    }

    public JMenuBar getMenue() {
        return menue;
    }

    private void addRightClickMenu() {
        JPopupMenu popupMenu = new JPopupMenu();
        JMenuItem deleteItem = new JMenuItem("Zeile löschen");
        popupMenu.add(deleteItem);

        deleteItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = tabelleJT.getSelectedRow();
                if (selectedRow != -1) {
                    try {
                        // Retrieve the ID and date from the table
                        int id = (int) tabelleJT.getValueAt(selectedRow, 0);
                        Date datum = (Date) tabelleJT.getValueAt(selectedRow, 1);

                        // Check if the selected Buchung was created today
                        Date currentDate = new Date();
                        if (isSameDay(datum, currentDate)) {
                            dbManager.deleteBuchung(id);
                            updateTable(); // Refresh the table and update the total sum
                        } else {
                            JOptionPane.showMessageDialog(null, "Nur Einträge, die am gleichen Tag erstellt wurden, können gelöscht werden.");
                        }
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });

        tabelleJT.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (e.isPopupTrigger()) {
                    showPopup(e);
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (e.isPopupTrigger()) {
                    showPopup(e);
                }
            }

            private void showPopup(MouseEvent e) {
                int row = tabelleJT.rowAtPoint(e.getPoint());
                tabelleJT.setRowSelectionInterval(row, row);
                popupMenu.show(e.getComponent(), e.getX(), e.getY());
            }
        });
    }
}