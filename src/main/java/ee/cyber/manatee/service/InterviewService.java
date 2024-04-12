package ee.cyber.manatee.service;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.time.Instant;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.stereotype.Service;

import ee.cyber.manatee.dto.InterviewDto;
import ee.cyber.manatee.model.Application;
import ee.cyber.manatee.model.Interview;
import ee.cyber.manatee.repository.ApplicationRepository;
import ee.cyber.manatee.repository.InterviewRepository;
import ee.cyber.manatee.statemachine.ApplicationState;
import ee.cyber.manatee.statemachine.ApplicationStateMachine;


@Slf4j
@Service
@RequiredArgsConstructor
public class InterviewService {
    private final InterviewRepository interviewRepository;
    private final ApplicationService applicationService;

    public List<Interview> getInterviews() {
        return interviewRepository.findAll();
    }

    public Interview addInterview(Interview interview) {
        applicationService.scheduleApplication(interview.getApplication().getId());
        interview.setId(null);
        val saved = interviewRepository.save(interview);
        return interviewRepository.findById(saved.getId()).get();
    }
}
