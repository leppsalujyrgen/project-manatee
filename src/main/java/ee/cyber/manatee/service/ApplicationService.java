package ee.cyber.manatee.service;


import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;
import java.time.Instant;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import ee.cyber.manatee.model.Application;
import ee.cyber.manatee.repository.ApplicationRepository;
import ee.cyber.manatee.statemachine.ApplicationState;
import ee.cyber.manatee.statemachine.ApplicationStateMachine;


@Slf4j
@Service
@RequiredArgsConstructor
public class ApplicationService {

    private final ApplicationRepository applicationRepository;
    private final ApplicationStateMachine applicationStateMachine;

    public List<Application> getApplications() {
        return applicationRepository.findAll();
    }

    public Application insertApplication(Application application) {
        application.setId(null);
        application.setApplicationState(ApplicationState.NEW);
        application.setUpdatedOn(OffsetDateTime.ofInstant(Instant.now(), ZoneOffset.UTC));

        return applicationRepository.save(application);
    }

    public Optional<Application> getApplicationById(Integer applicationId) {
        return applicationRepository.findById(applicationId);
    }

    public void rejectApplication(Integer applicationId) {
        applicationStateMachine.rejectApplication(applicationId);
    }

    public void scheduleApplication(Integer applicationId) {
        applicationStateMachine.scheduleApplication(applicationId);
    }

}
