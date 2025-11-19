package com.medilabo.abernathyclinic.notes.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.medilabo.abernathyclinic.notes.dto.CreateNoteDto;
import com.medilabo.abernathyclinic.notes.dto.NoteDto;
import com.medilabo.abernathyclinic.notes.entity.Note;
import com.medilabo.abernathyclinic.notes.repository.CustomizedNoteRepository;
import com.medilabo.abernathyclinic.notes.repository.NoteRepository;

import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

// vérifier si le service retourne les bons DTO
@ExtendWith(MockitoExtension.class)
public class NoteServiceTest {
	// mocker les deps
	@Mock
	private NoteRepository noteRepository;
	
	@Mock
	private CustomizedNoteRepository customizedRepository;
	
	@Mock
	private Clock clock;
	
	@InjectMocks
	private NoteService service;
	
	private LocalDateTime fixedDateTime;
	
	@BeforeEach
	public void setup() {
		// Time settings
		Instant fixedInstant = Instant.parse("2025-11-19T16:00:00Z");
        when(clock.instant()).thenReturn(fixedInstant);
        when(clock.getZone()).thenReturn(ZoneId.of("UTC"));
        
		fixedDateTime = LocalDateTime.now(clock);
	}
	
	@Test
	public void testCreateNote_shouldReturn_NoteDto() {
		// Arrange	
        // Note settings 
		String uuid = "82bcd28f-2db2-4e67-aed8-207774fdf52b";
		String doctorId = "4";
		String content = "Insertion d'une note de test pour un patient.";
		
		Note note = Note.withId("testId", uuid, doctorId, fixedDateTime, null, content);

		when(noteRepository.save(any(Note.class))).thenReturn(Mono.just(note));
		
		CreateNoteDto createNoteDto = new CreateNoteDto(doctorId, content);
		
		NoteDto expectedNote = new NoteDto(
				"testId", 
				uuid, 
				doctorId, 
				fixedDateTime.format(DateTimeFormatter.ISO_DATE_TIME), 
				null, 
				content);
		
		// Act
		Mono<NoteDto> result = service.createNote(uuid, createNoteDto);
		
		// Assert
		StepVerifier.create(result)
			.expectNext(expectedNote)
			.verifyComplete();
	}
	
	@Test
	public void findById_shouldReturnMinimalNoteDto() {
		
		// arrange
		String uuid = "82bcd28f-2db2-4e67-aed8-207774fdf52b";
		String doctorId = "4";
		String content = "Test de lecture d'une note";
		
		Note mockedNote = Note.withId("testId", uuid, doctorId, fixedDateTime, null, content);
		
		when(noteRepository.findById(anyString())).thenReturn(Mono.just(mockedNote));
		
		// ACT
		NoteDto expectedDto = new NoteDto(
	            "testId",
	            uuid,
	            doctorId,
	            fixedDateTime.format(DateTimeFormatter.ISO_DATE_TIME),
	            null, 
	            content);

		 Mono<NoteDto> result = service.findById("testId");

		    // Assert
		    StepVerifier.create(result)
		            .expectNext(expectedDto)
		            .verifyComplete();
		
	}
}
