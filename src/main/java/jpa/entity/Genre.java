package jpa.entity;

/**
 * Enumération représentant les différents genres musicaux
 */
public enum Genre {
    POP("Pop"),
    ROCK("Rock"),
    JAZZ("Jazz"),
    CLASSIQUE("Classique"),
    HIP_HOP("Hip-Hop"),
    ELECTRO("Électro"),
    RAP("Rap"),
    METAL("Metal"),
    REGGAE("Reggae"),
    COUNTRY("Country"),
    R_AND_B("R&B"),
    FOLK("Folk"),
    BLUES("Blues"),
    AUTRE("Autre");

    private final String libelle;

    Genre(String libelle) {
        this.libelle = libelle;
    }

    public String getLibelle() {
        return libelle;
    }
}
