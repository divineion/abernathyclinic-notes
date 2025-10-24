package com.medilabo.abernathyclinic.notes.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.ReactiveMongoDatabaseFactory;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.SimpleReactiveMongoDatabaseFactory;

import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoClients;

@Configuration
public class AppConfig {
    @Value("${spring.data.mongodb.database}")
    private String databaseName;
    
    @Value("${spring.data.mongodb.uri}")
    private String mongoUri;
	
	//1. register an instance of a MongoClient
	// MongoClient --> entry point du pilote Reactive Streams de MongoDB. Connexion physique au serveur
   @Bean 
   MongoClient mongoClient() {
       return MongoClients.create(mongoUri);
   }

   // Permet à Spring Data de savoir sur quelle BDD travailler + support des transactions réactives
   // nécessaire pour créer ReactiveMongoTemplate
   @Bean
   ReactiveMongoDatabaseFactory mongoDatabaseFactory() {
	   return new SimpleReactiveMongoDatabaseFactory(mongoClient(), databaseName);
   }
   
   // API haut niveau Spring Data pour les opérations réactives
   @Bean
   ReactiveMongoTemplate reactiveMongoTemplate(ReactiveMongoDatabaseFactory mongoDatabaseFactory) {
	   return new ReactiveMongoTemplate(mongoDatabaseFactory);
   }
}
