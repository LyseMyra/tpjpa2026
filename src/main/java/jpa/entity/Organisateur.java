package jpa.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Entité représentant un organisateur d'événements
 */
@Entity
public class Organisateur {

    private Long id;
    private String email;
    private String motDePasse;
    private String nomOrganisation;
    private String siret;
    private String telephone;
    private String adresse;
    private LocalDateTime dateInscription;
    private Boolean valide = false; // Nécessite validation par admin
    private Boolean actif = true;

    // Relations
    private List<Concert> concerts = new ArrayList<>();

    // Constructeurs
    public Organisateur() {
        this.dateInscription = LocalDateTime.now();
    }

    public Organisateur(String email, String motDePasse, String nomOrganisation, String siret, String telephone) {
        this.email = email;
        this.motDePasse = motDePasse;
        this.nomOrganisation = nomOrganisation;
        this.siret = siret;
        this.telephone = telephone;
        this.dateInscription = LocalDateTime.now();
    }

    // Getters et Setters
    @Id
    @GeneratedValue
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Column(nullable = false, unique = true, length = 150)
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Column(nullable = false)
    public String getMotDePasse() {
        return motDePasse;
    }

    public void setMotDePasse(String motDePasse) {
        this.motDePasse = motDePasse;
    }

    @Column(nullable = false, length = 200)
    public String getNomOrganisation() {
        return nomOrganisation;
    }

    public void setNomOrganisation(String nomOrganisation) {
        this.nomOrganisation = nomOrganisation;
    }

    @Column(unique = true, length = 14)
    public String getSiret() {
        return siret;
    }

    public void setSiret(String siret) {
        this.siret = siret;
    }

    @Column(length = 20)
    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    @Column(length = 300)
    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @Column(nullable = false)
    public LocalDateTime getDateInscription() {
        return dateInscription;
    }

    public void setDateInscription(LocalDateTime dateInscription) {
        this.dateInscription = dateInscription;
    }

    public Boolean getValide() {
        return valide;
    }

    public void setValide(Boolean valide) {
        this.valide = valide;
    }

    public Boolean getActif() {
        return actif;
    }

    public void setActif(Boolean actif) {
        this.actif = actif;
    }

    // Relation OneToMany avec Concert (bidirectionnelle)
    @JsonIgnore
    @OneToMany(mappedBy = "organisateur", cascade = CascadeType.ALL)
    public List<Concert> getConcerts() {
        return concerts;
    }

    public void setConcerts(List<Concert> concerts) {
        this.concerts = concerts;
    }

    @Override
    public String toString() {
        return "Organisateur{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", nomOrganisation='" + nomOrganisation + '\'' +
                ", siret='" + siret + '\'' +
                ", valide=" + valide +
                '}';
    }
}
