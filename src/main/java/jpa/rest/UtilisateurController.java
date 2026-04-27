package jpa.rest;

import io.swagger.v3.oas.annotations.Operation;
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
import jpa.dao.UtilisateurDAO;
import jpa.entity.Utilisateur;

import java.util.ArrayList;
import java.util.List;

/**
 * Controller REST pour les utilisateurs
 */
@Path("/utilisateurs")
@Tag(name = "Utilisateurs", description = "Gestion des utilisateurs")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UtilisateurController {

    @GET
    public Response getAllUtilisateurs() {
        EntityManager em = EntityManagerHelper.getEntityManager();
        try {
            UtilisateurDAO dao = new UtilisateurDAO(em);
            List<Utilisateur> users = dao.findAll();
            return Response.ok(users).build();
        } finally {
            EntityManagerHelper.closeEntityManager();
        }
    }

    @GET
    @Path("/{id}")
    public Response getUtilisateurById(@PathParam("id") Long id) {
        EntityManager em = EntityManagerHelper.getEntityManager();
        try {
            UtilisateurDAO dao = new UtilisateurDAO(em);
            Utilisateur user = dao.findById(id);
            if (user == null) {
                return Response.status(Response.Status.NOT_FOUND).build();
            }
            return Response.ok(user).build();
        } finally {
            EntityManagerHelper.closeEntityManager();
        }
    }

    @POST
    @Operation(
        summary = "Crée un nouvel utilisateur",
        description = "Crée un nouvel utilisateur. La dateInscription est générée automatiquement.",
        requestBody = @RequestBody(
            required = true,
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON,
                schema = @Schema(implementation = Utilisateur.class),
                examples = @ExampleObject(
                    name = "Exemple utilisateur",
                    value = "{\"email\":\"jean.dupont@email.com\",\"motDePasse\":\"motdepasse123\",\"nom\":\"Dupont\",\"prenom\":\"Jean\",\"telephone\":\"0612345678\"}"
                )
            )
        ),
        responses = {
            @ApiResponse(responseCode = "201", description = "Utilisateur créé avec succès"),
            @ApiResponse(responseCode = "400", description = "Données invalides")
        }
    )
    public Response createUtilisateur(Utilisateur utilisateur) {
        EntityManager em = EntityManagerHelper.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            UtilisateurDAO dao = new UtilisateurDAO(em);
            Utilisateur created = dao.create(utilisateur);
            tx.commit();
            return Response.status(Response.Status.CREATED).entity(created).build();
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            return Response.status(Response.Status.BAD_REQUEST).entity("{\"error\": \"" + e.getMessage() + "\"}").build();
        } finally {
            EntityManagerHelper.closeEntityManager();
        }
    }

    @GET
    @Path("/{id}/tickets")
    public Response getTicketsUtilisateur(@PathParam("id") Long id) {
        EntityManager em = EntityManagerHelper.getEntityManager();
        try {
            UtilisateurDAO dao = new UtilisateurDAO(em);
            Utilisateur user = dao.findById(id);
            if (user == null) {
                return Response.status(Response.Status.NOT_FOUND).build();
            }
            List<jpa.entity.Ticket> tickets = new ArrayList<>(user.getTickets());
            return Response.ok(tickets).build();
        } finally {
            EntityManagerHelper.closeEntityManager();
        }
    }
}
