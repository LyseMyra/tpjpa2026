package jpa.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jpa.entity.Organisateur;

import java.util.List;

/**
 * DAO pour l'entité Organisateur
 */
public class OrganisateurDAO implements GenericDAO<Organisateur> {

    private EntityManager em;

    public OrganisateurDAO(EntityManager em) {
        this.em = em;
    }

    // ========== Méthodes CRUD de base ==========

    @Override
    public Organisateur create(Organisateur organisateur) {
        em.persist(organisateur);
        return organisateur;
    }

    @Override
    public Organisateur findById(Long id) {
        return em.find(Organisateur.class, id);
    }

    @Override
    public List<Organisateur> findAll() {
        return em.createQuery("SELECT o FROM Organisateur o ORDER BY o.nomOrganisation", Organisateur.class)
                .getResultList();
    }

    @Override
    public Organisateur update(Organisateur organisateur) {
        return em.merge(organisateur);
    }

    @Override
    public void delete(Long id) {
        Organisateur organisateur = findById(id);
        if (organisateur != null) {
            em.remove(organisateur);
        }
    }

    @Override
    public long count() {
        return em.createQuery("SELECT COUNT(o) FROM Organisateur o", Long.class)
                .getSingleResult();
    }

    // ========== Méthodes spécifiques ==========

    /**
     * Recherche un organisateur par email
     * @param email L'email de l'organisateur
     * @return L'organisateur trouvé ou null
     */
    public Organisateur findByEmail(String email) {
        try {
            String jpql = "SELECT o FROM Organisateur o WHERE o.email = :email";
            return em.createQuery(jpql, Organisateur.class)
                    .setParameter("email", email)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    /**
     * Recherche un organisateur par SIRET
     * @param siret Le numéro SIRET
     * @return L'organisateur trouvé ou null
     */
    public Organisateur findBySiret(String siret) {
        try {
            String jpql = "SELECT o FROM Organisateur o WHERE o.siret = :siret";
            return em.createQuery(jpql, Organisateur.class)
                    .setParameter("siret", siret)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    /**
     * Trouve tous les organisateurs validés
     * @return Liste des organisateurs validés
     */
    public List<Organisateur> findValides() {
        String jpql = "SELECT o FROM Organisateur o WHERE o.valide = true AND o.actif = true ORDER BY o.nomOrganisation";
        return em.createQuery(jpql, Organisateur.class)
                .getResultList();
    }

    /**
     * Trouve tous les organisateurs en attente de validation
     * @return Liste des organisateurs en attente
     */
    public List<Organisateur> findEnAttenteValidation() {
        String jpql = "SELECT o FROM Organisateur o WHERE o.valide = false AND o.actif = true ORDER BY o.dateInscription";
        return em.createQuery(jpql, Organisateur.class)
                .getResultList();
    }

    /**
     * Valide un organisateur
     * @param organisateurId ID de l'organisateur
     * @return true si la validation a réussi
     */
    public boolean valider(Long organisateurId) {
        Organisateur org = findById(organisateurId);
        if (org != null && !org.getValide()) {
            org.setValide(true);
            update(org);
            return true;
        }
        return false;
    }

    /**
     * Authentifie un organisateur
     * @param email Email de l'organisateur
     * @param motDePasse Mot de passe
     * @return L'organisateur si l'authentification réussit, null sinon
     */
    public Organisateur authenticate(String email, String motDePasse) {
        try {
            String jpql = "SELECT o FROM Organisateur o WHERE o.email = :email AND o.motDePasse = :mdp AND o.actif = true AND o.valide = true";
            return em.createQuery(jpql, Organisateur.class)
                    .setParameter("email", email)
                    .setParameter("mdp", motDePasse)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    /**
     * Compte le nombre de concerts organisés par un organisateur
     * @param organisateurId ID de l'organisateur
     * @return Nombre de concerts organisés
     */
    public long countConcertsOrganises(Long organisateurId) {
        String jpql = "SELECT COUNT(c) FROM Concert c WHERE c.organisateur.id = :orgId";
        return em.createQuery(jpql, Long.class)
                .setParameter("orgId", organisateurId)
                .getSingleResult();
    }
}
