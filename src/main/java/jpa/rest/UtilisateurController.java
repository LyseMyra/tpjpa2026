package jpa.rest;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jpa.EntityManagerHelper;
import jpa.dao.UtilisateurDAO;
import jpa.entity.Utilisateur;

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
            em.close();
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
            em.close();
        }
    }

    @POST
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
            em.close();
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
            return Response.ok(user.getTickets()).build();
        } finally {
            em.close();
        }
    }
}
