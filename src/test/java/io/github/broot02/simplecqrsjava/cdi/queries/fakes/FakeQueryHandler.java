package io.github.broot02.simplecqrsjava.cdi.queries.fakes;

import io.github.broot02.simplecqrsjava.core.queries.QueryHandler;
import jakarta.enterprise.context.RequestScoped;

@RequestScoped
public class FakeQueryHandler implements QueryHandler<Boolean, FakeQuery> {
    @Override
    public Boolean handle(FakeQuery fakeQuery) {
        return true;
    }
}
