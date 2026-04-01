package jpa;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jpa.entity.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;


public class JpaTest {

	private EntityManager manager;

	public JpaTest(EntityManager manager) {
		this.manager = manager;
	}

	public static void main(String[] args) {
		EntityManager manager = EntityManagerHelper.getEntityManager();
		JpaTest test = new JpaTest(manager);

		EntityTransaction tx = manager.getTransaction();
		tx.begin();
		try {
			test.createTestData();
			tx.commit();
		} catch (Exception e) {
			tx.rollback();
			e.printStackTrace();
		}

		manager.close();
		EntityManagerHelper.closeEntityManagerFactory();
		System.out.println("Done!");
	}

	/**
	 * Crée des données de test
	 */
	private void createTestData() {

		System.out.println("Organisateurs");
		Organisateur org1 = new Organisateur(
				"contact@prodparis.fr",
				"password123",
				"Production Paris Events",
				"12345678901234",
				"0145678901"
		);
		org1.setAdresse("15 Avenue des Champs-Élysées, Paris");
		org1.setValide(true); // Validé par l'admin
		manager.persist(org1);

		Organisateur org2 = new Organisateur(
				"info@lyonlive.fr",
				"password456",
				"Lyon Live Productions",
				"98765432109876",
				"0478901234"
		);
		org2.setAdresse("50 Rue de la République, Lyon");
		org2.setValide(true);
		manager.persist(org2);

		System.out.println("Utilisateurs");
		Utilisateur user1 = new Utilisateur(
				"marie.dupont@email.fr",
				"pass123",
				"Dupont",
				"Marie",
				"0601020304"
		);
		manager.persist(user1);

		Utilisateur user2 = new Utilisateur(
				"pierre.martin@email.fr",
				"pass456",
				"Martin",
				"Pierre",
				"0612345678"
		);
		manager.persist(user2);

		Utilisateur user3 = new Utilisateur(
				"sophie.bernard@email.fr",
				"pass789",
				"Bernard",
				"Sophie",
				"0623456789"
		);
		manager.persist(user3);

		System.out.println("Concerts");
		Concert concert1 = new Concert(
				"Festival Rock Paris 2026",
				"Coldplay",
				LocalDate.of(2026, 7, 15),
				LocalTime.of(20, 30),
				"Stade de France",
				"Paris",
				Genre.ROCK,
				new BigDecimal("89.50"),
				100
		);
		concert1.setDescription("Concert exceptionnel de Coldplay au Stade de France");
		concert1.setOrganisateur(org1);
		manager.persist(concert1);

		Concert concert2 = new Concert(
				"Jazz Night Lyon",
				"Herbie Hancock",
				LocalDate.of(2026, 8, 20),
				LocalTime.of(21, 0),
				"Auditorium Maurice Ravel",
				"Lyon",
				Genre.JAZZ,
				new BigDecimal("65.00"),
				50
		);
		concert2.setDescription("Soirée jazz exceptionnelle avec la légende Herbie Hancock");
		concert2.setOrganisateur(org2);
		manager.persist(concert2);

		Concert concert3 = new Concert(
				"Electro Festival Marseille",
				"David Guetta",
				LocalDate.of(2026, 9, 5),
				LocalTime.of(22, 0),
				"Vélodrome",
				"Marseille",
				Genre.ELECTRO,
				new BigDecimal("75.00"),
				80
		);
		concert3.setDescription("Festival électro avec David Guetta");
		concert3.setOrganisateur(org1);
		manager.persist(concert3);

		System.out.println("Tickets (standard, premium, last minute)");

		TicketStandard ticketStd1 = new TicketStandard(
				concert1, user1,
				new BigDecimal("89.50"),
				"A12", "Gradin Nord"
		);
		manager.persist(ticketStd1);

		TicketStandard ticketStd2 = new TicketStandard(
				concert2, user2,
				new BigDecimal("65.00"),
				"B05", "Orchestre"
		);
		manager.persist(ticketStd2);

		TicketPremium ticketPrem1 = new TicketPremium(
				concert1, user2,
				new BigDecimal("250.00"),
				"VIP01", "Carré VIP",
				true, true, true  // accès coulisses, meet&greet, parking VIP
		);
		ticketPrem1.setAvantagesSupplementaires("Boissons offertes, merchandise exclusif");
		manager.persist(ticketPrem1);

		TicketPremium ticketPrem2 = new TicketPremium(
				concert3, user3,
				new BigDecimal("180.00"),
				"VIP05", "Zone Premium",
				false, true, true
		);
		manager.persist(ticketPrem2);

		TicketLastMinute ticketLM1 = new TicketLastMinute(
				concert3, user1,
				new BigDecimal("52.50"),
				30, "Debout - Zone Générale"
		);
		manager.persist(ticketLM1);

		TicketLastMinute ticketLM2 = new TicketLastMinute(
				concert1, user3,
				new BigDecimal("62.65"),
				30, "Places restantes sans réservation"
		);
		manager.persist(ticketLM2);

		// Mettre à jour les tickets disponibles
		concert1.setTicketsDisponibles(concert1.getTicketsDisponibles() - 3);
		concert2.setTicketsDisponibles(concert2.getTicketsDisponibles() - 1);
		concert3.setTicketsDisponibles(concert3.getTicketsDisponibles() - 2);

	}

}
