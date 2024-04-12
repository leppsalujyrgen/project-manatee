package ee.cyber.manatee.config;


import java.util.EnumSet;

import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.statemachine.config.EnableStateMachineFactory;
import org.springframework.statemachine.config.StateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineConfigurationConfigurer;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;
import org.springframework.statemachine.listener.StateMachineListenerAdapter;
import org.springframework.statemachine.state.State;

import ee.cyber.manatee.statemachine.ApplicationEvent;
import ee.cyber.manatee.statemachine.ApplicationState;


@Slf4j
@Configuration
@EnableStateMachineFactory
public class ApplicationStateMachineConfig
        extends StateMachineConfigurerAdapter<ApplicationState, ApplicationEvent> {

    @Override
    public void configure(StateMachineStateConfigurer<ApplicationState, ApplicationEvent> states)
            throws Exception {
        states.withStates()
              .initial(ApplicationState.NEW)
              .states(EnumSet.allOf(ApplicationState.class))
              .end(ApplicationState.HIRED)
              .end(ApplicationState.REJECTED);
    }

    @Override
    public void configure(
            StateMachineTransitionConfigurer<ApplicationState, ApplicationEvent> transitions)
            throws Exception {
        transitions.withExternal()
                   .source(ApplicationState.NEW)
                   .target(ApplicationState.REJECTED)
                   .event(ApplicationEvent.REJECT)

                   .and()
                   .withExternal()
                   .source(ApplicationState.INTERVIEW)
                   .target(ApplicationState.REJECTED)
                   .event(ApplicationEvent.REJECT)

                   .and()
                   .withExternal()
                   .source(ApplicationState.PRE_ONBOARD)
                   .target(ApplicationState.REJECTED)
                   .event(ApplicationEvent.REJECT)

                   .and()
                   .withExternal()
                   .source(ApplicationState.OFFER)
                   .target(ApplicationState.REJECTED)
                   .event(ApplicationEvent.REJECT);

    }

    @Override
    public void configure(
            StateMachineConfigurationConfigurer<ApplicationState, ApplicationEvent> config)
            throws Exception {
        val listenerAdapter =
                new StateMachineListenerAdapter<ApplicationState, ApplicationEvent>() {
                    @Override
                    public void stateChanged(
                            State<ApplicationState, ApplicationEvent> from,
                            State<ApplicationState, ApplicationEvent> to) {
                        log.info("State transition {} -> {}", from, to);
                    }

                    @Override
                    public void eventNotAccepted(Message<ApplicationEvent> event) {
                        log.error("Event {} not allowed in current state!", event);
                        throw new IllegalStateException("Event not allowed!");
                    }
                };

        config.withConfiguration().listener(listenerAdapter);
    }
}
