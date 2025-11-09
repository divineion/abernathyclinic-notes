package com.medilabo.abernathyclinic.notes.repository;

import java.time.LocalDateTime;

import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import com.medilabo.abernathyclinic.notes.dto.UpdateNoteDto;
import com.medilabo.abernathyclinic.notes.entity.Note;
import com.mongodb.client.result.UpdateResult;

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
				.addCriteria(Criteria.where("patientUuid").is(patientUuid))

				// chore(dal): return notes sorted by descending creation date
				.with(Sort.by(Sort.Direction.DESC, "createdAt"));
		
		return template.query(Note.class).matching(query).all();
	}
	

	@Override
	public Flux<Note> findByDoctorId(String doctorId) {
		
		Query query = new Query()
				.addCriteria(Criteria.where("doctorId").is(doctorId));
		
		return template.query(Note.class).matching(query).all();
	}

	@Override
	public Mono<UpdateResult> updateNote(String id, UpdateNoteDto noteDto) {
		Query query = new Query()
				.addCriteria(Criteria.where("id").is(id));
		
		Update update = new Update()
				.set("content", noteDto.content())
				.set("updatedAt", LocalDateTime.now()); // pour l'heure UTC
		
		return template.updateFirst(query, update, Note.class);
	}
}
