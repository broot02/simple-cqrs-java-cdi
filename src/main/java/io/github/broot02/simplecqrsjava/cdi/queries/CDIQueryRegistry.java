package io.github.broot02.simplecqrsjava.cdi.queries;

import io.github.broot02.simplecqrsjava.core.queries.Query;
import io.github.broot02.simplecqrsjava.core.queries.QueryBehavior;
import io.github.broot02.simplecqrsjava.core.queries.QueryHandler;
import io.github.broot02.simplecqrsjava.core.queries.QueryRegistry;
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
public class CDIQueryRegistry implements QueryRegistry {

    private final Map<Type, Class<?>> handlerProviderMap = new HashMap<>();
    private final Map<Type, List<Class<?>>> queryBehaviorMap = new HashMap<>();
    private final Logger logger;

    public CDIQueryRegistry() {
        this.logger = LoggerFactory.getLogger(this.getClass());
        registerHandlers();
        registerBehaviors();
    }

    protected void registerHandlers() {
        logger.info("Registering Query Handlers");

        var beanManager = CDI.current().getBeanManager();
        var beanList = beanManager.getBeans(Object.class, new AnnotationLiteral<Any>(){});

        for (Bean<?> bean: beanList) {
            if (QueryHandler.class.isAssignableFrom(bean.getBeanClass())) {
                Type type = null;

                for (var object: bean.getTypes().toArray()) {
                    if (object instanceof ParameterizedType) {
                        type = ((ParameterizedType)object).getActualTypeArguments()[1];
                    }
                }

                if (type == null) {
                    break;
                }

                if (!handlerProviderMap.containsKey(type)) {
                    logger.info("{} - Query Handler registered for query - {}", bean.getBeanClass().getSimpleName(), type.getTypeName());
                    handlerProviderMap.put(type, bean.getBeanClass());
                } else  {
                    logger.warn("{} - Query has already been registered with handler", type.getTypeName());
                }
            }
        }
    }

    protected void registerBehaviors() {
        logger.info("Registering Query Behaviors");
        var beanManager = CDI.current().getBeanManager();
        var beanList = beanManager.getBeans(Object.class, new AnnotationLiteral<Any>(){});

        for (Bean<?> bean: beanList) {
            if (QueryBehavior.class.isAssignableFrom(bean.getBeanClass())) {
                Type type = null;

                for (var object: bean.getTypes().toArray()) {
                    if (object instanceof ParameterizedType) {
                        type = ((ParameterizedType)object).getActualTypeArguments()[0];
                    }
                }

                if (type == null) {
                    break;
                }

                if (!queryBehaviorMap.containsKey(type)) {
                    logger.info("{} - Query Behavior registered for query - {}", bean.getBeanClass().getSimpleName(), type.getTypeName());
                    queryBehaviorMap.put(type, List.of(bean.getBeanClass()));
                } else  {
                    logger.info("{} - Query Behavior registered for query - {}", bean.getBeanClass().getSimpleName(), type.getTypeName());
                    var behaviors = queryBehaviorMap.get(type);
                    behaviors.add(bean.getBeanClass());
                    queryBehaviorMap.replace(type, behaviors);
                }
            }
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public <R, T extends Query<R>, T2 extends QueryHandler<R, T>> T2 getQueryHandler(T t) {
        if (!handlerProviderMap.containsKey(t.getClass())) {
            return null;
        }

        return (T2) CDI.current().select(handlerProviderMap.get(t.getClass())).get();
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends Query<R>, R> List<QueryBehavior<T>> getQueryBehaviors(T t) {
        if (!queryBehaviorMap.containsKey(t.getClass())) {
            return new ArrayList<>();
        }

        return queryBehaviorMap.get(t.getClass()).stream().map(behaviorClass ->
                (QueryBehavior<T>)CDI.current().select(behaviorClass).get()).collect(Collectors.toList());
    }
}
