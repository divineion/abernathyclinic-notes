package com.medilabo.abernathyclinic.notes.config.demo;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import com.medilabo.abernathyclinic.notes.entity.Note;
import com.medilabo.abernathyclinic.notes.repository.CustomizedNoteRepositoryImpl;
import com.medilabo.abernathyclinic.notes.repository.NoteRepository;

import reactor.core.publisher.Mono;

@Profile("dev")
@Component
public class DataLoader implements CommandLineRunner{
	private NoteRepository repository;
	private CustomizedNoteRepositoryImpl customRepository;

	public DataLoader(NoteRepository repository, CustomizedNoteRepositoryImpl customRepository) {
		this.repository = repository;
		this.customRepository = customRepository;
	}
	
	private static final String PATIENT_1_ID = "1";
	private static final String PATIENT_2_ID = "2";
	private static final String PATIENT_3_ID = "3";
	private static final String PATIENT_4_ID = "4";

	private static final String DOCTOR_1_ID = "4";
	private static final String DOCTOR_2_ID = "5";

	@Override
	public void run(String... args) throws Exception {
		List<Note> notes = new ArrayList<>();
				
		Note testNote1 = new Note(PATIENT_1_ID, DOCTOR_1_ID, LocalDateTime.of(2025, Month.AUGUST, 25, 10, 40), null, "Le patient déclare qu'il 'se sent très bien' \n "
						+ "Poids égal ou inférieur au poids recommandé");
		addIfNotExists(testNote1, notes);

		Note testNote2 = new Note(PATIENT_2_ID, DOCTOR_1_ID, LocalDateTime.of(2025, Month.AUGUST, 25, 10, 40), null, 
				"Le patient déclare qu'il ressent beaucoup de stress au travail \n "
				+ "Il se plaint également que son audition est anormale dernièrement");
		addIfNotExists(testNote2, notes);
				
		Note testNote3 = new Note(PATIENT_2_ID, DOCTOR_1_ID, LocalDateTime.of(2025, Month.OCTOBER, 25, 15, 10), null, 
				"Le patient déclare avoir fait une réaction aux médicaments au cours des 3 derniers mois \n"
				+ " Il remarque également que son audition continue d'être anormale");
		addIfNotExists(testNote3, notes);
		
		Note testNote4 = new Note(PATIENT_3_ID, DOCTOR_2_ID, LocalDateTime.of(2023, Month.AUGUST, 22, 14, 15), null, 
				"Le patient déclare qu'il qu'il fume depuis peu");
		addIfNotExists(testNote4, notes);
							
		Note testNote5 = new Note(PATIENT_3_ID, DOCTOR_2_ID, LocalDateTime.of(2025, Month.FEBRUARY, 25, 14, 40), null, 
				"Le patient déclare qu'il est fumeur et qu'il a cessé de fumer l'année dernière  \n "
				+ "Il se plaint également de crises d’apnée respiratoire anormales \n "
				+ "Tests de laboratoire indiquant un taux de cholestérol LDL élevé");
		addIfNotExists(testNote5, notes);
		
		Note testNote6 = new Note(PATIENT_4_ID, DOCTOR_2_ID, LocalDateTime.of(2023, Month.AUGUST, 25, 14, 40), null, 
				"Le patient déclare qu'il lui est devenu difficile de monter les escaliers \n"
				+ "Il se plaint également d’être essoufflé \n"
				+ "Tests de laboratoire indiquant que les anticorps sont élevés \n"
				+ "Réaction aux médicaments");		
		addIfNotExists(testNote6, notes);
		
		Note testNote7 = new Note(PATIENT_4_ID, DOCTOR_2_ID, LocalDateTime.of(2023, Month.DECEMBER, 25, 14, 40), null, 
				"Le patient déclare qu'il a mal au dos lorsqu'il reste assis pendant longtemps");
		addIfNotExists(testNote7, notes);
				
		Note testNote8 = new Note(PATIENT_4_ID, DOCTOR_2_ID, LocalDateTime.of(2024, Month.MAY, 25, 9, 15), null,
				"Le patient déclare avoir commencé à fumer depuis peu \n"
						+ "Hémoglobine A1C supérieure au niveau recommandé");
		addIfNotExists(testNote8, notes);
		
		Note testNote9 = new Note(PATIENT_4_ID, DOCTOR_2_ID, LocalDateTime.of(2025, Month.AUGUST, 18, 14, 40), null, 
				"Taille, Poids, Cholestérol, Vertige et Réaction");
		addIfNotExists(testNote9, notes);
				
		notes.forEach((note) -> {
			try { 
		// block() termione la chaîne des traitements asynchrones et retourne la valeur
		// je bloque le flux pour que les données soient créées avant l'initialisation de l'app 
				repository.insert(note).block();
			} catch (Exception e) {
				System.err.print("Insertion error for note " + note);
			}
		});
	}
	
	private void addIfNotExists(Note note, List<Note> notes) {
			customRepository.findByPatientIdAndCreatedAt(note.getPatientId(), note.getCreatedAt())
				.switchIfEmpty(Mono.fromRunnable(() -> notes.add(note)))
				.block();
	}
}
