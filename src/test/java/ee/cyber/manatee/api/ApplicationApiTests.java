package ee.cyber.manatee.api;

import java.time.OffsetDateTime;

import lombok.val;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;

import ee.cyber.manatee.dto.ApplicationDto;
import ee.cyber.manatee.dto.ApplicationStateDto;
import ee.cyber.manatee.dto.CandidateDto;
import ee.cyber.manatee.dto.InterviewDto;
import ee.cyber.manatee.statemachine.ApplicationState;
import ee.cyber.manatee.api.*;


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

        @Test
        public void addApplicationWithValidData_ReturnsCreatedStatus() {
                val draftCandidate = CandidateDto.builder().firstName("Kadi").lastName("Juurikas").build();
                val draftApplication = ApplicationDto.builder().candidate(draftCandidate).build();                
                val response = applicationApi.addApplication(draftApplication);                
                assertEquals(HttpStatus.CREATED, response.getStatusCode());
        }

        @Test
        public void addApplicationWithValidData_ReturnsNewApplicationState() {
                val draftCandidate = CandidateDto.builder().firstName("Kadi").lastName("Juurikas").build();
                val draftApplication = ApplicationDto.builder().candidate(draftCandidate).build();                
                val response = applicationApi.addApplication(draftApplication);
                val application = response.getBody();                
                assertNotNull(application);
                assertEquals(ApplicationStateDto.NEW, application.getApplicationState());
        }

        @Test
        public void scheduleInterviewWithValidData_ReturnsCreatedStatus() {
                val draftCandidate = CandidateDto.builder().firstName("Kadi").lastName("Juurikas").build();
                val draftApplication = ApplicationDto.builder().candidate(draftCandidate).build();
                val response1 = applicationApi.addApplication(draftApplication);
                val application = response1.getBody();                
                val draftInterview = InterviewDto.builder().time(OffsetDateTime.parse("2024-05-04T10:45:00+03:00")).build();
                val response2 = applicationApi.scheduleInterview(application.getId(), draftInterview);                
                assertEquals(HttpStatus.CREATED, response2.getStatusCode());
        }

        @Test
        public void scheduleInterviewWithValidData_ReturnsInterviewWithApplicationInInterviewState() {
                val draftCandidate = CandidateDto.builder().firstName("Kadi").lastName("Juurikas").build();
                val draftApplication = ApplicationDto.builder().candidate(draftCandidate).build();
                val response1 = applicationApi.addApplication(draftApplication);
                val application = response1.getBody();                
                val draftInterview = InterviewDto.builder().time(OffsetDateTime.parse("2024-05-04T10:45:00+03:00")).build();
                val response2 = applicationApi.scheduleInterview(application.getId(), draftInterview);
                val interview = response2.getBody();                
                assertNotNull(interview);
                assertNotNull(interview.getApplication());
                assertEquals(ApplicationStateDto.INTERVIEW, interview.getApplication().getApplicationState());
        }

        @Test
        public void scheduleInterviewWithInvalidApplicationId_ReturnsNotFoundStatus() {
                int invalidApplicationId = -1;
                val draftInterview = InterviewDto.builder().time(OffsetDateTime.parse("2024-05-04T10:45:00+03:00")).build();
                val response = applicationApi.scheduleInterview(invalidApplicationId, draftInterview);
                assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        }
}
