package com.medilabo.abernathyclinic.notes.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.medilabo.abernathyclinic.notes.dto.CreateNoteDto;
import com.medilabo.abernathyclinic.notes.dto.MinimalNoteDto;
import com.medilabo.abernathyclinic.notes.dto.NoteDto;
import com.medilabo.abernathyclinic.notes.dto.NotesReportInfoDto;
import com.medilabo.abernathyclinic.notes.dto.UpdateNoteDto;
import com.medilabo.abernathyclinic.notes.dto.UpdateResultDto;
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
	public Flux<MinimalNoteDto> getNotesByPatientUuid(@PathVariable String patientUuid) {
		return noteService.findByPatientUuid(patientUuid);
	}
	
	@GetMapping("/api/notes/doctor/{doctorId}")
	public Flux<MinimalNoteDto> getNotesByDoctorId(@PathVariable String doctorId) {
		return noteService.findByDoctorId(doctorId);
	}
	
	@PostMapping("/api/note/patient/{patientUuid}")
	public Mono<ResponseEntity<NoteDto>> createNote(@PathVariable String patientUuid, @RequestBody CreateNoteDto noteDto) {
		return noteService.createNote(patientUuid, noteDto).map(createdNote -> ResponseEntity.status(201).body(createdNote));
	}
	
	@PatchMapping("/api/note/{noteId}/update")
	public Mono<UpdateResultDto> updateNote(
			@PathVariable String noteId, 
			@RequestBody UpdateNoteDto noteDto,
			@RequestHeader("X-Auth-User-Roles") String role,
			@RequestHeader("X-Auth-User-Id") String authenticatedUserId) {	
		
	return noteService.updateNote(noteId, noteDto, authenticatedUserId)
					.map(result -> new UpdateResultDto(result.getMatchedCount() > 0, result.getModifiedCount()));
	}
	
	@GetMapping("/api/notes/{uuid}/report-info")
	public Flux<NotesReportInfoDto> getNotesReportInfo(@PathVariable("uuid") String patientUuid) {
		return noteService.getNotesInfoForReport(patientUuid);
	}
	
	
}