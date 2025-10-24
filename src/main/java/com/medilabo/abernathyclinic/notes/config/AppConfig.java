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

  /**
   * Use the standard Mongo driver API to create a {@link MongoClient} instance.
   */
	
	//1. register an instance of a MongoClient
	// MongoClient --> entry point du pilote Reactive Streams de MongoDB . 
	
	// As compared to instantiating a MongoClient instance directly, 
	// the FactoryBean has the added advantage of also providing the container with an ExceptionTranslator implementation
	// that translates MongoDB exceptions to exceptions in Spring’s portable DataAccessException hierarchy 
	// for data access classes annotated with the @Repository annotation
   @Bean 
   MongoClient mongoClient() {
       return MongoClients.create(mongoUri);
   }

   // Pour se connecter à une BDD en particulier, il faut créer un objet pour la représenter
   
   // 2. créer une factory qui sait à quelle BDD se connecter et utilise le MongoClient réactif
   // cela est nécessaire pour que Spring Data puisse gérer des concepts comme les transactions réactives
   // voir si cela est réellement nécessaire puisque je n'ai pas de relations dans la BDD
   // configurer une instance de ReactiveMongoDatabaseFactory en lui passant le client + le nom de la bdd

   @Bean
   ReactiveMongoDatabaseFactory mongoDatabaseFactory() {
	   return new SimpleReactiveMongoDatabaseFactory(mongoClient(), databaseName);
   }
   
   // If you intend to use transactions, make sure to use ReactiveMongoTemplate(ReactiveMongoDatabaseFactory) 
   // or ReactiveMongoTemplate(ReactiveMongoDatabaseFactory, MongoConverter) constructors, 
   // otherwise, this template will not participate in transactions using the default SessionSynchronization.ON_ACTUAL_TRANSACTION setting 
   // as ReactiveMongoTransactionManager uses strictly its configured ReactiveMongoDatabaseFactory for transaction participation.
   // la config attend un ReactiveMongoTemplate nommé reactiveMongoTemplate dans les beans
   @Bean
   ReactiveMongoTemplate reactiveMongoTemplate(ReactiveMongoDatabaseFactory mongoDatabaseFactory) {
	   return new ReactiveMongoTemplate(mongoDatabaseFactory);
   }
}
