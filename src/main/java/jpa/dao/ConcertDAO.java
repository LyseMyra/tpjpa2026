package jpa.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jpa.entity.Concert;
import jpa.entity.Genre;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * DAO pour l'entité Concert
 * Contient les méthodes CRUD + requêtes JPQL + requêtes nommées + méthodes métier
 */
public class ConcertDAO implements GenericDAO<Concert> {

    private EntityManager em;

    public ConcertDAO(EntityManager em) {
        this.em = em;
    }

    // ========== Méthodes CRUD de base ==========

    @Override
    public Concert create(Concert concert) {
        em.persist(concert);
        return concert;
    }

    @Override
    public Concert findById(Long id) {
        return em.find(Concert.class, id);
    }

    @Override
    public List<Concert> findAll() {
        return em.createQuery("SELECT c FROM Concert c ORDER BY c.dateConcert", Concert.class)
                .getResultList();
    }

    @Override
    public Concert update(Concert concert) {
        return em.merge(concert);
    }

    @Override
    public void delete(Long id) {
        Concert concert = findById(id);
        if (concert != null) {
            em.remove(concert);
        }
    }

    @Override
    public long count() {
        return em.createQuery("SELECT COUNT(c) FROM Concert c", Long.class)
                .getSingleResult();
    }

    // ========== Requêtes JPQL ==========

    /**
     * Recherche des concerts par genre (JPQL)
     * @param genre Le genre musical
     * @return Liste des concerts de ce genre
     */
    public List<Concert> findByGenreJPQL(Genre genre) {
        String jpql = "SELECT c FROM Concert c WHERE c.genre = :genre AND c.actif = true ORDER BY c.dateConcert";
        return em.createQuery(jpql, Concert.class)
                .setParameter("genre", genre)
                .getResultList();
    }

    /**
     * Recherche des concerts dans une fourchette de prix (JPQL)
     * @param prixMin Prix minimum
     * @param prixMax Prix maximum
     * @return Liste des concerts dans cette fourchette
     */
    public List<Concert> findByPrixRange(BigDecimal prixMin, BigDecimal prixMax) {
        String jpql = "SELECT c FROM Concert c WHERE c.prix BETWEEN :prixMin AND :prixMax AND c.actif = true";
        return em.createQuery(jpql, Concert.class)
                .setParameter("prixMin", prixMin)
                .setParameter("prixMax", prixMax)
                .getResultList();
    }

    // ========== Requêtes nommées (@NamedQuery) ==========

    /**
     * Recherche des concerts par ville (utilise @NamedQuery)
     * @param ville La ville
     * @return Liste des concerts dans cette ville
     */
    public List<Concert> findByVille(String ville) {
        return em.createNamedQuery("Concert.findByVille", Concert.class)
                .setParameter("ville", ville)
                .getResultList();
    }

    /**
     * Recherche des concerts par genre (utilise @NamedQuery)
     * @param genre Le genre
     * @return Liste des concerts de ce genre
     */
    public List<Concert> findByGenre(Genre genre) {
        return em.createNamedQuery("Concert.findByGenre", Concert.class)
                .setParameter("genre", genre)
                .getResultList();
    }

    /**
     * Trouve tous les concerts actifs (utilise @NamedQuery)
     * @return Liste des concerts actifs
     */
    public List<Concert> findActifs() {
        return em.createNamedQuery("Concert.findActifs", Concert.class)
                .getResultList();
    }

    // ========== Méthodes métier ==========

    /**
     * Trouve les concerts disponibles (avec des tickets restants)
     * Utilise une @NamedQuery
     * @return Liste des concerts disponibles
     */
    public List<Concert> findConcertsDisponibles() {
        return em.createNamedQuery("Concert.findDisponibles", Concert.class)
                .getResultList();
    }

    /**
     * Trouve les concerts dans une plage de dates
     * @param dateDebut Date de début
     * @param dateFin Date de fin
     * @return Liste des concerts dans cette plage
     */
    public List<Concert> findByDateRange(LocalDate dateDebut, LocalDate dateFin) {
        String jpql = "SELECT c FROM Concert c WHERE c.dateConcert BETWEEN :debut AND :fin AND c.actif = true ORDER BY c.dateConcert";
        return em.createQuery(jpql, Concert.class)
                .setParameter("debut", dateDebut)
                .setParameter("fin", dateFin)
                .getResultList();
    }

    /**
     * Réserve des tickets pour un concert
     * Méthode métier qui met à jour le nombre de tickets disponibles
     * @param concertId ID du concert
     * @param nombreTickets Nombre de tickets à réserver
     * @return true si la réservation a réussi, false sinon
     */
    public boolean reserverTickets(Long concertId, int nombreTickets) {
        Concert concert = findById(concertId);
        if (concert == null) {
            return false;
        }

        if (concert.getTicketsDisponibles() >= nombreTickets) {
            concert.setTicketsDisponibles(concert.getTicketsDisponibles() - nombreTickets);
            update(concert);
            return true;
        }
        return false;
    }

    /**
     * Libère des tickets (en cas d'annulation)
     * @param concertId ID du concert
     * @param nombreTickets Nombre de tickets à libérer
     */
    public void libererTickets(Long concertId, int nombreTickets) {
        Concert concert = findById(concertId);
        if (concert != null) {
            int nouveauNombre = concert.getTicketsDisponibles() + nombreTickets;
            // Ne pas dépasser la capacité totale
            if (nouveauNombre > concert.getCapacite()) {
                nouveauNombre = concert.getCapacite();
            }
            concert.setTicketsDisponibles(nouveauNombre);
            update(concert);
        }
    }

    /**
     * Recherche les concerts d'un artiste
     * @param artiste Nom de l'artiste
     * @return Liste des concerts de cet artiste
     */
    public List<Concert> findByArtiste(String artiste) {
        String jpql = "SELECT c FROM Concert c WHERE LOWER(c.artiste) LIKE LOWER(:artiste) AND c.actif = true";
        return em.createQuery(jpql, Concert.class)
                .setParameter("artiste", "%" + artiste + "%")
                .getResultList();
    }

    /**
     * Trouve les concerts d'un organisateur
     * @param organisateurId ID de l'organisateur
     * @return Liste des concerts de cet organisateur
     */
    public List<Concert> findByOrganisateur(Long organisateurId) {
        String jpql = "SELECT c FROM Concert c WHERE c.organisateur.id = :orgId ORDER BY c.dateConcert";
        return em.createQuery(jpql, Concert.class)
                .setParameter("orgId", organisateurId)
                .getResultList();
    }

    /**
     * Statistiques : nombre de tickets vendus pour un concert
     * @param concertId ID du concert
     * @return Nombre de tickets vendus
     */
    public int getNombreTicketsVendus(Long concertId) {
        Concert concert = findById(concertId);
        if (concert != null) {
            return concert.getCapacite() - concert.getTicketsDisponibles();
        }
        return 0;
    }

    /**
     * Vérifie si un concert est complet
     * @param concertId ID du concert
     * @return true si le concert est complet
     */
    public boolean isConcertComplet(Long concertId) {
        Concert concert = findById(concertId);
        return concert != null && concert.getTicketsDisponibles() == 0;
    }
}
