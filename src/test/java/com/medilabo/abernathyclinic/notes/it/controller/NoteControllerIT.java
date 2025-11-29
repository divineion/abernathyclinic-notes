package com.medilabo.abernathyclinic.notes.it.controller;

import static org.springframework.http.MediaType.APPLICATION_JSON;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.medilabo.abernathyclinic.notes.dto.CreateNoteDto;
import com.medilabo.abernathyclinic.notes.dto.MinimalNoteDto;
import com.medilabo.abernathyclinic.notes.dto.NoteDto;
import com.medilabo.abernathyclinic.notes.dto.NotesReportInfoDto;
import com.medilabo.abernathyclinic.notes.dto.UpdateNoteDto;
import com.medilabo.abernathyclinic.notes.dto.UpdateResultDto;
import com.medilabo.abernathyclinic.notes.entity.Note;
import com.medilabo.abernathyclinic.notes.repository.NoteRepository;

import reactor.core.publisher.Mono;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureWebTestClient

public class NoteControllerIT {
	@Autowired
    private WebTestClient webClient;

    @Autowired
    private NoteRepository noteRepository;
    
    @Autowired
    ObjectMapper mapper;

    
    private String noteId1 = "6915caf18a6b14e96442877e";
    
    private String patient1Uuid = "82bcd28f-2db2-4e67-aed8-207774fdf52b";

    @BeforeEach
    void setUp() throws IOException {
        noteRepository.deleteAll().block();

    List<Note> notes = mapper.readValue(
        new ClassPathResource("db/notes.json").getInputStream(),
        new TypeReference<>() {}
    );

        noteRepository.saveAll(notes).blockLast();
    }

    @Test
    void getNoteById_shouldReturnNoteDto() {   
    	
    	NoteDto expectedNote = new NoteDto(
    			noteId1, "82bcd28f-2db2-4e67-aed8-207774fdf52b", 
    			"4", LocalDateTime.of(2024,11, 20, 1, 40).format(DateTimeFormatter.ISO_DATE_TIME),
    			null, "Test content 1");
    	
        // Act & assert
        webClient.get()
            .uri("/api/note/{id}", noteId1)
            .accept(APPLICATION_JSON)
            .exchange()
            .expectStatus().isOk()
            .expectBody(NoteDto.class).isEqualTo(expectedNote);
    }
    
    @Test
    void testCreateNote_shouldReturnNoteDto() {
    	
    	String noteContent = "Le médecin crée un nouvelle note au sujetd 'un patient.";
    	CreateNoteDto newNote = new CreateNoteDto("4", noteContent);
    	
    	// Act & assert
    	webClient.post()
    		.uri("/api/note/patient/{patientUuid}", patient1Uuid)
    		.body(Mono.just(newNote), CreateNoteDto.class)
    		.exchange()
    		.expectStatus().isCreated()
    		.expectBody() 
    			.jsonPath("$.doctorId").isEqualTo(newNote.doctorId())
    			.jsonPath("$.id").exists()
    			.jsonPath("$.content").isEqualTo(noteContent)
    			.jsonPath("$.createdAt").exists()
    			.jsonPath("$.patientUuid").isEqualTo(patient1Uuid);
	}
    
    @Test
    void getNotesByPatientUuid_shouldReturnMinimalNotes() {
        webClient.get()
            .uri("/api/notes/patient/{patientUuid}", patient1Uuid)
            .accept(APPLICATION_JSON)
            .exchange()
            .expectStatus().isOk()
            .expectBodyList(MinimalNoteDto.class)
            .consumeWith(response -> {
                List<MinimalNoteDto> notes = response.getResponseBody();
                assert notes != null;
                assert notes.stream().allMatch(n -> n.patientUuid().equals(patient1Uuid));
            });
    }

    @Test
    void getNotesByDoctorId_shouldReturnMinimalNotes() {
        String doctorId = "4";
        webClient.get()
            .uri("/api/notes/doctor/{doctorId}", doctorId)
            .accept(APPLICATION_JSON)
            .exchange()
            .expectStatus().isOk()
            .expectBodyList(new ParameterizedTypeReference<MinimalNoteDto>() {})
            .consumeWith(response -> {
                List<MinimalNoteDto> notes = response.getResponseBody();
                assert notes != null;
                assert notes.stream().allMatch(n -> n.doctorId().equals(doctorId));
            });
    }

    @Test
    void updateNote_shouldReturnUpdateResultDto() {
        UpdateNoteDto updateDto = new UpdateNoteDto("Updated content");
        
        webClient.patch()
            .uri("/api/note/{noteId}/update", noteId1)
            .header("X-Auth-User-Roles", "ROLE_DOCTOR")
            .header("X-Auth-User-Id", "4")
            .body(Mono.just(updateDto), UpdateResultDto.class)
            .exchange()
            .expectStatus().isOk()
            .expectBody()
            .jsonPath("$.updatedCount").isEqualTo(1)
            .jsonPath("$.success").isEqualTo(true);
    }

    @Test
    void getNotesReportInfo_shouldReturnNotesReportInfo() {
        webClient.get()
            .uri("/api/notes/{uuid}/report-info", patient1Uuid)
            .accept(APPLICATION_JSON)
            .exchange()
            .expectStatus().isOk()
            .expectBodyList(NotesReportInfoDto.class)
            .consumeWith(response -> {
                List<NotesReportInfoDto> reportInfo = response.getResponseBody();
                assert reportInfo != null;
                assert reportInfo.size() > 0;
            });
    }
 }
