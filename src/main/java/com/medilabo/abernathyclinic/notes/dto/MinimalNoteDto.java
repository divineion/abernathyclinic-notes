package com.medilabo.abernathyclinic.notes.dto;

public record MinimalNoteDto(String id, String patientUuid, String doctorId, String createdAt, String updatedAt) {

}
