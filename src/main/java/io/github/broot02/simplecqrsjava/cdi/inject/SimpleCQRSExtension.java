package io.github.broot02.simplecqrsjava.cdi.inject;

import jakarta.enterprise.event.Observes;
import jakarta.enterprise.inject.spi.AnnotatedType;
import jakarta.enterprise.inject.spi.BeanManager;
import jakarta.enterprise.inject.spi.BeforeBeanDiscovery;
import jakarta.enterprise.inject.spi.Extension;

public class SimpleCQRSExtension implements Extension {

    public SimpleCQRSExtension() {
    }

    protected void beforeBeanDiscovery(@Observes BeforeBeanDiscovery event, BeanManager beanManager) {
        AnnotatedType<SimpleCQRSProducer> cqrsBean = beanManager.createAnnotatedType(SimpleCQRSProducer.class);
        event.addAnnotatedType(cqrsBean, SimpleCQRSProducer.class.getName());
    }






}
