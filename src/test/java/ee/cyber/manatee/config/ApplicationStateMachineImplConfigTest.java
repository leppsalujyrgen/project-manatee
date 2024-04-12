package ee.cyber.manatee.config;


import java.util.UUID;

import lombok.val;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.statemachine.config.StateMachineFactory;

import ee.cyber.manatee.statemachine.ApplicationEvent;
import ee.cyber.manatee.statemachine.ApplicationState;

import static org.junit.jupiter.api.Assertions.assertEquals;


@SpringBootTest
public class ApplicationStateMachineImplConfigTest {
    @Autowired
    StateMachineFactory<ApplicationState, ApplicationEvent> factory;


    @Test
    public void createNewStateMachine() {
        val stateMachine = factory.getStateMachine(UUID.randomUUID());
        stateMachine.start();
        assertEquals(ApplicationState.NEW, stateMachine.getState().getId());

        stateMachine.sendEvent(ApplicationEvent.REJECT);
        assertEquals(ApplicationState.REJECTED, stateMachine.getState().getId());
    }
}
