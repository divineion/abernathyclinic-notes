# Microservice Notes

## Architecture

Ce microservice fait partie d'une application de gestion de données médicales et démographiques permettant d'obtenir des rapports de risques en fonction des profils des patients et de leurs constatations médicales. 
  
Il s'intègre à l'application avec d'autres microservices :

- [API Gateway](https://github.com/divineion/abernathyclinic-gateway) pour l'authentification et le routage.    
 - [Microservice Patient](https://github.com/divineion/abernathyclinic-patient) pour la gestion des données démographiques des patients. 
 - [Microservice Report](https://github.com/divineion/abernathyclinic-report) pour l'évaluation du niveau de risque de diabète en croisant les données démographiques et les notes médicales.   
 - [Infrastructure](https://github.com/divineion/abernathyclinic-infra) pour l'orchestration Docker.   
 - [Interface utilisateur](https://github.com/divineion/abernathyclinic-client) pour l'interface web de gestion des fiches patients et la consultation des rapports de risque.   
 
 ![Schéma d'architecture](docs/app-architecture.png) 

## 1. Rôle

Ce microservice réactif permet la saisie, l'édition et la consultation des notes d'observations rédigées par les praticiens lors des visites médicales. 
Il fournit le contenu textuel analysé pour l'évaluation des déclencheurs de risque.

## 2. Choix techniques
 - Langage : **Java 24**
 - Framework : **Spring Boot** (Spring WebFlux)   
 - Persistance : **MongoDB** (Spring Data Reactive MongoDB)   
 - Tests d'intégration : **Testcontainers** 
 - Conteneurisation : **Docker**    

## 3. Configuration
Les fichiers `application.properties`, `application-dev.properties` et `application-test.properties` comportent les informations de connexion à la base de données.   

`spring.data.mongodb.uri` | URI de connexion MongoDB | `mongodb://localhost:27017`    
`spring.data.mongodb.database` | Nom de la base de données (profil dev) | `dev_abernathyclinic_notes` 

## 4. Principaux endpoints
Les interactions entre les microservices s'appuient sur l'architecture réactive (Reactor Mono et Flux).  

GET /api/note/{id} : recherche une note d'observation par son ID MongoDB.   
GET /api/notes/patient/{patientUuid} : récupère l'historique des notes d'un patient trié par date décroissante.   
GET /api/notes/doctor/{doctorId} : récupère les notes rédigées par un médecin.   
GET /api/notes/{uuid}/report-info : expose uniquement le champ content des notes pour le service Report.   

POST /api/note/patient/{patientUuid} : ajoute une nouvelle note médicale pour un patient.   

PATCH /api/note/{noteId}/update : met à jour le contenu d'une note existante (nécessite les en-têtes d'authentification X-Auth-User-Roles et X-Auth-User-Id).   

## 5. Démarrage rapide

### Prérequis
 - Java 24
 - Maven 3.x
 - Instance MongoDB démarrée sur le port 27017
 - API Gateway démarrée pour le routage, la sécurité et l'authentification (se référer au guide d'installation de Vault dans son README)

### Lancer le microservice
 
```
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```
Le profil dev écoute sur le port 8083 et charge automatiquement un jeu de données de test réactif (notes associées à des cas "NONE", "BORDERLINE", "IN DANGER", "EARLY ONSET").   



# Notes Microservice

## Architecture
This microservice is part of a medical and demographic data management application designed to assess health risk reports based on patient profiles and medical observations.   

It integrates with the following microservices:
 - [API Gateway](https://github.com/divineion/abernathyclinic-gateway) for routing, security, and authentication (integrates HashiCorp Vault).
 - [Microservice Patient](https://github.com/divineion/abernathyclinic-patient) for managing patient demographic data.
 - [Microservice Report](https://github.com/divineion/abernathyclinic-report) for assessing diabetes risk levels by combining demographic data and medical notes.
 - [Infrastructure](https://github.com/divineion/abernathyclinic-infra) for Docker container orchestration.
 - [User Interface](https://github.com/divineion/abernathyclinic-client) web interface for managing patient records and viewing risk reports.

 ![Schéma d'architecture](docs/app-architecture.png) 

## 1. Role
This microservice manages the entry, update, and retrieval of medical observation notes created by doctors during patient visits.  
It supplies textual data used to detect health risk triggers.

## 2. Technical Stack
 - Language: Java 24
 - Framework: Spring Boot (Spring WebFlux)
 - Persistence: MongoDB (Spring Data Reactive MongoDB)
 - Integration Testing: Testcontainers
 - Containerization: Docker

## 3. Configuration
The microservice connects directly to the local or containerized MongoDB instance using properties defined in application.properties (or application-dev.properties).

`spring.data.mongodb.uri` | MongoDB connection URI | `mongodb://localhost:27017`    
`spring.data.mongodb.database` | Database name (dev profile) | `dev_abernathyclinic_notes`    

## 4. Main Endpoints
Inter-service interactions are built using a non-blocking reactive architecture (Reactor Mono and Flux).   

GET `/api/note/{id}`: retrieves a medical note by its MongoDB ID.   
GET `/api/notes/patient/{patientUuid}`: retrieves all notes for a patient sorted by descending creation date.   
GET `/api/notes/doctor/{doctorId}`: retrieves all notes recorded by a specific doctor.   
GET `/api/notes/{uuid}/report-info`: exposes note content required by the Report service.   

POST `/api/note/patient/{patientUuid}`: adds a new medical note for a patient.   

PATCH `/api/note/{noteId}/update`: updates an existing note's content (requires X-Auth-User-Roles and X-Auth-User-Id headers).

## 5. Quickstart
### Prerequisites
 - Java 24
 - Maven 3.x
 - Running MongoDB instance on port 27017   
 - API Gateway running for routing and authentication (refer to its README for the local Vault setup guide)   

### Run the Microservice
```
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```

The dev profile listens on port 8083 and automatically initializes reactive test datasets (notes corresponding to "NONE", "BORDERLINE", "IN DANGER", and "EARLY ONSET" risk levels).