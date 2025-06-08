package cz.vse.logbookapp.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "Ponor")
public class Ponor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "uzivatel_id", nullable = false)
    private Uzivatel uzivatel;

    @ManyToOne
    @JoinColumn(name = "lokalita_id", nullable = false)
    private Lokalita lokalita;

    @Column(name = "datum", nullable = false)
    private LocalDate datum;

    @Column(name = "hloubka", nullable = false)
    private Double hloubka;

    @Column(name = "doba", nullable = false)
    private Integer doba;

    @Column(name = "teplota_vody")
    private Double teplotaVody;

    @Column(name = "poznamka")
    private String poznamka;

    // Gettery a settery
    public Long getId() {
        return id;
    }

    public Uzivatel getUzivatel() {
        return uzivatel;
    }

    public void setUzivatel(Uzivatel uzivatel) {
        this.uzivatel = uzivatel;
    }

    public Lokalita getLokalita() {
        return lokalita;
    }

    public void setLokalita(Lokalita lokalita) {
        this.lokalita = lokalita;
    }

    public LocalDate getDatum() {
        return datum;
    }

    public void setDatum(LocalDate datum) {
        this.datum = datum;
    }

    public Double getHloubka() {
        return hloubka;
    }

    public void setHloubka(Double hloubka) {
        this.hloubka = hloubka;
    }

    public Integer getDoba() {
        return doba;
    }

    public void setDoba(Integer doba) {
        this.doba = doba;
    }

    public Double getTeplotaVody() {
        return teplotaVody;
    }

    public void setTeplotaVody(Double teplotaVody) {
        this.teplotaVody = teplotaVody;
    }

    public String getPoznamka() {
        return poznamka;
    }

    public void setPoznamka(String poznamka) {
        this.poznamka = poznamka;
    }
}