package jpa.rest;

import jakarta.persistence.EntityManager;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jpa.EntityManagerHelper;
import jpa.dao.TicketDAO;
import jpa.entity.Ticket;

import java.util.List;

/**
 * Controller REST pour les tickets
 */
@Path("/tickets")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class TicketController {

    @GET
    public Response getAllTickets() {
        EntityManager em = EntityManagerHelper.getEntityManager();
        try {
            TicketDAO dao = new TicketDAO(em);
            List<Ticket> tickets = dao.findAll();
            return Response.ok(tickets).build();
        } finally {
            em.close();
        }
    }

    @GET
    @Path("/{id}")
    public Response getTicketById(@PathParam("id") Long id) {
        EntityManager em = EntityManagerHelper.getEntityManager();
        try {
            TicketDAO dao = new TicketDAO(em);
            Ticket ticket = dao.findById(id);
            if (ticket == null) {
                return Response.status(Response.Status.NOT_FOUND).build();
            }
            return Response.ok(ticket).build();
        } finally {
            em.close();
        }
    }

    @GET
    @Path("/utilisateur/{userId}")
    public Response getTicketsByUtilisateur(@PathParam("userId") Long userId) {
        EntityManager em = EntityManagerHelper.getEntityManager();
        try {
            TicketDAO dao = new TicketDAO(em);
            List<Ticket> tickets = dao.findTicketsByUtilisateur(userId);
            return Response.ok(tickets).build();
        } finally {
            em.close();
        }
    }

    @DELETE
    @Path("/{id}/annuler")
    public Response annulerTicket(@PathParam("id") Long id) {
        EntityManager em = EntityManagerHelper.getEntityManager();
        jakarta.persistence.EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            TicketDAO dao = new TicketDAO(em);
            boolean success = dao.annulerTicket(id);
            tx.commit();

            if (success) {
                return Response.ok("{\"message\": \"Ticket annulé\"}").build();
            } else {
                return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"error\": \"Impossible d'annuler ce ticket\"}")
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
