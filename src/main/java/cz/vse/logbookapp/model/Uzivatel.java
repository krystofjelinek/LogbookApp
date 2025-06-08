package cz.vse.logbookapp.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "Uzivatel")
public class Uzivatel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "jmeno", nullable = false)
    private String jmeno;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "certifikace")
    private String certifikace;

    @Column(name = "datum_registrace")
    private LocalDate datumRegistrace;

    @OneToMany(mappedBy = "uzivatel", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Ponor> ponory;

    // Gettery a settery
    public Long getId() {
        return id;
    }

    public String getJmeno() {
        return jmeno;
    }

    public void setJmeno(String jmeno) {
        this.jmeno = jmeno;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCertifikace() {
        return certifikace;
    }

    public void setCertifikace(String certifikace) {
        this.certifikace = certifikace;
    }

    public LocalDate getDatumRegistrace() {
        return datumRegistrace;
    }

    public void setDatumRegistrace(LocalDate datumRegistrace) {
        this.datumRegistrace = datumRegistrace;
    }

    public List<Ponor> getPonory() {
        return ponory;
    }

    public void setPonory(List<Ponor> ponory) {
        this.ponory = ponory;
    }
}