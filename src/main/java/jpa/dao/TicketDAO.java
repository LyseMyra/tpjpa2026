package jpa.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.*;
import jpa.entity.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO pour l'entité Ticket
 * Contient les méthodes CRUD + Criteria Query + méthodes métier
 */
public class TicketDAO implements GenericDAO<Ticket> {

    private EntityManager em;

    public TicketDAO(EntityManager em) {
        this.em = em;
    }

    @Override
    public Ticket create(Ticket ticket) {
        em.persist(ticket);
        return ticket;
    }

    @Override
    public Ticket findById(Long id) {
        return em.find(Ticket.class, id);
    }

    @Override
    public List<Ticket> findAll() {
        return em.createQuery("SELECT t FROM Ticket t ORDER BY t.dateAchat DESC", Ticket.class)
                .getResultList();
    }

    @Override
    public Ticket update(Ticket ticket) {
        return em.merge(ticket);
    }

    @Override
    public void delete(Long id) {
        Ticket ticket = findById(id);
        if (ticket != null) {
            em.remove(ticket);
        }
    }

    @Override
    public long count() {
        return em.createQuery("SELECT COUNT(t) FROM Ticket t", Long.class)
                .getSingleResult();
    }

    // ========== Criteria Query (requis par le TP) ==========

    /**
     * Recherche de tickets avec des filtres multiples en utilisant Criteria Query
     * @param utilisateurId ID de l'utilisateur (optionnel)
     * @param concertId ID du concert (optionnel)
     * @param typeTicket Type de ticket: "STANDARD", "PREMIUM", "LAST_MINUTE" (optionnel)
     * @param valide true pour tickets valides uniquement (optionnel)
     * @return Liste des tickets correspondant aux critères
     */
    public List<Ticket> findByCriteria(Long utilisateurId, Long concertId, String typeTicket, Boolean valide) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Ticket> cq = cb.createQuery(Ticket.class);
        Root<Ticket> ticket = cq.from(Ticket.class);

        List<Predicate> predicates = new ArrayList<>();

        // Filtre par utilisateur
        if (utilisateurId != null) {
            predicates.add(cb.equal(ticket.get("utilisateur").get("id"), utilisateurId));
        }

        // Filtre par concert
        if (concertId != null) {
            predicates.add(cb.equal(ticket.get("concert").get("id"), concertId));
        }

        // Filtre par type de ticket (utilise le discriminateur de l'héritage)
        if (typeTicket != null && !typeTicket.isEmpty()) {
            switch (typeTicket.toUpperCase()) {
                case "STANDARD":
                    predicates.add(cb.equal(ticket.type(), TicketStandard.class));
                    break;
                case "PREMIUM":
                    predicates.add(cb.equal(ticket.type(), TicketPremium.class));
                    break;
                case "LAST_MINUTE":
                    predicates.add(cb.equal(ticket.type(), TicketLastMinute.class));
                    break;
            }
        }

        // Filtre par validité
        if (valide != null) {
            predicates.add(cb.equal(ticket.get("valide"), valide));
        }

        // Combiner tous les prédicats avec AND
        cq.where(cb.and(predicates.toArray(new Predicate[0])));

        // Ordonner par date d'achat décroissante
        cq.orderBy(cb.desc(ticket.get("dateAchat")));

