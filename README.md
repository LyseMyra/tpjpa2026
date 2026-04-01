# Projet TP JPA

## Démarrage du projet

### Étape 1 : Démarrer le serveur HSQLDB
Ouvrir un terminal dans le répertoire du projet et exécutez :
```bash
.\run-hsqldb-server.bat
```

### Étape 2 : Créer les entités
Dans IntelliJ IDEA :
1. Ouvrir le fichier `src/main/java/jpa/JpaTest.java`
2. Faites un clic droit sur la classe `JpaTest`
3. Sélectionnez **"Run 'JpaTest.main()'"**

### Étape 3 : Visualiser la base de données
Ouvrir un terminal et exécutez :
```bash
.\show-hsqldb.bat
```

## Modèle de données

### Entités principales
Concert (ManyToOne avec Organisateur, OneToMany avec Ticket) 
Organisateur (OneToMany avec Concert)
Utilisateur (OneToMany avec Ticket) 
Ticket (héritage - stratégie SINGLE_TABLE) (ManyToOne avec Concert et Utilisateur)
   - TicketStandard : Place assise numérotée
   - TicketPremium : Avec avantages VIP (accès coulisses, meet & greet, parking)
   - TicketLastMinute : Sans place réservée, tarif réduit


### Relations bidirectionnelles (mappedBy)
Organisateur - Concert : Un organisateur peut avoir plusieurs concerts
Concert - Ticket : Un concert peut avoir plusieurs tickets
Utilisateur - Ticket : Un utilisateur peut acheter plusieurs tickets


### DAOs implémentées
DAOs implémentent l'interface GenericDAO qui fournit les méthodes CRUD de base

#### ConcertDAO
Contient :
- Requêtes JPQL : `findByGenreJPQL()`, `findByPrixRange()`, `findByArtiste()`
- @NamedQuery : `findByVille()`, `findByGenre()`, `findActifs()`, `findConcertsDisponibles()`
- Méthodes métier : `reserverTickets()`, `libererTickets()`, `findByDateRange()`, `isConcertComplet()`

#### TicketDAO
Contient :
- Criteria Query : `findByCriteria()`, `findByPrixRange()`
- Méthodes métier : `annulerTicket()`, `utiliserTicket()`, `calculerRevenuTotal()`, `calculerRevenuConcert()`
- Requêtes par type : `findTicketsStandard()`, `findTicketsPremium()`, `findTicketsLastMinute()`

#### UtilisateurDAO
Contient :
- Recherches : `findByEmail()`, `findByNom()`, `findActifs()`
- Méthodes métier : `authenticate()`, `countTicketsAchetes()`

#### OrganisateurDAO
Contient :
- Recherches : `findByEmail()`, `findBySiret()`, `findValides()`, `findEnAttenteValidation()`
- Méthodes métier : `valider()`, `authenticate()`, `countConcertsOrganises()`

## API REST

### Controllers REST
Endpoints CRUD
Endpoints métier : Recherche avec filtres, Vérifier les places disponibles, Réserver des tickets
Documentation OpenAPI avec annotations `@Operation`, `@Parameter`, `@ApiResponse`

### DTOs (Data Transfer Objects)
ConcertDTO : Exposé par l'API au lieu de l'entité brute
- Évite d'exposer les relations circulaires (problème de sérialisation JSON)
- Ne contient que les données publiques
- Mapping entité ↔ DTO via `ConcertMapper`

### Démarrer l'API REST
1. Démarrer HSQLDB
2. Lancer RestServer.java
3. Accéder à Swagger UI :                                                                                                                                                        
   http://localhost:8080/api/ 


### Arreter un processus en cours sur le port

PS C:\Users\yahgn\WebstormProjects\tpjpa2026>  netstat -ano | findstr :8080
TCP    0.0.0.0:8080           0.0.0.0:0              LISTENING       21928
TCP    [::]:8080              [::]:0                 LISTENING       21928
PS C:\Users\yahgn\WebstormProjects\tpjpa2026>  taskkill /PID 21928 /F
Opération réussie: le processus avec PID 21928 a été terminé.