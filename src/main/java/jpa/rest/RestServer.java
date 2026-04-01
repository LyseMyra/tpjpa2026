package jpa.rest;

import io.swagger.v3.jaxrs2.integration.resources.OpenApiResource;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import io.undertow.Undertow;
import org.jboss.resteasy.plugins.server.undertow.UndertowJaxrsServer;
import org.jboss.resteasy.core.ResteasyDeploymentImpl;
import org.jboss.resteasy.spi.ResteasyDeployment;

/**
 * Serveur REST principal
 * Lance l'API REST sur le port 8080
 */
@OpenAPIDefinition(
    info = @Info(
        title = "API Gestion de Tickets de Concert",
        version = "1.0",
        description = "API REST pour la gestion de vente de tickets de concert en ligne. "
    ),
    servers = {
        @Server(url = "http://localhost:8080", description = "Serveur de développement")
    }
)
public class RestServer {

    private static final int PORT = 8080;

    public static void main(String[] args) {
        System.out.println("Serveur REST  : http://localhost:" + PORT);
        System.out.println("Swagger UI    : http://localhost:" + PORT + "/api/");
        System.out.println("OpenAPI JSON  : http://localhost:" + PORT + "/openapi.json");
        System.out.println();

        // Configuration RESTEasy
        ResteasyDeployment deployment = new ResteasyDeploymentImpl();

        // Enregistrer les controllers
        deployment.getActualResourceClasses().add(ConcertController.class);
        deployment.getActualResourceClasses().add(TicketController.class);
        deployment.getActualResourceClasses().add(UtilisateurController.class);
        deployment.getActualResourceClasses().add(OrganisateurController.class);

        // SWAGGER endpoints
        deployment.getActualResourceClasses().add(OpenApiResource.class);
        deployment.getActualResourceClasses().add(SwaggerResource.class);

        // Jackson configuration (support Java 8 date/time)
        deployment.getActualProviderClasses().add(JacksonConfig.class);

        // Démarrer le serveur Undertow
        UndertowJaxrsServer server = new UndertowJaxrsServer();

        server.deploy(deployment);

        server.start(
            Undertow.builder()
                .addHttpListener(PORT, "0.0.0.0")
        );

        // Shutdown hook pour libérer le port proprement à l'arrêt
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("\nArrêt du serveur...");
            server.stop();
            System.out.println("Serveur arrêté.");
        }));

        System.out.println("Serveur démarré ! (Ctrl+C pour arrêter)");

    }
}
