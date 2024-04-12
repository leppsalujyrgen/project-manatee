package ee.cyber.manatee.controller;

import java.util.List;
import java.util.ArrayList;
import java.util.Optional;
import java.time.OffsetDateTime;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import ee.cyber.manatee.api.ApplicationApi;
import ee.cyber.manatee.dto.ApplicationDto;
import ee.cyber.manatee.dto.ApplicationStateDto;
import ee.cyber.manatee.dto.InterviewDto;
import ee.cyber.manatee.mapper.ApplicationMapper;
import ee.cyber.manatee.mapper.InterviewMapper;
import ee.cyber.manatee.service.ApplicationService;
import ee.cyber.manatee.service.InterviewService;
import jakarta.validation.OverridesAttribute;
import jakarta.validation.Valid;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ApplicationApiImpl implements ApplicationApi {

    private final ApplicationMapper applicationMapper;
    private final ApplicationService applicationService;
    private final InterviewMapper interviewMapper;
    private final InterviewService interviewService;

    @Override
    public ResponseEntity<List<ApplicationDto>> getApplications() {
        return ResponseEntity.ok(applicationMapper.entitiesToDtoList(applicationService.getApplications()));
    }

    @Override
    public ResponseEntity<List<InterviewDto>> getInterviews() {
        return ResponseEntity.ok(interviewMapper.entitiesToDtoList(interviewService.getInterviews()));
    }

    @Override
    public ResponseEntity<ApplicationDto> addApplication(ApplicationDto applicationDto) {
        val draftApplication = applicationMapper.dtoToEntity(applicationDto);
        val application = applicationService.insertApplication(draftApplication);

        return ResponseEntity.status(CREATED)
                .body(applicationMapper.entityToDto(application));
    }

    @Override
    public ResponseEntity<InterviewDto> scheduleInterview(
            @PathVariable("applicationId") Integer applicationId,
            @Valid @RequestBody InterviewDto interviewDto) {
        val draftInterview = interviewMapper.dtoToEntity(interviewDto);
        val interview = interviewService.addInterview(draftInterview);
        return ResponseEntity.status(CREATED).body(interviewMapper.entityToDto(interview));
    }

    @Override
    public ResponseEntity<Void> rejectApplication(Integer applicationId) {
        try {
            applicationService.rejectApplication(applicationId);
            return ResponseEntity.status(HttpStatus.ACCEPTED).build();
        } catch (IllegalArgumentException exception) {
            throw new ResponseStatusException(NOT_FOUND, "Invalid application id", exception);
        }
    }

}
