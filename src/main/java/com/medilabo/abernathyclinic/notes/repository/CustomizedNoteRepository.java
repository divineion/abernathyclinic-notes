package com.medilabo.abernathyclinic.notes.repository;

import java.time.LocalDateTime;

import com.medilabo.abernathyclinic.notes.entity.Note;

import reactor.core.publisher.Mono;

public interface CustomizedNoteRepository {
	Mono<Note> findByPatientIdAndCreatedAt(String patientId, LocalDateTime createdAt);	
}
