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
Contient Requêtes JPQL, @NamedQuery, Méthodes métier 

#### TicketDAO
Contient Criteria Query, Méthodes métier, Requêtes par type

#### UtilisateurDAO
Contient Recherches, Méthodes métier 

#### OrganisateurDAO
Contient, Recherches, Méthodes métier 


## API REST

### Controllers REST
Endpoints CRUD
Endpoints métier : Recherche avec filtres, Vérifier les places disponibles, Réserver des tickets
Documentation OpenAPI

### DTOs (Data Transfer Objects)
ConcertDTO : Exposé par l'API au lieu de l'entité brute
- Évite d'exposer les relations circulaires (problème de sérialisation JSON)
- Ne contient que les données publiques


### Démarrer l'API REST
1. Démarrer HSQLDB
2. Lancer RestServer.java
3. Accéder à Swagger UI :                                                                                                                                                        
   http://localhost:8080/api/ 


### Arreter un processus en cours sur le port
Dans le terminal
PS \tpjpa>  netstat -ano | findstr :8080
TCP    0.0.0.0:8080           0.0.0.0:0              LISTENING       21928
TCP    [::]:8080              [::]:0                 LISTENING       21928
PS \tpjpa>  taskkill /PID 21928 /F
Opération réussie: le processus avec PID 21928 a été terminé.