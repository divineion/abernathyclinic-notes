package com.medilabo.abernathyclinic.notes.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

import com.medilabo.abernathyclinic.notes.entity.Note;

@Repository
public interface NoteRepository extends ReactiveMongoRepository<Note, String> {
}
