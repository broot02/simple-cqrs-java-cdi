package io.github.broot02.simplecqrsjava.cdi.events.fakes;

import io.github.broot02.simplecqrsjava.core.events.Event;

public class UnregisteredEvent extends Event<Boolean> {
    public UnregisteredEvent(Boolean data) {
        super(data);
    }
}
