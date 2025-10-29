package com.medilabo.abernathyclinic.notes.dto;

public record NoteDto(String id, String patientUuid, String doctorId, String createdAt, String updatedAt, String content) {}
