package io.github.broot02.simplecqrsjava.cdi.queries;

import io.github.broot02.simplecqrsjava.cdi.queries.fakes.FakeQuery;
import io.github.broot02.simplecqrsjava.cdi.queries.fakes.FakeQueryHandler;
import io.github.broot02.simplecqrsjava.cdi.queries.fakes.UnregisteredQuery;
import org.jboss.weld.junit5.auto.AddPackages;
import org.jboss.weld.junit5.auto.EnableAutoWeld;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

@EnableAutoWeld
@AddPackages({CDIQueryRegistry.class, FakeQueryHandler.class})
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
}
