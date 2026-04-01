package jpa.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jpa.EntityManagerHelper;
import jpa.dao.ConcertDAO;
import jpa.dto.ConcertDTO;
import jpa.dto.ConcertMapper;
import jpa.entity.Concert;
import jpa.entity.Genre;

import java.time.LocalDate;
import java.util.List;

/**
 * Controller REST pour les concerts
 * Contient endpoints CRUD + endpoints métier + documentation OpenAPI
 */
@Path("/concerts")
@Tag(name = "Concerts", description = "Gestion des concerts")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ConcertController {


    @GET
    @Operation(
        summary = "Liste tous les concerts",
        description = "Retourne la liste complète des concerts disponibles",
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Liste des concerts récupérée avec succès",
                content = @Content(schema = @Schema(implementation = ConcertDTO.class))
            )
        }
    )
    public Response getAllConcerts() {
        EntityManager em = EntityManagerHelper.getEntityManager();
        try {
            ConcertDAO dao = new ConcertDAO(em);
            List<Concert> concerts = dao.findAll();
            List<ConcertDTO> dtos = ConcertMapper.toDTOList(concerts);
            return Response.ok(dtos).build();
        } finally {
            em.close();
        }
    }

    @GET
    @Path("/{id}")
    @Operation(
        summary = "Récupère un concert par son ID",
        description = "Retourne les détails complets d'un concert spécifique",
        responses = {
            @ApiResponse(responseCode = "200", description = "Concert trouvé"),
            @ApiResponse(responseCode = "404", description = "Concert non trouvé")
        }
    )
    public Response getConcertById(
        @Parameter(description = "ID du concert", required = true)
        @PathParam("id") Long id
    ) {
        EntityManager em = EntityManagerHelper.getEntityManager();
        try {
            ConcertDAO dao = new ConcertDAO(em);
            Concert concert = dao.findById(id);
            if (concert == null) {
                return Response.status(Response.Status.NOT_FOUND)
                    .entity("{\"error\": \"Concert non trouvé\"}")
                    .build();
            }
            ConcertDTO dto = ConcertMapper.toDTO(concert);
            return Response.ok(dto).build();
        } finally {
            em.close();
        }
    }

    @POST
    @Operation(
        summary = "Crée un nouveau concert",
        description = "Crée un nouveau concert avec les informations fournies",
        responses = {
            @ApiResponse(responseCode = "201", description = "Concert créé avec succès"),
            @ApiResponse(responseCode = "400", description = "Données invalides")
        }
    )
    public Response createConcert(ConcertDTO concertDTO) {
        EntityManager em = EntityManagerHelper.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            Concert concert = ConcertMapper.toEntity(concertDTO);
            ConcertDAO dao = new ConcertDAO(em);
            Concert created = dao.create(concert);
            tx.commit();
            ConcertDTO dto = ConcertMapper.toDTO(created);
            return Response.status(Response.Status.CREATED).entity(dto).build();
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            return Response.status(Response.Status.BAD_REQUEST)
                .entity("{\"error\": \"" + e.getMessage() + "\"}")
                .build();
        } finally {
            em.close();
        }
    }

    @PUT
    @Path("/{id}")
    @Operation(
        summary = "Met à jour un concert",
        description = "Met à jour les informations d'un concert existant",
        responses = {
            @ApiResponse(responseCode = "200", description = "Concert mis à jour"),
            @ApiResponse(responseCode = "404", description = "Concert non trouvé")
        }
    )
    public Response updateConcert(
        @PathParam("id") Long id,
        ConcertDTO concertDTO
    ) {
        EntityManager em = EntityManagerHelper.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            ConcertDAO dao = new ConcertDAO(em);
            Concert existing = dao.findById(id);
            if (existing == null) {
                tx.rollback();
                return Response.status(Response.Status.NOT_FOUND)
                    .entity("{\"error\": \"Concert non trouvé\"}")
                    .build();
            }

            // Mise à jour des champs
            existing.setNom(concertDTO.getNom());
            existing.setArtiste(concertDTO.getArtiste());
            existing.setDescription(concertDTO.getDescription());
            existing.setDateConcert(concertDTO.getDateConcert());
            existing.setHeureConcert(concertDTO.getHeureConcert());
            existing.setLieu(concertDTO.getLieu());
            existing.setVille(concertDTO.getVille());
            existing.setGenre(concertDTO.getGenre());
            existing.setPrix(concertDTO.getPrix());

            Concert updated = dao.update(existing);
            tx.commit();
            ConcertDTO dto = ConcertMapper.toDTO(updated);
            return Response.ok(dto).build();
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            return Response.status(Response.Status.BAD_REQUEST)
                .entity("{\"error\": \"" + e.getMessage() + "\"}")
                .build();
        } finally {
            em.close();
        }
    }

    @DELETE
    @Path("/{id}")
    @Operation(
        summary = "Supprime un concert",
        description = "Supprime un concert de la base de données",
        responses = {
            @ApiResponse(responseCode = "204", description = "Concert supprimé"),
            @ApiResponse(responseCode = "404", description = "Concert non trouvé")
        }
    )
    public Response deleteConcert(@PathParam("id") Long id) {
        EntityManager em = EntityManagerHelper.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            ConcertDAO dao = new ConcertDAO(em);
            Concert concert = dao.findById(id);
            if (concert == null) {
                tx.rollback();
                return Response.status(Response.Status.NOT_FOUND)
                    .entity("{\"error\": \"Concert non trouvé\"}")
                    .build();
            }
            dao.delete(id);
            tx.commit();
            return Response.noContent().build();
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            return Response.status(Response.Status.BAD_REQUEST)
                .entity("{\"error\": \"" + e.getMessage() + "\"}")
                .build();
        } finally {
            em.close();
        }
    }

    // ========== Endpoints métier ==========

    @GET
    @Path("/search")
    @Operation(
        summary = "Recherche de concerts avec filtres",
        description = "Recherche des concerts par ville, genre et/ou période",
        responses = {
            @ApiResponse(responseCode = "200", description = "Résultats de recherche")
        }
    )
    public Response searchConcerts(
        @Parameter(description = "Ville du concert") @QueryParam("ville") String ville,
        @Parameter(description = "Genre musical") @QueryParam("genre") String genre,
        @Parameter(description = "Date de début (YYYY-MM-DD)") @QueryParam("dateDebut") String dateDebut,
        @Parameter(description = "Date de fin (YYYY-MM-DD)") @QueryParam("dateFin") String dateFin
    ) {
        EntityManager em = EntityManagerHelper.getEntityManager();
        try {
            ConcertDAO dao = new ConcertDAO(em);
            List<Concert> concerts;

            // Filtre par ville
            if (ville != null && !ville.isEmpty()) {
                concerts = dao.findByVille(ville);
            }
            // Filtre par genre
            else if (genre != null && !genre.isEmpty()) {
                try {
                    Genre genreEnum = Genre.valueOf(genre.toUpperCase());
                    concerts = dao.findByGenre(genreEnum);
                } catch (IllegalArgumentException e) {
                    return Response.status(Response.Status.BAD_REQUEST)
                        .entity("{\"error\": \"Genre invalide\"}")
                        .build();
                }
            }
            // Filtre par période
            else if (dateDebut != null && dateFin != null) {
                LocalDate debut = LocalDate.parse(dateDebut);
                LocalDate fin = LocalDate.parse(dateFin);
                concerts = dao.findByDateRange(debut, fin);
            }
            // Par défaut: concerts disponibles
            else {
                concerts = dao.findConcertsDisponibles();
            }

            List<ConcertDTO> dtos = ConcertMapper.toDTOList(concerts);
            return Response.ok(dtos).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST)
                .entity("{\"error\": \"" + e.getMessage() + "\"}")
                .build();
        } finally {
            em.close();
        }
    }

    @GET
    @Path("/{id}/disponibilites")
    @Operation(
        summary = "Vérifie les disponibilités d'un concert",
        description = "Retourne le nombre de tickets disponibles pour un concert",
        responses = {
            @ApiResponse(responseCode = "200", description = "Disponibilités récupérées"),
            @ApiResponse(responseCode = "404", description = "Concert non trouvé")
        }
    )
    public Response getDisponibilites(@PathParam("id") Long id) {
        EntityManager em = EntityManagerHelper.getEntityManager();
        try {
            ConcertDAO dao = new ConcertDAO(em);
            Concert concert = dao.findById(id);
            if (concert == null) {
                return Response.status(Response.Status.NOT_FOUND)
                    .entity("{\"error\": \"Concert non trouvé\"}")
                    .build();
            }

            String json = String.format(
                "{\"concertId\": %d, \"ticketsDisponibles\": %d, \"capacite\": %d, \"complet\": %b}",
                concert.getId(),
                concert.getTicketsDisponibles(),
                concert.getCapacite(),
                concert.getTicketsDisponibles() == 0
            );

            return Response.ok(json).build();
        } finally {
            em.close();
        }
    }

    @POST
    @Path("/{id}/reserver")
    @Operation(
        summary = "Réserve des tickets pour un concert",
        description = "Réserve un nombre spécifié de tickets pour un concert",
        responses = {
            @ApiResponse(responseCode = "200", description = "Réservation réussie"),
            @ApiResponse(responseCode = "400", description = "Réservation impossible"),
            @ApiResponse(responseCode = "404", description = "Concert non trouvé")
        }
    )
    public Response reserverTickets(
        @PathParam("id") Long id,
        @Parameter(description = "Nombre de tickets à réserver") @QueryParam("nombre") int nombre
    ) {
        EntityManager em = EntityManagerHelper.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            ConcertDAO dao = new ConcertDAO(em);

            Concert concert = dao.findById(id);
            if (concert == null) {
                tx.rollback();
                return Response.status(Response.Status.NOT_FOUND)
                    .entity("{\"error\": \"Concert non trouvé\"}")
                    .build();
            }

            boolean success = dao.reserverTickets(id, nombre);
            tx.commit();

            if (success) {
                return Response.ok("{\"message\": \"Réservation réussie\", \"ticketsReserves\": " + nombre + "}").build();
            } else {
                return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"error\": \"Pas assez de tickets disponibles\"}")
                    .build();
            }
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            return Response.status(Response.Status.BAD_REQUEST)
                .entity("{\"error\": \"" + e.getMessage() + "\"}")
                .build();
        } finally {
            em.close();
        }
    }
}
