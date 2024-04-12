package ee.cyber.manatee.config;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import ee.cyber.manatee.dto.ApplicationDto;
import ee.cyber.manatee.dto.ApplicationStateDto;
import ee.cyber.manatee.dto.CandidateDto;
import ee.cyber.manatee.dto.InterviewDto;
import ee.cyber.manatee.mapper.ApplicationMapper;
import ee.cyber.manatee.mapper.InterviewMapper;
import ee.cyber.manatee.model.Application;
import ee.cyber.manatee.repository.ApplicationRepository;
import ee.cyber.manatee.repository.InterviewRepository;
import ee.cyber.manatee.service.ApplicationService;
import ee.cyber.manatee.service.InterviewService;
import ee.cyber.manatee.statemachine.ApplicationState;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import lombok.val;

@Configuration
@AllArgsConstructor
public class DatabaseSeedConfig {

    private final ApplicationService applicationService;
    private final InterviewService interviewService;
    private final ApplicationMapper applicationMapper;
    private final InterviewMapper interviewMapper;

    @PostConstruct
    public void addSeedData() {
        List<String> firstNames = Arrays.asList("John", "Jane", "Bob", "Alice");
        List<String> lastNames = Arrays.asList("Smith", "Davis", "Johnson", "Wilson");
        List<String> interviewDates = Arrays.asList("2024-05-04T09:15Z", "2024-04-30T12:00Z");

        for (int i = 0; i < firstNames.size(); i++) {
            // int i = 0;
            val candidateDto = CandidateDto.builder().firstName(firstNames.get(i)).lastName(lastNames.get(i))
                    .build();
            val applicationDto = ApplicationDto.builder().candidate(candidateDto).build();
            applicationDto.setApplicationState(ApplicationStateDto.NEW);
            val application = applicationService
                    .insertApplication(applicationMapper.dtoToEntity(applicationDto));
            if ((i < interviewDates.size()) && (interviewDates.get(i) != null)) {
                interviewService.addInterview(
                        interviewMapper.dtoToEntity(
                                InterviewDto.builder()
                                        .application(applicationMapper.entityToDto(application))
                                        .time(OffsetDateTime.parse(interviewDates.get(i)))
                                        .build()));
            }
        }
    }

}
