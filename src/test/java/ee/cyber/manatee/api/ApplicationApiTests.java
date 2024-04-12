package ee.cyber.manatee.api;


import lombok.val;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;

import ee.cyber.manatee.dto.ApplicationDto;
import ee.cyber.manatee.dto.CandidateDto;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


@SpringBootTest
public class ApplicationApiTests {

    @Autowired
    private ApplicationApi applicationApi;


    @Test
    public void submitApplicationWithValidData() {
        val draftCandidate = CandidateDto
                .builder().firstName("Mari").lastName("Maasikas").build();
        val draftApplication = ApplicationDto
                .builder().candidate(draftCandidate).build();

        val response = applicationApi.addApplication(draftApplication);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());

        val application = response.getBody();
        assertNotNull(application);
        assertNotNull(application.getId());
        assertNotNull(application.getApplicationState());
        assertNotNull(application.getUpdatedOn());

        assertEquals(draftApplication.getCandidate().getFirstName(),
                     application.getCandidate().getFirstName());
        assertEquals(draftApplication.getCandidate().getLastName(),
                     application.getCandidate().getLastName());
    }


}
