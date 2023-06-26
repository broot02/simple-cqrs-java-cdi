package io.github.broot02.simplecqrsjava.cdi.commands.fakes;

import io.github.broot02.simplecqrsjava.core.commands.CommandHandler;
import jakarta.enterprise.context.RequestScoped;

@RequestScoped
public class FakeCommandHandler implements CommandHandler<FakeCommand, Boolean> {
    @Override
    public Boolean handle(FakeCommand fakeCommand) {
        return true;
    }
}
