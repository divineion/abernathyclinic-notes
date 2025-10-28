package com.medilabo.abernathyclinic.notes.entity;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Note {
	@Id
	private String id;
	private String patientUuid;
	private String doctorId;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
	private String content;
	
	public Note(String patientUuid, String doctorId, LocalDateTime createdAt,
			LocalDateTime updatedAt, String content) {
		this.patientUuid = patientUuid;
		this.doctorId = doctorId;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
		this.content = content;
	}

	public String getId() {
		return id;
	}
	
	public String getPatientUuid() {
		return patientUuid;
	}
	
	public void setPatientUuid(String patientUuid) {
		this.patientUuid = patientUuid;
	}
	
	public String getDoctorId() {
		return doctorId;
	}
	
	public void setDoctorId(String doctorId) {
		this.doctorId = doctorId;
	}
	
	public LocalDateTime getCreatedAt() {
		return createdAt;
	}
	
	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}
	
	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}
	
	public void setUpdatedAt(LocalDateTime updatedAt) {
		this.updatedAt = updatedAt;
	}
	
	public String getContent() {
		return content;
	}
	
	public void setContent(String content) {
		this.content = content;
	}
	
	@Override
	public String toString() {
		StringBuilder string = new StringBuilder()
				.append("patientUuid: ")
				.append(this.patientUuid)
				.append(" - creation date: ")
				.append(this.createdAt);
			
				return string.toString();
	}
}
