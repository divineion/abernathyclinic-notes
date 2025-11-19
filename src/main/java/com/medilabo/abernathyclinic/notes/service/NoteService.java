package com.medilabo.abernathyclinic.notes.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.stereotype.Service;

import com.medilabo.abernathyclinic.notes.dto.CreateNoteDto;
import com.medilabo.abernathyclinic.notes.dto.MinimalNoteDto;
import com.medilabo.abernathyclinic.notes.dto.NoteDto;
import com.medilabo.abernathyclinic.notes.dto.NotesReportInfoDto;
import com.medilabo.abernathyclinic.notes.dto.UpdateNoteDto;
import com.medilabo.abernathyclinic.notes.entity.Note;
import com.medilabo.abernathyclinic.notes.exceptions.ForbiddenAccessException;
import com.medilabo.abernathyclinic.notes.exceptions.NoteNotFoundException;
import com.medilabo.abernathyclinic.notes.repository.CustomizedNoteRepository;
import com.medilabo.abernathyclinic.notes.repository.NoteRepository;
import com.mongodb.client.result.UpdateResult;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class NoteService {
	private final NoteRepository noteRepository;
	private final CustomizedNoteRepository customizedRepository;

	public NoteService(NoteRepository noteRepository, CustomizedNoteRepository customizedRepository) {
		this.noteRepository = noteRepository;
		this.customizedRepository = customizedRepository;
	}
	
	public Mono<NoteDto> findById(String id) {
		return noteRepository.findById(id)
				.switchIfEmpty(Mono.error(new NoteNotFoundException("Note not found")))
				.map(note -> new NoteDto(
						note.getId(),
						note.getPatientUuid(), 
						note.getDoctorId(), 
						note.getCreatedAt().format(DateTimeFormatter.ISO_DATE_TIME),
						note.getUpdatedAt() != null ? note.getUpdatedAt().format(DateTimeFormatter.ISO_DATE_TIME) : null, 
						note.getContent()));
	}

	public Flux<MinimalNoteDto> findByPatientUuid(String patientUuid) {
		return customizedRepository.findByPatientUuid(patientUuid)
			.map(note -> new MinimalNoteDto(
					note.getId(), 
					note.getPatientUuid(),
					note.getDoctorId(),
					note.getCreatedAt().toString(),
					note.getUpdatedAt() == null ? null : note.getUpdatedAt().toString()
				)
			);
	}

	public Flux<MinimalNoteDto> findByDoctorId(String doctorId) {
		return customizedRepository.findByDoctorId(doctorId)
				.map((note) -> new MinimalNoteDto( 
						note.getId(), 
						note.getPatientUuid(),
						note.getDoctorId(),
						note.getCreatedAt().toString(),
						note.getUpdatedAt() == null ? null : note.getUpdatedAt().toString()));
	}

	public Mono<NoteDto> createNote(String patientUuid, CreateNoteDto noteDto) {
		Note note = new Note(patientUuid, noteDto.doctorId(), LocalDateTime.now(), null, noteDto.content());
		return noteRepository.save(note)
			.map(createdNote -> new NoteDto(note.getId(),
					createdNote.getPatientUuid(), createdNote.getDoctorId(), 
					createdNote.getCreatedAt().format(DateTimeFormatter.ISO_DATE_TIME), 
					null, createdNote.getContent()));
	}
	
	/**
	 * Verifies that the user is the note's author. 
	 * @param noteId
	 * @param authenticatedUserId
	 * @return
	 */
	public Mono<Void> validateUserAccess(String noteId, String authenticatedUserId) {
		return noteRepository.findById(noteId)
			.flatMap(note -> {
				if (!note.getDoctorId().equals(authenticatedUserId)) {
					return Mono.error(new ForbiddenAccessException("Unauthorized"));
				}
				return Mono.empty();
			});
	}

	public Mono<UpdateResult> updateNote(String noteId, UpdateNoteDto noteDto, String authenticatedUserId) {
		
		return validateUserAccess(noteId, authenticatedUserId) // chaîner les appels réactifs... sinon pas d'exécution
			.then(customizedRepository.updateNote(noteId, noteDto))
			.flatMap(updateResult -> {
				if (updateResult.getModifiedCount() == 0) {
					return Mono.error(new NoteNotFoundException("No note found with id " + noteId + " for update"));
				}
				
				return Mono.just(updateResult);
			});
	}

	public Flux<NotesReportInfoDto> getNotesInfoForReport(String patientUuid) {
		return customizedRepository.findReportPatientInfo(patientUuid);
	}
}
