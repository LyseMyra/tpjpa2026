package jpa.dto;

import jpa.entity.Genre;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

/**
 * DTO pour l'entité Concert
 * Ne contient que les informations publiques (pas de relations sensibles)
 */
public class ConcertDTO {

    private Long id;
    private String nom;
    private String artiste;
    private String description;
    private LocalDate dateConcert;
    private LocalTime heureConcert;
    private String lieu;
    private String ville;
    private Genre genre;
    private BigDecimal prix;
    private Integer capacite;
    private Integer ticketsDisponibles;
    private String image;
    private Long organisateurId;
    private String organisateurNom;

    // Constructeurs
    public ConcertDTO() {
    }

    // Getters et Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getArtiste() {
        return artiste;
    }

    public void setArtiste(String artiste) {
        this.artiste = artiste;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getDateConcert() {
        return dateConcert;
    }

    public void setDateConcert(LocalDate dateConcert) {
        this.dateConcert = dateConcert;
    }

    public LocalTime getHeureConcert() {
        return heureConcert;
    }

    public void setHeureConcert(LocalTime heureConcert) {
        this.heureConcert = heureConcert;
    }

    public String getLieu() {
        return lieu;
    }

    public void setLieu(String lieu) {
        this.lieu = lieu;
    }

    public String getVille() {
        return ville;
    }

    public void setVille(String ville) {
        this.ville = ville;
    }

    public Genre getGenre() {
        return genre;
    }

    public void setGenre(Genre genre) {
        this.genre = genre;
    }

    public BigDecimal getPrix() {
        return prix;
    }

    public void setPrix(BigDecimal prix) {
        this.prix = prix;
    }

    public Integer getCapacite() {
        return capacite;
    }

    public void setCapacite(Integer capacite) {
        this.capacite = capacite;
    }

    public Integer getTicketsDisponibles() {
        return ticketsDisponibles;
    }

    public void setTicketsDisponibles(Integer ticketsDisponibles) {
        this.ticketsDisponibles = ticketsDisponibles;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Long getOrganisateurId() {
        return organisateurId;
    }

    public void setOrganisateurId(Long organisateurId) {
        this.organisateurId = organisateurId;
    }

    public String getOrganisateurNom() {
        return organisateurNom;
    }

    public void setOrganisateurNom(String organisateurNom) {
        this.organisateurNom = organisateurNom;
    }
}
