package jpa.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jpa.entity.Genre;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

/**
 * DTO pour l'entité Concert
 * Ne contient que les informations publiques (pas de relations sensibles)
 */
public class ConcertDTO {

    @Schema(example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;
    @Schema(example = "Nuit Électro Paris")
    private String nom;
    @Schema(example = "David Guetta")
    private String artiste;
    @Schema(example = "Un concert électro exceptionnel au cœur de Paris")
    private String description;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @Schema(example = "2026-07-14", type = "string", format = "date")
    private LocalDate dateConcert;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss")
    @Schema(example = "21:00:00", type = "string")
    private LocalTime heureConcert;
    @Schema(example = "Accor Arena")
    private String lieu;
    @Schema(example = "Paris")
    private String ville;
    @Schema(example = "ELECTRO")
    private Genre genre;
    @Schema(example = "49.99")
    private BigDecimal prix;
    @Schema(example = "5000")
    private Integer capacite;
    @Schema(example = "4850", accessMode = Schema.AccessMode.READ_ONLY)
    private Integer ticketsDisponibles;
    @Schema(example = "https://example.com/images/concert.jpg")
    private String image;
    @Schema(example = "3")
    private Long organisateurId;
    @Schema(example = "Live Nation France", accessMode = Schema.AccessMode.READ_ONLY)
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
