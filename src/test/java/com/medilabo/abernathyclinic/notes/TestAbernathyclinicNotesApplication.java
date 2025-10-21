package com.medilabo.abernathyclinic.notes;

import org.springframework.boot.SpringApplication;

public class TestAbernathyclinicNotesApplication {

	public static void main(String[] args) {
		SpringApplication.from(AbernathyclinicNotesApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
