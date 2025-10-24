package com.medilabo.abernathyclinic.notes.config.demo;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.medilabo.abernathyclinic.notes.entity.Note;
import com.medilabo.abernathyclinic.notes.repository.NoteRepository;

@Component
public class DataLoader implements CommandLineRunner{
	private NoteRepository repository;


	public DataLoader(NoteRepository repository) {
		this.repository = repository;
	}

	@Override
	public void run(String... args) throws Exception {
		List<Note> notes = new ArrayList<>();
		Note note1 = new Note();
		note1.setPatientId("1");
		note1.setCreatedAt(LocalDateTime.now());
		note1.setContent("Le patient déclare qu'il 'se sent très bien' \n "
				+ "Poids égal ou inférieur au poids recommandé");
		note1.setDoctorId("4");
		
		notes.add(note1);
		
		Note note2 = new Note();
		note2.setPatientId("2");
		note2.setCreatedAt(LocalDateTime.now().minusMonths(3L));
		note2.setContent("Le patient déclare qu'il ressent beaucoup de stress au travail \n "
				+ "Il se plaint également que son audition est anormale dernièrement");
		note2.setDoctorId("4");
		
		notes.add(note2);

		Note note3 = new Note();
		note3.setPatientId("2");
		note3.setCreatedAt(LocalDateTime.now());
		note3.setContent("Le patient déclare avoir fait une réaction aux médicaments au cours des 3 derniers mois \n"
				+ " Il remarque également que son audition continue d'être anormale");
		note3.setDoctorId("4");
		
		notes.add(note3);
				
		Note note4 = new Note();
		note4.setPatientId("3");
		note4.setCreatedAt(LocalDateTime.now().minusMonths(6L));
		note4.setContent("Le patient déclare qu'il fume depuis peu");
		note4.setDoctorId("5");
		
		notes.add(note4);

		Note note5 = new Note();
		note5.setPatientId("3");
		note5.setCreatedAt(LocalDateTime.now().minusDays(3L));
		note5.setContent("Le patient déclare qu'il est fumeur et qu'il a cessé de fumer l'année dernière  \n "
				+ "Il se plaint également de crises d’apnée respiratoire anormales \n "
				+ "Tests de laboratoire indiquant un taux de cholestérol LDL élevé");
		note5.setDoctorId("5");
		
		notes.add(note5);

		Note note6 = new Note();
		note6.setPatientId("4");
		note6.setCreatedAt(LocalDateTime.now().minusMonths(11L));
		note6.setContent(" Le patient déclare qu'il lui est devenu difficile de monter les escaliers \n "
				+ "Il se plaint également d’être essoufflé \n "
				+ "Tests de laboratoire indiquant que les anticorps sont élevés \n"
				+ " Réaction aux médicaments");
		note6.setDoctorId("6");
		
		notes.add(note6);

		Note note7 = new Note();
		note7.setPatientId("4");
		note7.setCreatedAt(LocalDateTime.now().minusMonths(8L));
		note7.setContent("Le patient déclare qu'il a mal au dos lorsqu'il reste assis pendant longtemps");
		note7.setDoctorId("6");
		
		notes.add(note7);
		
		Note note8 = new Note();
		note8.setPatientId("4");
		note8.setCreatedAt(LocalDateTime.now().minusMonths(1L));
		note8.setContent("Le patient déclare avoir commencé à fumer depuis peu \n "
				+ "Hémoglobine A1C supérieure au niveau recommandé");
		note8.setDoctorId("6");
		
		notes.add(note8);

		Note note9 = new Note();
		note9.setPatientId("4");
		note9.setCreatedAt(LocalDateTime.now().minusDays(2));
		note9.setContent("Taille, Poids, Cholestérol, Vertige et Réaction");
		note9.setDoctorId("6");
		
		notes.add(note9);
		
		// bloquer le flux pour que les données soient créées avant l'initialisation 
		 notes.forEach(note -> repository.insert(note).block());
		
	}
}
