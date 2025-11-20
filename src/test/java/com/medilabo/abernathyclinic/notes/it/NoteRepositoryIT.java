package com.medilabo.abernathyclinic.notes.it;

import static org.junit.Assert.assertNotNull;

import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.medilabo.abernathyclinic.notes.entity.Note;
import com.medilabo.abernathyclinic.notes.repository.NoteRepository;

import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

public class NoteRepositoryIT {
	@Autowired
	private NoteRepository repository;
	
	@BeforeEach
	public void clearDb()	 {
		repository.deleteAll().block();
	}
	
	/**
	 * This test ensures that the basic save operation works with MongoDB.
	 * Verifies that saving a Note actually assigns it an ID.
	 * The method creates a Note, saves it in the database, and checks that the returned Note has a generated ID.
	 */
	@Test
	public void testSaveNote_shouldReturnNoteMono() {
		// Arrange	
        // Note settings 
		String uuid = "82bcd28f-2db2-4e67-aed8-207774fdf52b";
		String doctorId = "4";
		String content = "Insertion d'une note de test pour un patient.";
		
		Note note = new Note(uuid, doctorId, LocalDateTime.of(2025, 11, 20, 0, 40), null, content);
		
		Mono<Note> savedNote = repository.save(note);
		
		StepVerifier.create(savedNote)
		.assertNext(saved -> {
			assertNotNull(saved.getId());
		})
		.verifyComplete();
	}
}
