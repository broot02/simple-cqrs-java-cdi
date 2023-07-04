package io.github.broot02.simplecqrsjava.cdi.events;

import io.github.broot02.simplecqrsjava.cdi.events.fakes.FakeEvent;
import io.github.broot02.simplecqrsjava.cdi.events.fakes.FakeEventHandler;
import io.github.broot02.simplecqrsjava.cdi.events.fakes.FakeEventValidationBehavior;
import io.github.broot02.simplecqrsjava.cdi.events.fakes.UnregisteredEvent;
import org.jboss.weld.junit5.auto.AddPackages;
import org.jboss.weld.junit5.auto.EnableAutoWeld;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

@EnableAutoWeld
@AddPackages({CDIEventRegistry.class, FakeEventHandler.class, FakeEventValidationBehavior.class})
class CDIEventRegistryTest {

    @Test
    void should_ReturnHandler_When_EventHandlerIsRegistered(CDIEventRegistry registry) {
        var handler = registry.getEventHandler(new FakeEvent(true));

        assertNotNull(handler);
    }

    @Test
    void should_ReturnNull_When_EventHandlerIsNotRegistered(CDIEventRegistry registry) {
        var handler = registry.getEventHandler(new UnregisteredEvent(true));

        assertNull(handler);
    }

    @Test
    void should_ReturnBehavior_When_BehaviorIsRegistered(CDIEventRegistry registry) {
        var behaviors = registry.getEventBehaviors(new FakeEvent(true));

        assertFalse(behaviors.isEmpty());
    }

    @Test
    void should_ReturnEmpty_When_BehaviorIsNotRegistered(CDIEventRegistry registry) {
        var behaviors = registry.getEventBehaviors(new UnregisteredEvent(true));

        assertTrue(behaviors.isEmpty());
    }

}
