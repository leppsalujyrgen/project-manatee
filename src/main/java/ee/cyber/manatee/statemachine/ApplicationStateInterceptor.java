package ee.cyber.manatee.statemachine;


import java.time.OffsetDateTime;
import java.util.Optional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.state.State;
import org.springframework.statemachine.support.StateMachineInterceptorAdapter;
import org.springframework.statemachine.transition.Transition;
import org.springframework.stereotype.Component;

import ee.cyber.manatee.repository.ApplicationRepository;

import static ee.cyber.manatee.statemachine.ApplicationStateMachineImpl.APPLICATION_ID_HEADER;


@Slf4j
@Component
@RequiredArgsConstructor
public class ApplicationStateInterceptor
        extends StateMachineInterceptorAdapter<ApplicationState, ApplicationEvent> {
    private final ApplicationRepository applicationRepository;

    @Override
    public void preStateChange(
            State<ApplicationState, ApplicationEvent> state,
            Message<ApplicationEvent> message,
            Transition<ApplicationState, ApplicationEvent> transition,
            StateMachine<ApplicationState, ApplicationEvent> stateMachine
    ) {
        Optional.ofNullable(message)
                .flatMap(eventMessage -> Optional.ofNullable(
                        eventMessage.getHeaders()
                                    .get(APPLICATION_ID_HEADER, Integer.class)))
                .flatMap(applicationRepository::findById)
                .ifPresentOrElse(application -> {
                    application.setApplicationState(state.getId());
                    application.setUpdatedOn(OffsetDateTime.now());

                    applicationRepository.save(application);
                }, () -> {
                    log.error("Statemachine preStateChange failed! Possible empty message or "
                                      + "invalid application id detected.");
                    throw new IllegalArgumentException("Statemachine preStateChange failed!");
                });
    }
}
