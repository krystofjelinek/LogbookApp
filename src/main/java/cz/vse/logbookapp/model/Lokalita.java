package cz.vse.logbookapp.model;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "Lokalita")
public class Lokalita {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nazev", nullable = false)
    private String nazev;

    @Column(name = "zeme", nullable = false)
    private String zeme;

    @Column(name = "typ_lokality")
    private String typLokality;

    @Column(name = "max_hloubka")
    private Double maxHloubka;

    @Column(name = "popis")
    private String popis;

    @OneToMany(mappedBy = "lokalita", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Ponor> ponory;


    public Lokalita() {}

    public Lokalita(String nazev, String zeme, Double maxHloubka, String typLokality, String popis) {
        this.nazev = nazev;
        this.zeme = zeme;
        this.maxHloubka = maxHloubka;
        this.typLokality = typLokality;
        this.popis = popis;
    }

    public Long getId() {
        return id;
    }

    public String getNazev() {
        return nazev;
    }

    public void setNazev(String nazev) {
        this.nazev = nazev;
    }

    public String getZeme() {
        return zeme;
    }

    public void setZeme(String zeme) {
        this.zeme = zeme;
    }

    public String getTypLokality() {
        return typLokality;
    }

    public void setTypLokality(String typLokality) {
        this.typLokality = typLokality;
    }

    public Double getMaxHloubka() {
        return maxHloubka;
    }

    public void setMaxHloubka(Double maxHloubka) {
        this.maxHloubka = maxHloubka;
    }

    public String getPopis() {
        return popis;
    }

    public void setPopis(String popis) {
        this.popis = popis;
    }

    public List<Ponor> getPonory() {
        return ponory;
    }

    public void setPonory(List<Ponor> ponory) {
        this.ponory = ponory;
    }
}