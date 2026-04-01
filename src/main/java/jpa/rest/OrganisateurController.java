package jpa.rest;

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
