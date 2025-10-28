package com.medilabo.abernathyclinic.notes.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.medilabo.abernathyclinic.notes.dto.CreateNoteDto;
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
	public ResponseEntity<Mono<Note>> getNoteById(@PathVariable String id) {
		return new ResponseEntity<Mono<Note>>(noteService.findById(id), HttpStatus.OK);
	}

	@GetMapping("/api/notes/patient/{patientUuid}")
	public ResponseEntity<Flux<Note>> getNotesByPatientUuid(@PathVariable String patientUuid) {
		return new ResponseEntity<Flux<Note>>(noteService.findByPatientUuid(patientUuid), HttpStatus.OK);
	}
	
	@PostMapping("/api/note/patient/{patientUuid}")
	public ResponseEntity<Mono<Note>> createNote(@PathVariable String patientUuid, @RequestBody CreateNoteDto noteDto) {
		return new ResponseEntity<Mono<Note>>(noteService.createNote(noteDto), HttpStatus.CREATED);
	}
}