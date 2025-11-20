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
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.medilabo.abernathyclinic.notes.dto.CreateNoteDto;
import com.medilabo.abernathyclinic.notes.dto.NoteDto;
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
    
    private String patient1Uuid = "6915caf18a6b14e96442877e";

    @BeforeEach
    void setUp() throws IOException {
        noteRepository.deleteAll().block();

    List<Note> notes = mapper.readValue(
        new ClassPathResource("db/notes.json").getInputStream(),
        new TypeReference<>() {}
    );

        noteRepository.saveAll(notes).blockLast(); // block pour que les données soient bien en place avant le test
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
    	// java.lang.IllegalArgumentException: 'producer' type is unknown to ReactiveAdapterRegistry
    	// https://34codefactory.medium.com/spring-5-webclient-and-webtestclient-tutorial-code-factory-84e32978149a
    	
    	// Act & assert
    	webClient.post()
    		.uri("/api/note/patient/{patientUuid}", patient1Uuid)
    		.body(Mono.just(newNote), CreateNoteDto.class) // attend que le client WebTestClient lui fournisse un flux réactif
    		.exchange()
    		.expectStatus().isCreated()
    		.expectBody() //Consume and decode the response body to byte[] and then apply assertions on the raw content (for example, isEmpty, JSONPath
    			.jsonPath("$.doctorId").isEqualTo(newNote.doctorId())
    			.jsonPath("$.id").exists()
    			.jsonPath("$.content").isEqualTo(noteContent)
    			.jsonPath("$.createdAt").exists()
    			.jsonPath("$.patientUuid").isEqualTo(patient1Uuid);
	}
 }
