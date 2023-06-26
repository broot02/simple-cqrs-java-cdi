package io.github.broot02.simplecqrsjava.cdi.commands;

import io.github.broot02.simplecqrsjava.cdi.commands.fakes.FakeCommand;
import io.github.broot02.simplecqrsjava.cdi.commands.fakes.FakeCommandHandler;
import io.github.broot02.simplecqrsjava.cdi.commands.fakes.UnregisteredCommand;
import org.jboss.weld.junit5.auto.AddPackages;
import org.jboss.weld.junit5.auto.EnableAutoWeld;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@EnableAutoWeld
@AddPackages({FakeCommandHandler.class, CDICommandRegistry.class})
class CDICommandRegistryTest {

    @Test
    void should_ReturnHandler_When_CommandHandlerIsRegistered(CDICommandRegistry registry) {
        var handler = registry.getCommandHandler(new FakeCommand());

        assertNotNull(handler);
    }

    @Test
    void should_ReturnNull_When_CommandHandlerIsNotRegistered(CDICommandRegistry registry) {
        var handler = registry.getCommandHandler(new UnregisteredCommand());

        assertNull(handler);
    }
}