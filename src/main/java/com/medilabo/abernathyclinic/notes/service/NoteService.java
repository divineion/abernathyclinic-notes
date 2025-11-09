package com.medilabo.abernathyclinic.notes.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.stereotype.Service;

import com.medilabo.abernathyclinic.notes.dto.MinimalNoteDto;
import com.medilabo.abernathyclinic.notes.dto.NoteDto;
import com.medilabo.abernathyclinic.notes.dto.UpdateNoteDto;
import com.medilabo.abernathyclinic.notes.entity.Note;
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

	// retourner des dto pour la liste de notes 
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

	public Mono<NoteDto> createNote(NoteDto noteDto) {
		Note note = new Note(noteDto.patientUuid(), noteDto.doctorId(), LocalDateTime.now(), null, noteDto.content());
		//récupérer le Mono, le traiter avec map pour lui faire émettre un dto
		return noteRepository.save(note)
			.map(createdNote -> new NoteDto(note.getId(),
					createdNote.getPatientUuid(), createdNote.getDoctorId(), 
					createdNote.getCreatedAt().format(DateTimeFormatter.ISO_DATE_TIME), 
					null, createdNote.getContent()));
		// 4. le Mono<NoteDtoW> est retourné au contrôleur : l'objet est un pipeline prêt
		// à s'exécuter mais pas encore déclenché
	}

	public Mono<UpdateResult> updateNote(String id, UpdateNoteDto noteDto) {
		
		return customizedRepository.updateNote(id, noteDto)
				// vérifier le résultat non bloquant
				.flatMap(updateResult -> {
					// le flatMap est exécuté après que a BDD a renvoyé les résultats updateresult est déjà émis
					// donc le if/else est exécuté sur une valeur existante en mémoire,, et non pas ds l'attente dune vvaleur
					if (updateResult.getModifiedCount() == 0) {
						return Mono.error(new NoteNotFoundException("No note found with id " + id + "for update"));
					}
					
					return Mono.just(updateResult);
				});
	}
}
