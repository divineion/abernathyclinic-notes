package com.medilabo.abernathyclinic.notes.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.medilabo.abernathyclinic.notes.dto.NoteDto;
import com.medilabo.abernathyclinic.notes.entity.Note;
import com.medilabo.abernathyclinic.notes.service.NoteService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
public class NoteController {
	private final NoteService noteService;		
	
	public NoteController(NoteService noteService) {
		this.noteService = noteService;
	}
	
	@GetMapping("/api/note/{id}")
	public Mono<NoteDto> getNoteById(@PathVariable String id) {
		return noteService.findById(id);
	}
	
	@GetMapping("/api/notes/patient/{patientUuid}")
	public Flux<Note> getNotesByPatientUuid(@PathVariable String patientUuid) {
		return noteService.findByPatientUuid(patientUuid);
	}
	
	@PostMapping("/api/note/patient/{patientUuid}")
	public Mono<ResponseEntity<NoteDto>> createNote(@PathVariable String patientUuid, @RequestBody NoteDto noteDto) {
		return noteService.createNote(noteDto).map(createdNote -> ResponseEntity.status(201).body(createdNote));
	}
}