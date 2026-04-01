package jpa.dao;

import java.util.List;

/**
 * Interface générique pour les opérations CRUD de base
 * @param <T> Type de l'entité
 */
public interface GenericDAO<T> {

    /**
     * Crée une nouvelle entité dans la base de données
     * @param entity L'entité à créer
     * @return L'entité créée avec son ID généré
     */
    T create(T entity);

    /**
     * Trouve une entité par son ID
     * @param id L'identifiant de l'entité
     * @return L'entité trouvée ou null si non trouvée
     */
    T findById(Long id);

    /**
     * Récupère toutes les entités
     * @return Liste de toutes les entités
     */
    List<T> findAll();

    /**
     * Met à jour une entité existante
     * @param entity L'entité à mettre à jour
     * @return L'entité mise à jour
     */
    T update(T entity);

    /**
     * Supprime une entité par son ID
     * @param id L'identifiant de l'entité à supprimer
     */
    void delete(Long id);

    /**
     * Compte le nombre total d'entités
     * @return Le nombre d'entités
     */
    long count();
}
