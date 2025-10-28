package com.medilabo.abernathyclinic.notes.dto;

public record CreateNoteDto(String patientUuid, String doctorId, String createdAt, String updatedAt, String content) {}
