package com.medilabo.abernathyclinic.notes.service;

import java.time.LocalDateTime;

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
	
	public Flux<Note> findAllNotes() {
		return noteRepository.findAll();
	}
	
	public Mono<Note> findById(String id) {
		return noteRepository.findById(id)
				.doOnError(_ -> new NoteNotFoundException("note not found"));
	}

	public Flux<Note> findByPatientUuid(String patientUuid) {
		return customizedRepository.findByPatientUuid(patientUuid);
	}

	public Mono<Note> createNote(CreateNoteDto noteDto) {
		CreateNoteDto noteToSave = new CreateNoteDto(noteDto.patientUuid(), noteDto.doctorId(), noteDto.createdAt(), noteDto.updatedAt(), noteDto.content());
		Note note = new Note(noteToSave.patientUuid(), noteToSave.doctorId(), LocalDateTime.now(), null, noteToSave.content());
		return noteRepository.save(note);
	}
}
