package io.github.broot02.simplecqrsjava.cdi.events.fakes;

import io.github.broot02.simplecqrsjava.core.events.EventBehavior;
import jakarta.enterprise.context.RequestScoped;

@RequestScoped
public class FakeEventValidationBehavior implements EventBehavior<FakeEvent> {
    @Override
    public void handle(FakeEvent o) {

    }
}
