package ee.cyber.manatee.statemachine;


import org.springframework.statemachine.StateMachine;


public interface ApplicationStateMachine {

    StateMachine<ApplicationState, ApplicationEvent> rejectApplication(
            Integer applicationId);
}
