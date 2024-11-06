public class Kategorie {
    private String name;
    private int einAuszahlung;
    private int id;
    private String kurzbeschreibung;

    public Kategorie() {
        // No-argument constructor
    }

    public Kategorie(String name, String kurzbeschreibung, int einAuszahlung) {
        this.name = name;
        this.einAuszahlung = einAuszahlung;
        this.kurzbeschreibung = kurzbeschreibung;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBezeichnung() {
        return name;
    }

    public void setBezeichnung(String name) {
        this.name = name;
    }

    public int getEinAuszahlung() {
        return einAuszahlung;
    }

    public void setEinAuszahlung(int einAuszahlung) {
        this.einAuszahlung = einAuszahlung;
    }

    public String getKurzbeschreibung() {
        return kurzbeschreibung;
    }

    public void setKurzbeschreibung(String kurzbeschreibung) {
        this.kurzbeschreibung = kurzbeschreibung;
    }

    @Override
    public String toString() {
        return name;
    }
}