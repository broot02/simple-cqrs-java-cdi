package io.github.broot02.simplecqrsjava.cdi.commands;

import io.github.broot02.simplecqrsjava.core.commands.Command;
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
import java.util.HashMap;
import java.util.Map;

@ApplicationScoped
public class CDICommandRegistry implements CommandRegistry {

    private final Map<Type, Class<?>> handlerProviderMap = new HashMap<>();
    private final Logger logger;

    public CDICommandRegistry() {
        this.logger = LoggerFactory.getLogger(this.getClass());
        registerHandlers();
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


    @Override
    @SuppressWarnings("unchecked")
    public <R, T extends Command<R>, T2 extends CommandHandler<T, R>> T2 getCommandHandler(T t) {
        if (!handlerProviderMap.containsKey(t.getClass())) {
            return null;
        }

        return (T2)CDI.current().select(handlerProviderMap.get(t.getClass())).get();
    }
}
