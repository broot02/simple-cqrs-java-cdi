package io.github.broot02.simplecqrsjava.cdi.commands;

import io.github.broot02.simplecqrsjava.core.commands.Command;
import io.github.broot02.simplecqrsjava.core.commands.CommandBehavior;
import io.github.broot02.simplecqrsjava.core.commands.CommandHandler;
import io.github.broot02.simplecqrsjava.core.commands.CommandRegistry;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Any;
import jakarta.enterprise.inject.spi.Bean;
import jakarta.enterprise.inject.spi.CDI;
import jakarta.enterprise.util.AnnotationLiteral;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@ApplicationScoped
public class CDICommandRegistry implements CommandRegistry {

    private final Map<Type, Class<?>> handlerProviderMap = new HashMap<>();
    private final Map<Type, List<Class<?>>> commandBehaviorMap = new HashMap<>();
    private final Logger logger;

    public CDICommandRegistry() {
        this.logger = LoggerFactory.getLogger(this.getClass());
        registerHandlers();
        registerBehaviors();
    }

    protected void registerHandlers() {
        logger.info("Registering Command Handlers");
        var beanManager = CDI.current().getBeanManager();
        var beanList = beanManager.getBeans(Object.class, new AnnotationLiteral<Any>(){});

        for (Bean<?> bean: beanList) {
            if (CommandHandler.class.isAssignableFrom(bean.getBeanClass())) {
                Type type = null;

                for (var object: bean.getTypes().toArray()) {
                    if (object instanceof ParameterizedType) {
                        type = ((ParameterizedType)object).getActualTypeArguments()[0];
                    }
                }

                if (type == null) {
                    break;
                }

                if (!handlerProviderMap.containsKey(type)) {
                    logger.info("{} - Command Handler registered for command - {}", bean.getBeanClass().getSimpleName(), type.getTypeName());
                    handlerProviderMap.put(type, bean.getBeanClass());
                } else  {
                    logger.warn("{} - Command has already been registered with handler", type.getTypeName());
                }
            }
        }
    }

    protected void registerBehaviors() {
        logger.info("Registering Command Behaviors");
        var beanManager = CDI.current().getBeanManager();
        var beanList = beanManager.getBeans(Object.class, new AnnotationLiteral<Any>(){});

        for (Bean<?> bean: beanList) {
            if (CommandBehavior.class.isAssignableFrom(bean.getBeanClass())) {
                Type type = null;

                for (var object: bean.getTypes().toArray()) {
                    if (object instanceof ParameterizedType) {
                        type = ((ParameterizedType)object).getActualTypeArguments()[0];
                    }
                }

                if (type == null) {
                    break;
                }

                if (!commandBehaviorMap.containsKey(type)) {
                    logger.info("{} - Command Behavior registered for command - {}", bean.getBeanClass().getSimpleName(), type.getTypeName());
                    commandBehaviorMap.put(type, List.of(bean.getBeanClass()));
                } else  {
                    logger.info("{} - Command Behavior registered for command - {}", bean.getBeanClass().getSimpleName(), type.getTypeName());
                    var behaviors = commandBehaviorMap.get(type);
                    behaviors.add(bean.getBeanClass());
                    commandBehaviorMap.replace(type, behaviors);
                }
            }
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public <R, T extends Command<R>, T2 extends CommandHandler<T, R>> T2 getCommandHandler(T t) {
        if (!handlerProviderMap.containsKey(t.getClass())) {
            return null;
        }

        return (T2)CDI.current().select(handlerProviderMap.get(t.getClass())).get();
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends Command<R>, R> List<CommandBehavior<T>> getCommandBehaviors(T t) {
        if (!commandBehaviorMap.containsKey(t.getClass())) {
            return new ArrayList<>();
        }

        return commandBehaviorMap.get(t.getClass()).stream().map(behaviorClass ->
                (CommandBehavior<T>)CDI.current().select(behaviorClass).get()).collect(Collectors.toList());
    }

}
