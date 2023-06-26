package io.github.broot02.simplecqrsjava.cdi.events.fakes;

import io.github.broot02.simplecqrsjava.core.events.EventHandler;
import jakarta.enterprise.context.RequestScoped;

@RequestScoped
public class FakeEventHandler implements EventHandler<FakeEvent> {
    @Override
    public void handle(FakeEvent fakeEvent) {

    }
}
