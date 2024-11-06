import java.util.Date;
public class Buchung {
    private double betrag;
    private String info;
    private int kategorieID;
    private Date datum;
    private int id; // Hidden ID column
    private String typ; // "Einnahme" or "Ausgabe"

    public String getTyp() {
        return typ;
    }

    public void setTyp(String typ) {
        this.typ = typ;
    }

    public double getBetrag() {
        return betrag;
    }

    public void setBetrag(double betrag) {
        this.betrag = betrag;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public int getKategorieID() {
        return kategorieID;
    }

    public void setKategorieID(int kategorieID) {
        this.kategorieID = kategorieID;
    }

    public Date getDatum() {
        return datum;
    }

    public void setDatum(Date datum) {
        this.datum = datum;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}