        return em.createQuery(cq).getResultList();
    }

    /**
     * Recherche de tickets avec un prix dans une fourchette (Criteria Query)
     * @param prixMin Prix minimum
     * @param prixMax Prix maximum
     * @return Liste des tickets dans cette fourchette de prix
     */
    public List<Ticket> findByPrixRange(BigDecimal prixMin, BigDecimal prixMax) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Ticket> cq = cb.createQuery(Ticket.class);
        Root<Ticket> ticket = cq.from(Ticket.class);

        Predicate prixCondition = cb.between(ticket.get("prixAchat"), prixMin, prixMax);
        cq.where(prixCondition);
        cq.orderBy(cb.asc(ticket.get("prixAchat")));

        return em.createQuery(cq).getResultList();
    }

    // ========== Méthodes métier ==========

    /**
     * Trouve tous les tickets d'un utilisateur
     * @param utilisateurId ID de l'utilisateur
     * @return Liste des tickets de cet utilisateur
     */
    public List<Ticket> findTicketsByUtilisateur(Long utilisateurId) {
        String jpql = "SELECT t FROM Ticket t WHERE t.utilisateur.id = :userId ORDER BY t.dateAchat DESC";
        return em.createQuery(jpql, Ticket.class)
                .setParameter("userId", utilisateurId)
                .getResultList();
    }

    /**
     * Trouve tous les tickets pour un concert
     * @param concertId ID du concert
     * @return Liste des tickets pour ce concert
     */
    public List<Ticket> findTicketsByConcert(Long concertId) {
        String jpql = "SELECT t FROM Ticket t WHERE t.concert.id = :concertId ORDER BY t.dateAchat";
        return em.createQuery(jpql, Ticket.class)
                .setParameter("concertId", concertId)
                .getResultList();
    }

    /**
     * Annule un ticket (le marque comme invalide)
     * Méthode métier
     * @param ticketId ID du ticket à annuler
     * @return true si l'annulation a réussi, false sinon
     */
    public boolean annulerTicket(Long ticketId) {
        Ticket ticket = findById(ticketId);
        if (ticket != null && ticket.getValide() && !ticket.getUtilise()) {
            ticket.setValide(false);
            update(ticket);
            return true;
        }
        return false;
    }

    /**
     * Marque un ticket comme utilisé
     * @param ticketId ID du ticket
     * @return true si le ticket a été marqué comme utilisé
     */
    public boolean utiliserTicket(Long ticketId) {
        Ticket ticket = findById(ticketId);
        if (ticket != null && ticket.getValide() && !ticket.getUtilise()) {
            ticket.setUtilise(true);
            update(ticket);
            return true;
        }
        return false;
    }

    /**
     * Trouve tous les tickets standard
     * @return Liste des tickets standard
     */
    public List<TicketStandard> findTicketsStandard() {
        return em.createQuery("SELECT t FROM TicketStandard t ORDER BY t.dateAchat DESC", TicketStandard.class)
                .getResultList();
    }

    /**
     * Trouve tous les tickets premium
     * @return Liste des tickets premium
     */
    public List<TicketPremium> findTicketsPremium() {
        return em.createQuery("SELECT t FROM TicketPremium t ORDER BY t.dateAchat DESC", TicketPremium.class)
                .getResultList();
    }

    /**
     * Trouve tous les tickets last minute
     * @return Liste des tickets last minute
     */
    public List<TicketLastMinute> findTicketsLastMinute() {
        return em.createQuery("SELECT t FROM TicketLastMinute t ORDER BY t.dateAchat DESC", TicketLastMinute.class)
                .getResultList();
    }

    /**
     * Calcule le revenu total généré par les ventes de tickets
     * @return Montant total des ventes
     */
    public BigDecimal calculerRevenuTotal() {
        String jpql = "SELECT SUM(t.prixAchat) FROM Ticket t WHERE t.valide = true";
        BigDecimal revenu = em.createQuery(jpql, BigDecimal.class).getSingleResult();
        return revenu != null ? revenu : BigDecimal.ZERO;
    }

    /**
     * Calcule le revenu pour un concert spécifique
     * @param concertId ID du concert
     * @return Montant total des ventes pour ce concert
     */
    public BigDecimal calculerRevenuConcert(Long concertId) {
        String jpql = "SELECT SUM(t.prixAchat) FROM Ticket t WHERE t.concert.id = :concertId AND t.valide = true";
        BigDecimal revenu = em.createQuery(jpql, BigDecimal.class)
                .setParameter("concertId", concertId)
                .getSingleResult();
        return revenu != null ? revenu : BigDecimal.ZERO;
    }

    /**
     * Compte le nombre de tickets par type pour un concert
     * @param concertId ID du concert
     * @param typeClass Classe du type de ticket
     * @return Nombre de tickets de ce type
     */
    public long countTicketsByType(Long concertId, Class<? extends Ticket> typeClass) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Long> cq = cb.createQuery(Long.class);
        Root<Ticket> ticket = cq.from(Ticket.class);

        cq.select(cb.count(ticket));
        cq.where(
            cb.and(
                cb.equal(ticket.get("concert").get("id"), concertId),
                cb.equal(ticket.type(), typeClass)
            )
        );

        return em.createQuery(cq).getSingleResult();
    }

    /**
     * Trouve les tickets achetés dans une période
     * @param dateDebut Date de début
     * @param dateFin Date de fin
     * @return Liste des tickets achetés dans cette période
     */
    public List<Ticket> findTicketsByPeriode(LocalDateTime dateDebut, LocalDateTime dateFin) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Ticket> cq = cb.createQuery(Ticket.class);
        Root<Ticket> ticket = cq.from(Ticket.class);

        cq.where(cb.between(ticket.get("dateAchat"), dateDebut, dateFin));
        cq.orderBy(cb.desc(ticket.get("dateAchat")));

        return em.createQuery(cq).getResultList();
    }
}
