package com.medilabo.abernathyclinic.notes.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.stereotype.Service;

import com.medilabo.abernathyclinic.notes.dto.NoteDto;
import com.medilabo.abernathyclinic.notes.entity.Note;
import com.medilabo.abernathyclinic.notes.exceptions.NoteNotFoundException;
import com.medilabo.abernathyclinic.notes.repository.CustomizedNoteRepository;
import com.medilabo.abernathyclinic.notes.repository.NoteRepository;

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
						note.getPatientUuid(), 
						note.getDoctorId(), 
						note.getCreatedAt().format(DateTimeFormatter.ISO_DATE_TIME),
						note.getUpdatedAt() != null ? note.getUpdatedAt().format(DateTimeFormatter.ISO_DATE_TIME) : null, 
						note.getContent()));
	}

	public Flux<Note> findByPatientUuid(String patientUuid) {
		return customizedRepository.findByPatientUuid(patientUuid);
	}

	public Mono<NoteDto> createNote(NoteDto noteDto) {
		Note note = new Note(noteDto.patientUuid(), noteDto.doctorId(), LocalDateTime.now(), null, noteDto.content());
		//récupérer le Mono, le traiter avec map pour lui faire émettre un dto
		return noteRepository.save(note)
			.map(createdNote -> new NoteDto(
					createdNote.getPatientUuid(), createdNote.getDoctorId(), 
					createdNote.getCreatedAt().format(DateTimeFormatter.ISO_DATE_TIME), 
					null, createdNote.getContent()));
	}
}
