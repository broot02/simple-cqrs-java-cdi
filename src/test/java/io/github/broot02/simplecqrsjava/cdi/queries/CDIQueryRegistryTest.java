package io.github.broot02.simplecqrsjava.cdi.queries;

import io.github.broot02.simplecqrsjava.cdi.queries.fakes.FakeQuery;
import io.github.broot02.simplecqrsjava.cdi.queries.fakes.FakeQueryHandler;
import io.github.broot02.simplecqrsjava.cdi.queries.fakes.FakeQueryValidationBehavior;
import io.github.broot02.simplecqrsjava.cdi.queries.fakes.UnregisteredQuery;
import org.jboss.weld.junit5.auto.AddPackages;
import org.jboss.weld.junit5.auto.EnableAutoWeld;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

@EnableAutoWeld
@AddPackages({CDIQueryRegistry.class, FakeQueryHandler.class, FakeQueryValidationBehavior.class})
class CDIQueryRegistryTest {

    @Test
    void should_ReturnHandler_When_QueryHandlerIsRegistered(CDIQueryRegistry registry) {
        var handler = registry.getQueryHandler(new FakeQuery());

        assertNotNull(handler);
    }

    @Test
    void should_ReturnNull_When_QueryHandlerIsNotRegistered(CDIQueryRegistry registry) {
        var handler = registry.getQueryHandler(new UnregisteredQuery());

        assertNull(handler);
    }

    @Test
    void should_ReturnBehavior_When_BehaviorIsRegistered(CDIQueryRegistry registry) {
        var behaviors = registry.getQueryBehaviors(new FakeQuery());

        assertFalse(behaviors.isEmpty());
    }

    @Test
    void should_ReturnEmpty_When_BehaviorIsNotRegistered(CDIQueryRegistry registry) {
        var behaviors = registry.getQueryBehaviors(new UnregisteredQuery());

        assertTrue(behaviors.isEmpty());
    }
}
