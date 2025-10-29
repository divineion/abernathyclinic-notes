package com.medilabo.abernathyclinic.notes.repository;

import java.time.LocalDateTime;

import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import com.medilabo.abernathyclinic.notes.entity.Note;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public class CustomizedNoteRepositoryImpl implements CustomizedNoteRepository {
	private final ReactiveMongoTemplate template;

	public CustomizedNoteRepositoryImpl(ReactiveMongoTemplate template) {
		this.template = template;
	}

	@Override
	public Mono<Note> findByPatientUuidAndCreatedAt(String patientUuid, LocalDateTime createdAt) {
		Query query = new Query()
				.addCriteria(Criteria.where("patientUuid").is(patientUuid))
				.addCriteria(Criteria.where("createdAt").is(createdAt));
				
		return template.query(Note.class).matching(query).first();
	}
	
	@Override
	public Flux<Note> findByPatientUuid(String patientUuid) {
		Query query = new Query()
				.addCriteria(Criteria.where("patientUuid").is(patientUuid));
		
		return template.query(Note.class).matching(query).all();
	}
}
