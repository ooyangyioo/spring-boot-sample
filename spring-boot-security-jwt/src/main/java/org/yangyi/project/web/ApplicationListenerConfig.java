package org.yangyi.project.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class ApplicationListenerConfig {

    private static final Logger logger = LoggerFactory.getLogger(ApplicationListenerConfig.class);

    @Component
    public static class ApplicationStartedListener implements ApplicationListener<ApplicationStartedEvent> {
        @Override
        public void onApplicationEvent(ApplicationStartedEvent event) {
        }
    }

    @Component
    public static class ApplicationReadyListener implements ApplicationListener<ApplicationReadyEvent> {
        @Override
        public void onApplicationEvent(ApplicationReadyEvent event) {
        }
    }
}
