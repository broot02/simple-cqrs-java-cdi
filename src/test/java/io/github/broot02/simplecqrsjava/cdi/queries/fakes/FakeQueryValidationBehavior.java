package io.github.broot02.simplecqrsjava.cdi.queries.fakes;

import io.github.broot02.simplecqrsjava.core.queries.QueryBehavior;
import jakarta.enterprise.context.RequestScoped;

@RequestScoped
public class FakeQueryValidationBehavior implements QueryBehavior<FakeQuery> {
    @Override
    public void handle(FakeQuery fakeQuery) {

    }
}
