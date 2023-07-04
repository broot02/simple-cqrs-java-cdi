package io.github.broot02.simplecqrsjava.cdi.commands.fakes;

import io.github.broot02.simplecqrsjava.core.commands.CommandBehavior;
import jakarta.enterprise.context.RequestScoped;

@RequestScoped
public class FakeCommandValidationBehavior implements CommandBehavior<FakeCommand> {
    @Override
    public void handle(FakeCommand fakeCommand) {

    }
}
