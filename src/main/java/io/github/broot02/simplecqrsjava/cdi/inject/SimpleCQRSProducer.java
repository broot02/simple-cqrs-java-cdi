package io.github.broot02.simplecqrsjava.cdi.inject;

import io.github.broot02.simplecqrsjava.cdi.commands.CDICommandRegistry;
import io.github.broot02.simplecqrsjava.cdi.events.CDIEventRegistry;
import io.github.broot02.simplecqrsjava.cdi.queries.CDIQueryRegistry;
import io.github.broot02.simplecqrsjava.core.commands.CommandBus;
import io.github.broot02.simplecqrsjava.core.commands.CommandRegistry;
import io.github.broot02.simplecqrsjava.core.events.EventBus;
import io.github.broot02.simplecqrsjava.core.events.EventRegistry;
import io.github.broot02.simplecqrsjava.core.queries.QueryBus;
import io.github.broot02.simplecqrsjava.core.queries.QueryRegistry;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.Dependent;
import jakarta.enterprise.inject.Default;
import jakarta.enterprise.inject.Produces;

/**
 * CDI producer for CQRS components.
 *
 * @author Brad Root
 */
@ApplicationScoped
@Dependent
public class SimpleCQRSProducer {

    @Produces
    @Default
    @ApplicationScoped
    protected EventRegistry getEventRegistry() {
        return new CDIEventRegistry();
    }

    @Produces
    @Default
    @ApplicationScoped
    protected EventBus getEventBus(EventRegistry eventRegistry) {
        return new EventBus(eventRegistry);
    }

    @Produces
    @Default
    @ApplicationScoped
    protected CommandRegistry getCommandRegistry() {
        return new CDICommandRegistry();
    }

    @Produces
    @Default
    @ApplicationScoped
    protected CommandBus getCommandBus(CommandRegistry commandRegistry) {
        return new CommandBus(commandRegistry);
    }

    @Produces
    @Default
    @ApplicationScoped
    protected QueryRegistry getQueryRegistry() {
        return new CDIQueryRegistry();
    }

    @Produces
    @Default
    @ApplicationScoped
    protected QueryBus getCommandBus(QueryRegistry queryRegistry) {
        return new QueryBus(queryRegistry);
    }

}
