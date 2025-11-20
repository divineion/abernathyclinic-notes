package com.medilabo.abernathyclinic.notes.it.repositories;

import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.utility.TestcontainersConfiguration;

import com.medilabo.abernathyclinic.notes.entity.Note;
import com.medilabo.abernathyclinic.notes.repository.CustomizedNoteRepositoryImpl;
import com.medilabo.abernathyclinic.notes.repository.NoteRepository;

import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

@SpringBootTest
@Import(TestcontainersConfiguration.class)
@ActiveProfiles("test")
public class CustomizedNoteRepositoryIT {
	@Autowired
	private NoteRepository repository;
	
	@Autowired
	private CustomizedNoteRepositoryImpl customizedNoteRepository;
	
	@BeforeEach
	public void clearDb()	 {
		repository.deleteAll().block();
	}
	
	/**
	 * Checks that finding notes by patient UUID using the customized repository
	 * returns all notes for that patient sorted by creation date (newest first).
	 */
	@Test
	public void testFindNotesByUuid_shouldReturnNoteFlux() {
		// Arrange	
        // Note settings 
		String patientUuid = "82bcd28f-2db2-4e67-aed8-207774fdf52b";
		Note note1 = new Note(patientUuid, "4", LocalDateTime.of(2024, 11, 20, 0, 45), null, "Contenu de la note 1");
		Note note2 = new Note(patientUuid, "5", LocalDateTime.of(2025, 11, 20, 0, 45), null, "Contenu de la note  2");
		
		// penser à block() pr déclencher l'exécut°. 
		 repository.save(note1).block();
		 repository.save(note2).block();
		
		Flux<Note> result = customizedNoteRepository.findByPatientUuid(patientUuid);
		
		StepVerifier.create(result)			
			.expectNextMatches(n1 -> n1.getContent().equalsIgnoreCase(note2.getContent()))
			.expectNextMatches(n2 -> n2.getContent().equalsIgnoreCase(note1.getContent()))
			.expectNextCount(0) // 0 puisque les 2 élémnts ont été consommés...
			.verifyComplete(); 
	}
}
