package io.github.broot02.simplecqrsjava.cdi.events;

import io.github.broot02.simplecqrsjava.cdi.events.fakes.FakeEvent;
import io.github.broot02.simplecqrsjava.cdi.events.fakes.FakeEventHandler;
import io.github.broot02.simplecqrsjava.cdi.events.fakes.UnregisteredEvent;
import org.jboss.weld.junit5.auto.AddPackages;
import org.jboss.weld.junit5.auto.EnableAutoWeld;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

@EnableAutoWeld
@AddPackages({CDIEventRegistry.class, FakeEventHandler.class})
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

}
