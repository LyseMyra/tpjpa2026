package jpa.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jpa.EntityManagerHelper;
import jpa.dao.ConcertDAO;
import jpa.dao.OrganisateurDAO;
import jpa.dao.TicketDAO;
import jpa.dao.UtilisateurDAO;
import jpa.dto.ConcertDTO;
import jpa.dto.ConcertMapper;
import jpa.entity.*;
import jpa.rest.dto.ReservationRequest;

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
            EntityManagerHelper.closeEntityManager();
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
            EntityManagerHelper.closeEntityManager();
        }
    }

    @POST
    @Operation(
        summary = "Crée un nouveau concert",
        description = "Crée un nouveau concert avec les informations fournies",
        requestBody = @RequestBody(
            required = true,
            content = @Content(
                schema = @Schema(implementation = ConcertDTO.class),
                examples = @ExampleObject(
                    name = "Exemple concert",
                    value = "{\"nom\":\"Nuit Electro Paris\",\"artiste\":\"David Guetta\",\"description\":\"Un concert electro exceptionnel\",\"dateConcert\":\"2026-07-14\",\"heureConcert\":\"21:00:00\",\"lieu\":\"Accor Arena\",\"ville\":\"Paris\",\"genre\":\"ELECTRO\",\"prix\":49.99,\"capacite\":5000,\"organisateurId\":1}"
                )
            )
        ),
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

            // ticketsDisponibles initialisé à capacite lors de la création
            if (concert.getTicketsDisponibles() == null && concert.getCapacite() != null) {
                concert.setTicketsDisponibles(concert.getCapacite());
            }

            // Rattacher l'organisateur depuis la DB
            if (concertDTO.getOrganisateurId() != null) {
                Organisateur organisateur = new OrganisateurDAO(em).findById(concertDTO.getOrganisateurId());
                if (organisateur == null) {
                    tx.rollback();
                    return Response.status(Response.Status.BAD_REQUEST)
                        .entity("{\"error\": \"Organisateur introuvable\"}")
                        .build();
                }
                concert.setOrganisateur(organisateur);
            }

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
            EntityManagerHelper.closeEntityManager();
        }
    }

    @PUT
    @Path("/{id}")
    @Operation(
        summary = "Met à jour un concert",
        description = "Met à jour les informations d'un concert existant",
        requestBody = @RequestBody(
            required = true,
            content = @Content(
                schema = @Schema(implementation = ConcertDTO.class),
                examples = @ExampleObject(
                    name = "Exemple mise a jour",
                    value = "{\"nom\":\"Nuit Electro Paris - Edition Speciale\",\"artiste\":\"David Guetta\",\"description\":\"Edition speciale avec DJ set exclusif\",\"dateConcert\":\"2026-07-14\",\"heureConcert\":\"22:00:00\",\"lieu\":\"Accor Arena\",\"ville\":\"Paris\",\"genre\":\"ELECTRO\",\"prix\":59.99,\"capacite\":5000}"
                )
            )
        ),
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
            EntityManagerHelper.closeEntityManager();
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
            EntityManagerHelper.closeEntityManager();
        }
    }

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
            EntityManagerHelper.closeEntityManager();
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
            EntityManagerHelper.closeEntityManager();
        }
    }

    @POST
    @Path("/{id}/reserver")
    @Operation(
        summary = "Réserve un ticket pour un concert",
        description = "Crée un ticket (STANDARD, PREMIUM ou LAST_MINUTE) lié au concert et à l'utilisateur, et décrémente les places disponibles.",
        requestBody = @RequestBody(
            required = true,
            content = @Content(
                schema = @Schema(implementation = jpa.rest.dto.ReservationRequest.class),
                examples = {
                    @ExampleObject(name = "Standard",    value = "{\"utilisateurId\":1,\"typeTicket\":\"STANDARD\",\"numeroPlace\":\"A12\",\"categorie\":\"Fosse\"}"),
                    @ExampleObject(name = "Premium",     value = "{\"utilisateurId\":1,\"typeTicket\":\"PREMIUM\",\"numeroPlace\":\"VIP-01\",\"categorie\":\"VIP\",\"accesCoulisses\":true,\"meetAndGreet\":false,\"parkingVIP\":true}"),
                    @ExampleObject(name = "Last Minute", value = "{\"utilisateurId\":1,\"typeTicket\":\"LAST_MINUTE\",\"pourcentageReduction\":30,\"zoneAcces\":\"Debout uniquement\"}")
                }
            )
        ),
        responses = {
            @ApiResponse(responseCode = "201", description = "Ticket créé"),
            @ApiResponse(responseCode = "400", description = "Réservation impossible (plus de places, type invalide…)"),
            @ApiResponse(responseCode = "404", description = "Concert ou utilisateur introuvable")
        }
    )
    public Response reserverTicket(
        @PathParam("id") Long concertId,
        jpa.rest.dto.ReservationRequest req
    ) {
        if (req == null || req.getUtilisateurId() == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                .entity("{\"error\": \"utilisateurId est obligatoire\"}")
                .build();
        }

        String type = req.getTypeTicket() != null ? req.getTypeTicket().toUpperCase() : "STANDARD";

        EntityManager em = EntityManagerHelper.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();

            Concert concert = new ConcertDAO(em).findById(concertId);
            if (concert == null) {
                tx.rollback();
                return Response.status(Response.Status.NOT_FOUND)
                    .entity("{\"error\": \"Concert introuvable\"}")
                    .build();
            }

            if (concert.getTicketsDisponibles() == null || concert.getTicketsDisponibles() <= 0) {
                tx.rollback();
                return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"error\": \"Plus de tickets disponibles\"}")
                    .build();
            }

            Utilisateur utilisateur = new UtilisateurDAO(em).findById(req.getUtilisateurId());
            if (utilisateur == null) {
                tx.rollback();
                return Response.status(Response.Status.NOT_FOUND)
                    .entity("{\"error\": \"Utilisateur introuvable\"}")
                    .build();
            }

            Ticket ticket;
            switch (type) {
                case "PREMIUM":
                    ticket = new TicketPremium(
                        concert, utilisateur, concert.getPrix(),
                        req.getNumeroPlace(), req.getCategorie(),
                        Boolean.TRUE.equals(req.getAccesCoulisses()),
                        Boolean.TRUE.equals(req.getMeetAndGreet()),
                        Boolean.TRUE.equals(req.getParkingVIP())
                    );
                    break;
                case "LAST_MINUTE":
                    int reduction = req.getPourcentageReduction() != null ? req.getPourcentageReduction() : 0;
                    java.math.BigDecimal prixReduit = concert.getPrix()
                        .multiply(java.math.BigDecimal.valueOf(100 - reduction))
                        .divide(java.math.BigDecimal.valueOf(100));
                    ticket = new TicketLastMinute(
                        concert, utilisateur, prixReduit,
                        reduction, req.getZoneAcces()
                    );
                    break;
                case "STANDARD":
                default:
                    ticket = new TicketStandard(
                        concert, utilisateur, concert.getPrix(),
                        req.getNumeroPlace(), req.getCategorie()
                    );
                    break;
            }

            new TicketDAO(em).create(ticket);
            concert.setTicketsDisponibles(concert.getTicketsDisponibles() - 1);

            tx.commit();

            String json = String.format(
                "{\"ticketId\":%d,\"numeroTicket\":\"%s\",\"typeTicket\":\"%s\",\"prixAchat\":%s,\"concertId\":%d,\"utilisateurId\":%d}",
                ticket.getId(), ticket.getNumeroTicket(), ticket.getTypeTicket(),
                ticket.getPrixAchat(), concertId, req.getUtilisateurId()
            );
            return Response.status(Response.Status.CREATED).entity(json).build();

        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            return Response.status(Response.Status.BAD_REQUEST)
                .entity("{\"error\": \"" + e.getMessage() + "\"}")
                .build();
        } finally {
            EntityManagerHelper.closeEntityManager();
        }
    }
}
