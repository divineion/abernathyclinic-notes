package com.medilabo.abernathyclinic.notes;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@Import(TestcontainersConfiguration.class)
@SpringBootTest
class AbernathyclinicNotesApplicationIT {

	@Test
	void contextLoads() {
	}

}
