package jpa.rest.dto;

/**
 * Corps de la requête POST /concerts/{id}/reserver
 */
public class ReservationRequest {

    private Long utilisateurId;
    // "STANDARD", "PREMIUM", "LAST_MINUTE"
    private String typeTicket;

    // Champs optionnels selon le type
    private String numeroPlace;
    private String categorie;

    // PREMIUM uniquement
    private Boolean accesCoulisses;
    private Boolean meetAndGreet;
    private Boolean parkingVIP;

    // LAST_MINUTE uniquement
    private Integer pourcentageReduction;
    private String zoneAcces;

    public Long getUtilisateurId() { return utilisateurId; }
    public void setUtilisateurId(Long utilisateurId) { this.utilisateurId = utilisateurId; }

    public String getTypeTicket() { return typeTicket; }
    public void setTypeTicket(String typeTicket) { this.typeTicket = typeTicket; }

    public String getNumeroPlace() { return numeroPlace; }
    public void setNumeroPlace(String numeroPlace) { this.numeroPlace = numeroPlace; }

    public String getCategorie() { return categorie; }
    public void setCategorie(String categorie) { this.categorie = categorie; }

    public Boolean getAccesCoulisses() { return accesCoulisses; }
    public void setAccesCoulisses(Boolean accesCoulisses) { this.accesCoulisses = accesCoulisses; }

    public Boolean getMeetAndGreet() { return meetAndGreet; }
    public void setMeetAndGreet(Boolean meetAndGreet) { this.meetAndGreet = meetAndGreet; }

    public Boolean getParkingVIP() { return parkingVIP; }
    public void setParkingVIP(Boolean parkingVIP) { this.parkingVIP = parkingVIP; }

    public Integer getPourcentageReduction() { return pourcentageReduction; }
    public void setPourcentageReduction(Integer pourcentageReduction) { this.pourcentageReduction = pourcentageReduction; }

    public String getZoneAcces() { return zoneAcces; }
    public void setZoneAcces(String zoneAcces) { this.zoneAcces = zoneAcces; }
}
