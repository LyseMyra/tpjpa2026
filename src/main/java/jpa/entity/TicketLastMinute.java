package jpa.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Transient;

import java.math.BigDecimal;

/**
 * Ticket last minute - sans place réservée, tarif réduit
 */
@Entity
@DiscriminatorValue("LAST_MINUTE")
public class TicketLastMinute extends Ticket {

    private Integer pourcentageReduction; // Ex: 30 pour 30% de réduction
    private String zoneAcces;            // Ex: "Debout uniquement", "Places restantes"
    private Boolean placeReservee;       // Toujours false pour last minute

    // Constructeurs
    public TicketLastMinute() {
        super();
        this.placeReservee = false;
    }

    public TicketLastMinute(Concert concert, Utilisateur utilisateur, BigDecimal prixAchat,
                            Integer pourcentageReduction, String zoneAcces) {
        super(concert, utilisateur, prixAchat);
        this.pourcentageReduction = pourcentageReduction;
        this.zoneAcces = zoneAcces;
        this.placeReservee = false;
    }

    @Override
    @Transient
    public String getTypeTicket() {
        return "LAST_MINUTE";
    }

    // Getters et Setters
    public Integer getPourcentageReduction() {
        return pourcentageReduction;
    }

    public void setPourcentageReduction(Integer pourcentageReduction) {
        this.pourcentageReduction = pourcentageReduction;
    }

    public String getZoneAcces() {
        return zoneAcces;
    }

    public void setZoneAcces(String zoneAcces) {
        this.zoneAcces = zoneAcces;
    }

    public Boolean getPlaceReservee() {
        return placeReservee;
    }

    public void setPlaceReservee(Boolean placeReservee) {
        this.placeReservee = placeReservee;
    }

    @Override
    public String toString() {
        return "TicketLastMinute{" +
                "id=" + getId() +
                ", numeroTicket='" + getNumeroTicket() + '\'' +
                ", zoneAcces='" + zoneAcces + '\'' +
                ", pourcentageReduction=" + pourcentageReduction + "%" +
                ", prixAchat=" + getPrixAchat() +
                '}';
    }
}
