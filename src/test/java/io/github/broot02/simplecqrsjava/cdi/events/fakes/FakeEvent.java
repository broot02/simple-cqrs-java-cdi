package io.github.broot02.simplecqrsjava.cdi.events.fakes;

import io.github.broot02.simplecqrsjava.core.events.Event;

public class FakeEvent extends Event<Boolean> {
    public FakeEvent(Boolean data) {
        super(data);
    }
}
