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
import jpa.dao.OrganisateurDAO;
import jpa.entity.Organisateur;

import java.util.List;

/**
 * Controller REST pour les organisateurs
 */
@Path("/organisateurs")
@Tag(name = "Organisateurs", description = "Gestion des organisateurs")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class OrganisateurController {

    @GET
    public Response getAllOrganisateurs() {
        EntityManager em = EntityManagerHelper.getEntityManager();
        try {
            OrganisateurDAO dao = new OrganisateurDAO(em);
            List<Organisateur> orgs = dao.findAll();
            return Response.ok(orgs).build();
        } finally {
            em.close();
        }
    }

    @GET
    @Path("/{id}")
    public Response getOrganisateurById(@PathParam("id") Long id) {
        EntityManager em = EntityManagerHelper.getEntityManager();
        try {
            OrganisateurDAO dao = new OrganisateurDAO(em);
            Organisateur org = dao.findById(id);
            if (org == null) {
                return Response.status(Response.Status.NOT_FOUND).build();
            }
            return Response.ok(org).build();
        } finally {
            em.close();
        }
    }

    @POST
    @Operation(
        summary = "Crée un nouvel organisateur",
        description = "Crée un nouvel organisateur. La dateInscription est générée automatiquement. Le compte nécessite une validation admin (valide=false par défaut).",
        requestBody = @RequestBody(
            required = true,
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON,
                schema = @Schema(implementation = Organisateur.class),
                examples = @ExampleObject(
                    name = "Exemple organisateur",
                    value = "{\"email\":\"contact@livenation.fr\",\"motDePasse\":\"motdepasse123\",\"nomOrganisation\":\"Live Nation France\",\"siret\":\"12345678901234\",\"telephone\":\"0140506070\",\"adresse\":\"1 Avenue des Champs-Elysees, 75008 Paris\"}"
                )
            )
        ),
        responses = {
            @ApiResponse(responseCode = "201", description = "Organisateur créé avec succès"),
            @ApiResponse(responseCode = "400", description = "Données invalides")
        }
    )
    public Response createOrganisateur(Organisateur organisateur) {
        EntityManager em = EntityManagerHelper.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            OrganisateurDAO dao = new OrganisateurDAO(em);
            Organisateur created = dao.create(organisateur);
            tx.commit();
            return Response.status(Response.Status.CREATED).entity(created).build();
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            return Response.status(Response.Status.BAD_REQUEST).entity("{\"error\": \"" + e.getMessage() + "\"}").build();
        } finally {
            em.close();
        }
    }

    @GET
    @Path("/{id}/concerts")
    public Response getConcertsOrganisateur(@PathParam("id") Long id) {
        EntityManager em = EntityManagerHelper.getEntityManager();
        try {
            OrganisateurDAO dao = new OrganisateurDAO(em);
            Organisateur org = dao.findById(id);
            if (org == null) {
                return Response.status(Response.Status.NOT_FOUND).build();
            }
            return Response.ok(org.getConcerts()).build();
        } finally {
            em.close();
        }
    }
}
