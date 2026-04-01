package jpa.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Transient;

import java.math.BigDecimal;

/**
 * Ticket premium - avec avantages exclusifs
 */
@Entity
@DiscriminatorValue("PREMIUM")
public class TicketPremium extends Ticket {

    private String numeroPlace;
    private String categorie;
    private Boolean accesCoulisses;     // Accès backstage
    private Boolean meetAndGreet;       // Rencontre avec l'artiste
    private Boolean parkingVIP;         // Parking réservé
    private String avantagesSupplementaires; // Autres avantages

    // Constructeurs
    public TicketPremium() {
        super();
        this.accesCoulisses = false;
        this.meetAndGreet = false;
        this.parkingVIP = false;
    }

    public TicketPremium(Concert concert, Utilisateur utilisateur, BigDecimal prixAchat,
                         String numeroPlace, String categorie,
                         Boolean accesCoulisses, Boolean meetAndGreet, Boolean parkingVIP) {
        super(concert, utilisateur, prixAchat);
        this.numeroPlace = numeroPlace;
        this.categorie = categorie;
        this.accesCoulisses = accesCoulisses;
        this.meetAndGreet = meetAndGreet;
        this.parkingVIP = parkingVIP;
    }

    @Override
    @Transient
    public String getTypeTicket() {
        return "PREMIUM";
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

    public Boolean getAccesCoulisses() {
        return accesCoulisses;
    }

    public void setAccesCoulisses(Boolean accesCoulisses) {
        this.accesCoulisses = accesCoulisses;
    }

    public Boolean getMeetAndGreet() {
        return meetAndGreet;
    }

    public void setMeetAndGreet(Boolean meetAndGreet) {
        this.meetAndGreet = meetAndGreet;
    }

    public Boolean getParkingVIP() {
        return parkingVIP;
    }

    public void setParkingVIP(Boolean parkingVIP) {
        this.parkingVIP = parkingVIP;
    }

    public String getAvantagesSupplementaires() {
        return avantagesSupplementaires;
    }

    public void setAvantagesSupplementaires(String avantagesSupplementaires) {
        this.avantagesSupplementaires = avantagesSupplementaires;
    }

    @Override
    public String toString() {
        return "TicketPremium{" +
                "id=" + getId() +
                ", numeroTicket='" + getNumeroTicket() + '\'' +
                ", numeroPlace='" + numeroPlace + '\'' +
                ", categorie='" + categorie + '\'' +
                ", accesCoulisses=" + accesCoulisses +
                ", meetAndGreet=" + meetAndGreet +
                ", parkingVIP=" + parkingVIP +
                ", prixAchat=" + getPrixAchat() +
                '}';
    }
}
