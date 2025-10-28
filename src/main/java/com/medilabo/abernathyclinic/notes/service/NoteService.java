package com.medilabo.abernathyclinic.notes.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.stereotype.Service;

import com.medilabo.abernathyclinic.notes.dto.CreateNoteDto;
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
	
	public Mono<Note> findById(String id) {
		return noteRepository.findById(id)
				.doOnError(_ -> new NoteNotFoundException("note not found"));
	}

	public Flux<Note> findByPatientUuid(String patientUuid) {
		return customizedRepository.findByPatientUuid(patientUuid);
	}

	public Mono<CreateNoteDto> createNote(CreateNoteDto noteDto) {
		Note note = new Note(noteDto.patientUuid(), noteDto.doctorId(), LocalDateTime.now(), null, noteDto.content());
		//récupérer le Mono, le traiter avec map pour lui faire émettre un dto
		return noteRepository.save(note)
			.map(createdNote -> new CreateNoteDto(
					createdNote.getPatientUuid(), createdNote.getDoctorId(), 
					createdNote.getCreatedAt().format(DateTimeFormatter.ISO_DATE_TIME), 
					null, createdNote.getContent()));
	}
}
