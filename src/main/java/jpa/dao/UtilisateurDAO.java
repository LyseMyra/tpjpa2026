package jpa.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jpa.entity.Utilisateur;

import java.util.List;

/**
 * DAO pour l'entité Utilisateur
 */
public class UtilisateurDAO implements GenericDAO<Utilisateur> {

    private EntityManager em;

    public UtilisateurDAO(EntityManager em) {
        this.em = em;
    }

    // ========== Méthodes CRUD de base ==========

    @Override
    public Utilisateur create(Utilisateur utilisateur) {
        em.persist(utilisateur);
        return utilisateur;
    }

    @Override
    public Utilisateur findById(Long id) {
        return em.find(Utilisateur.class, id);
    }

    @Override
    public List<Utilisateur> findAll() {
        return em.createQuery("SELECT u FROM Utilisateur u ORDER BY u.nom, u.prenom", Utilisateur.class)
                .getResultList();
    }

    @Override
    public Utilisateur update(Utilisateur utilisateur) {
        return em.merge(utilisateur);
    }

    @Override
    public void delete(Long id) {
        Utilisateur utilisateur = findById(id);
        if (utilisateur != null) {
            em.remove(utilisateur);
        }
    }

    @Override
    public long count() {
        return em.createQuery("SELECT COUNT(u) FROM Utilisateur u", Long.class)
                .getSingleResult();
    }

    // ========== Méthodes spécifiques ==========

    /**
     * Recherche un utilisateur par email
     * @param email L'email de l'utilisateur
     * @return L'utilisateur trouvé ou null
     */
    public Utilisateur findByEmail(String email) {
        try {
            String jpql = "SELECT u FROM Utilisateur u WHERE u.email = :email";
            return em.createQuery(jpql, Utilisateur.class)
                    .setParameter("email", email)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    /**
     * Recherche des utilisateurs par nom
     * @param nom Le nom (partiel ou complet)
     * @return Liste des utilisateurs correspondants
     */
    public List<Utilisateur> findByNom(String nom) {
        String jpql = "SELECT u FROM Utilisateur u WHERE LOWER(u.nom) LIKE LOWER(:nom) ORDER BY u.nom";
        return em.createQuery(jpql, Utilisateur.class)
                .setParameter("nom", "%" + nom + "%")
                .getResultList();
    }

    /**
     * Trouve tous les utilisateurs actifs
     * @return Liste des utilisateurs actifs
     */
    public List<Utilisateur> findActifs() {
        String jpql = "SELECT u FROM Utilisateur u WHERE u.actif = true ORDER BY u.dateInscription DESC";
        return em.createQuery(jpql, Utilisateur.class)
                .getResultList();
    }

    /**
     * Authentifie un utilisateur
     * @param email Email de l'utilisateur
     * @param motDePasse Mot de passe
     * @return L'utilisateur si l'authentification réussit, null sinon
     */
    public Utilisateur authenticate(String email, String motDePasse) {
        try {
            String jpql = "SELECT u FROM Utilisateur u WHERE u.email = :email AND u.motDePasse = :mdp AND u.actif = true";
            return em.createQuery(jpql, Utilisateur.class)
                    .setParameter("email", email)
                    .setParameter("mdp", motDePasse)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    /**
     * Compte le nombre de tickets achetés par un utilisateur
     * @param utilisateurId ID de l'utilisateur
     * @return Nombre de tickets achetés
     */
    public long countTicketsAchetes(Long utilisateurId) {
        String jpql = "SELECT COUNT(t) FROM Ticket t WHERE t.utilisateur.id = :userId";
        return em.createQuery(jpql, Long.class)
                .setParameter("userId", utilisateurId)
                .getSingleResult();
    }
}
