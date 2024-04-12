package ee.cyber.manatee.statemachine;


import jakarta.transaction.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.statemachine.support.DefaultStateMachineContext;
import org.springframework.stereotype.Component;

import ee.cyber.manatee.repository.ApplicationRepository;


@Slf4j
@Component
@RequiredArgsConstructor
public class ApplicationStateMachineImpl implements ApplicationStateMachine {

    public static final String APPLICATION_ID_HEADER = "application_id";

    private final ApplicationRepository applicationRepository;
    private final StateMachineFactory<ApplicationState, ApplicationEvent> stateMachineFactory;
    private final ApplicationStateInterceptor applicationStateInterceptor;


    @Override
    @Transactional
    public StateMachine<ApplicationState, ApplicationEvent> rejectApplication(
            Integer applicationId) {
        val stateMachine = build(applicationId);
        sendEvent(applicationId, stateMachine, ApplicationEvent.REJECT);

        return stateMachine;
    }

    private void sendEvent(Integer applicationId,
                           StateMachine<ApplicationState, ApplicationEvent> stateMachine,
                           ApplicationEvent applicationEvent) {
        val message = MessageBuilder
                .withPayload(applicationEvent)
                .setHeader(APPLICATION_ID_HEADER, applicationId)
                .build();

        stateMachine.sendEvent(message);
    }


    private StateMachine<ApplicationState, ApplicationEvent> build(Integer applicationId) {
        val application = applicationRepository
                .findById(applicationId)
                .orElseThrow(() -> {
                    log.error("Couldn't find the application with given id {}", applicationId);
                    throw new IllegalArgumentException("Invalid application id");
                });

        val stateMachine = stateMachineFactory.getStateMachine(String.valueOf(application.getId()));
        stateMachine.stop();

        stateMachine.getStateMachineAccessor()
                    .doWithAllRegions(sma -> {
                        sma.addStateMachineInterceptor(applicationStateInterceptor);
                        sma.resetStateMachine(new DefaultStateMachineContext<>(
                                application.getApplicationState(), null, null, null));
                    });

        stateMachine.start();
        return stateMachine;
    }
}
