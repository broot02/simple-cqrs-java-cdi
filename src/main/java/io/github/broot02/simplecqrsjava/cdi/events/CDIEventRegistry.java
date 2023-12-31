package io.github.broot02.simplecqrsjava.cdi.events;

import io.github.broot02.simplecqrsjava.core.events.Event;
import io.github.broot02.simplecqrsjava.core.events.EventBehavior;
import io.github.broot02.simplecqrsjava.core.events.EventHandler;
import io.github.broot02.simplecqrsjava.core.events.EventRegistry;
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
public class CDIEventRegistry implements EventRegistry {
    private final Map<Type, Class<?>> handlerProviderMap = new HashMap<>();
    private final Map<Type, List<Class<?>>> eventBehaviorMap = new HashMap<>();
    private final Logger logger;

    public CDIEventRegistry() {
        this.logger = LoggerFactory.getLogger(this.getClass());
        registerHandlers();
        registerBehaviors();
    }

    protected void registerHandlers() {
        logger.info("Registering Event Handlers");
        var beanManager = CDI.current().getBeanManager();
        var beanList = beanManager.getBeans(Object.class, new AnnotationLiteral<Any>(){});

        for (Bean<?> bean: beanList) {
            if (EventHandler.class.isAssignableFrom(bean.getBeanClass())) {
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
                    logger.info("{} - Event Handler registered for event - {}", bean.getBeanClass().getSimpleName(), type.getTypeName());
                    handlerProviderMap.put(type, bean.getBeanClass());
                } else  {
                    logger.warn("{} - Event has already been registered with handler", type.getTypeName());
                }
            }
        }
    }

    protected void registerBehaviors() {
        logger.info("Registering Event Behaviors");
        var beanManager = CDI.current().getBeanManager();
        var beanList = beanManager.getBeans(Object.class, new AnnotationLiteral<Any>(){});

        for (Bean<?> bean: beanList) {
            if (EventBehavior.class.isAssignableFrom(bean.getBeanClass())) {
                Type type = null;

                for (var object: bean.getTypes().toArray()) {
                    if (object instanceof ParameterizedType) {
                        type = ((ParameterizedType)object).getActualTypeArguments()[0];
                    }
                }

                if (type == null) {
                    break;
                }

                if (!eventBehaviorMap.containsKey(type)) {
                    logger.info("{} - Event Behavior registered for event - {}", bean.getBeanClass().getSimpleName(), type.getTypeName());
                    eventBehaviorMap.put(type, List.of(bean.getBeanClass()));
                } else  {
                    logger.info("{} - Event Behavior registered for event - {}", bean.getBeanClass().getSimpleName(), type.getTypeName());
                    var behaviors = eventBehaviorMap.get(type);
                    behaviors.add(bean.getBeanClass());
                    eventBehaviorMap.replace(type, behaviors);
                }
            }
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends Event<?>, T2 extends EventHandler<T>> T2 getEventHandler(T t) {
        if (!handlerProviderMap.containsKey(t.getClass())) {
            return null;
        }

        return (T2) CDI.current().select(handlerProviderMap.get(t.getClass())).get();
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends Event<R>, R> List<EventBehavior<T>> getEventBehaviors(T t) {
        if (!eventBehaviorMap.containsKey(t.getClass())) {
            return new ArrayList<>();
        }

        return eventBehaviorMap.get(t.getClass()).stream().map(behaviorClass ->
                (EventBehavior<T>)CDI.current().select(behaviorClass).get()).collect(Collectors.toList());
    }
}
