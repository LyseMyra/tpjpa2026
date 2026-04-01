package jpa.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Transient;

import java.math.BigDecimal;

/**
 * Ticket standard - place assise normale
 */
@Entity
@DiscriminatorValue("STANDARD")
public class TicketStandard extends Ticket {

    private String numeroPlace; // Ex: "A12", "B05"
    private String categorie;   // Ex: "Fosse", "Gradin", "Balcon"

    // Constructeurs
    public TicketStandard() {
        super();
    }

    public TicketStandard(Concert concert, Utilisateur utilisateur, BigDecimal prixAchat,
                          String numeroPlace, String categorie) {
        super(concert, utilisateur, prixAchat);
        this.numeroPlace = numeroPlace;
        this.categorie = categorie;
    }

    @Override
    @Transient
    public String getTypeTicket() {
        return "STANDARD";
    }

    // Getters et Setters
    public String getNumeroPlace() {
        return numeroPlace;
    }

    public void setNumeroPlace(String numeroPlace) {
        this.numeroPlace = numeroPlace;
    }

    public String getCategorie() {
        return categorie;
    }

    public void setCategorie(String categorie) {
        this.categorie = categorie;
    }

    @Override
    public String toString() {
        return "TicketStandard{" +
                "id=" + getId() +
                ", numeroTicket='" + getNumeroTicket() + '\'' +
                ", numeroPlace='" + numeroPlace + '\'' +
                ", categorie='" + categorie + '\'' +
                ", prixAchat=" + getPrixAchat() +
                '}';
    }
}
