package jpa.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Classe abstraite représentant un ticket de concert
 * Utilise l'héritage JPA avec la stratégie SINGLE_TABLE par défaut
 * (peut être modifiée pour tester JOINED ou TABLE_PER_CLASS)
 */
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "type_ticket", discriminatorType = DiscriminatorType.STRING)
public abstract class Ticket {

    private Long id;
    private String numeroTicket;
    private BigDecimal prixAchat;
    private LocalDateTime dateAchat;
    private Boolean valide = true;
    private Boolean utilise = false;

    // Relations
    private Concert concert;
    private Utilisateur utilisateur;

    // Constructeurs
    public Ticket() {
        this.dateAchat = LocalDateTime.now();
        this.numeroTicket = generateNumeroTicket();
    }

    public Ticket(Concert concert, Utilisateur utilisateur, BigDecimal prixAchat) {
        this.concert = concert;
        this.utilisateur = utilisateur;
        this.prixAchat = prixAchat;
        this.dateAchat = LocalDateTime.now();
        this.numeroTicket = generateNumeroTicket();
    }

    // Méthode pour générer un numéro de ticket unique
    private String generateNumeroTicket() {
        return "TKT-" + System.currentTimeMillis() + "-" + (int)(Math.random() * 1000);
    }

    // Méthode abstraite que chaque type de ticket doit implémenter
    @Transient
    public abstract String getTypeTicket();

    // Getters et Setters
    @Id
    @GeneratedValue
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Column(unique = true, nullable = false, length = 50)
    public String getNumeroTicket() {
        return numeroTicket;
    }

    public void setNumeroTicket(String numeroTicket) {
        this.numeroTicket = numeroTicket;
    }

    @Column(nullable = false)
    public BigDecimal getPrixAchat() {
        return prixAchat;
    }

    public void setPrixAchat(BigDecimal prixAchat) {
        this.prixAchat = prixAchat;
    }

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @Column(nullable = false)
    public LocalDateTime getDateAchat() {
        return dateAchat;
    }

    public void setDateAchat(LocalDateTime dateAchat) {
        this.dateAchat = dateAchat;
    }

    public Boolean getValide() {
        return valide;
    }

    public void setValide(Boolean valide) {
        this.valide = valide;
    }

    public Boolean getUtilise() {
        return utilise;
    }

    public void setUtilise(Boolean utilise) {
        this.utilise = utilise;
    }

    // Relation ManyToOne avec Concert
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "concert_id", nullable = false)
    public Concert getConcert() {
        return concert;
    }

    public void setConcert(Concert concert) {
        this.concert = concert;
    }

    // Relation ManyToOne avec Utilisateur
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "utilisateur_id", nullable = false)
    public Utilisateur getUtilisateur() {
        return utilisateur;
    }

    public void setUtilisateur(Utilisateur utilisateur) {
        this.utilisateur = utilisateur;
    }

    @Override
    public String toString() {
        return "Ticket{" +
                "id=" + id +
                ", numeroTicket='" + numeroTicket + '\'' +
                ", type='" + getTypeTicket() + '\'' +
                ", prixAchat=" + prixAchat +
                ", dateAchat=" + dateAchat +
                ", valide=" + valide +
                '}';
    }
}
