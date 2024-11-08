import javax.swing.*;
import java.awt.event.*;
import java.sql.SQLException;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class KategorieHinzufuegen extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField kategorieNameTF;
    private JComboBox<String> einAuszahlungCB;
    private JTextField beschreibungTF;
    private JPanel jpanel2;
    private DatabaseManager dbManager;
    private GUI_Buchhaltung guiBuchhaltung;

    public KategorieHinzufuegen(DatabaseManager dbManager, GUI_Buchhaltung guiBuchhaltung) {
        this.dbManager = dbManager;
        this.guiBuchhaltung = guiBuchhaltung;
        setContentPane(contentPane); // Ensure contentPane is initialized
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        // Add items to the combo box
        einAuszahlungCB.addItem("Einnahme");
        einAuszahlungCB.addItem("Auszahlung");

        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });

        // wenn man Enter drückt, wird onOK() ausgeführt
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    private void onOK() {
        String kategorieName = kategorieNameTF.getText();
        String beschreibung = beschreibungTF.getText();
        String einAuszahlung = (String) einAuszahlungCB.getSelectedItem();

        if (kategorieName.isEmpty() || einAuszahlung == null) {
            JOptionPane.showMessageDialog(null, "Bitte füllen Sie alle Felder aus.");
            return;
        }

        try {
            dbManager.insertKategorie(new Kategorie(kategorieName, beschreibung, einAuszahlung.equals("Einnahme") ? 1 : 0));
            guiBuchhaltung.updateKategorieComboBox();
            dispose();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void onCancel() {
        dispose();
    }
}