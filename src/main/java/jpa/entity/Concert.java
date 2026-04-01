package jpa.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Entité représentant un concert
 */
@Entity
@NamedQueries({
    @NamedQuery(
        name = "Concert.findByVille",
        query = "SELECT c FROM Concert c WHERE c.ville = :ville AND c.actif = true"
    ),
    @NamedQuery(
        name = "Concert.findByGenre",
        query = "SELECT c FROM Concert c WHERE c.genre = :genre AND c.actif = true ORDER BY c.dateConcert"
    ),
    @NamedQuery(
        name = "Concert.findActifs",
        query = "SELECT c FROM Concert c WHERE c.actif = true ORDER BY c.dateConcert"
    ),
    @NamedQuery(
        name = "Concert.findDisponibles",
        query = "SELECT c FROM Concert c WHERE c.ticketsDisponibles > 0 AND c.actif = true ORDER BY c.dateConcert"
    )
})
public class Concert {

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
    private Boolean actif = true;

    // Relations
    private Organisateur organisateur;
    private List<Ticket> tickets = new ArrayList<>();

    // Constructeurs
    public Concert() {
    }

    public Concert(String nom, String artiste, LocalDate dateConcert, LocalTime heureConcert,
                   String lieu, String ville, Genre genre, BigDecimal prix, Integer capacite) {
        this.nom = nom;
        this.artiste = artiste;
        this.dateConcert = dateConcert;
        this.heureConcert = heureConcert;
        this.lieu = lieu;
        this.ville = ville;
        this.genre = genre;
        this.prix = prix;
        this.capacite = capacite;
        this.ticketsDisponibles = capacite;
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

    @Column(nullable = false, length = 200)
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

    public Boolean getActif() {
        return actif;
    }

    public void setActif(Boolean actif) {
        this.actif = actif;
    }

    // Relation ManyToOne avec Organisateur (bidirectionnelle)
    @ManyToOne
    @JoinColumn(name = "organisateur_id")
    public Organisateur getOrganisateur() {
        return organisateur;
    }

    public void setOrganisateur(Organisateur organisateur) {
        this.organisateur = organisateur;
    }

    // Relation OneToMany avec Ticket (bidirectionnelle)
    @OneToMany(mappedBy = "concert", cascade = CascadeType.ALL)
    public List<Ticket> getTickets() {
        return tickets;
    }

    public void setTickets(List<Ticket> tickets) {
        this.tickets = tickets;
    }

    @Override
    public String toString() {
        return "Concert{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", artiste='" + artiste + '\'' +
                ", dateConcert=" + dateConcert +
                ", lieu='" + lieu + '\'' +
                ", ville='" + ville + '\'' +
                ", genre=" + genre +
                ", prix=" + prix +
                ", ticketsDisponibles=" + ticketsDisponibles +
                '}';
    }
}
