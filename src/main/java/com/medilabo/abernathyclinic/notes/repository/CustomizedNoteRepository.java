package com.medilabo.abernathyclinic.notes.repository;

import java.time.LocalDateTime;

import com.medilabo.abernathyclinic.notes.entity.Note;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CustomizedNoteRepository {
	Mono<Note> findByPatientUuidAndCreatedAt(String patientUuid, LocalDateTime createdAt);

	Flux<Note> findByPatientUuid(String patientUuid);	
}
