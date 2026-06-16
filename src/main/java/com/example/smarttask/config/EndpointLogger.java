package com.example.smarttask.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.event.EventListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

@Component
public class EndpointLogger {

    private static final Logger logger = LoggerFactory.getLogger(EndpointLogger.class);

    @EventListener
    public void handleContextRefresh(ContextRefreshedEvent event) {
        ApplicationContext context = event.getApplicationContext();
        RequestMappingHandlerMapping mapping = context.getBean(RequestMappingHandlerMapping.class);
        logger.info("================ REGISTERED ENDPOINTS ================");
        mapping.getHandlerMethods().forEach((key, value) -> {
            logger.info("Endpoint: {} -> Method: {}", key, value);
        });
        logger.info("======================================================");
    }
}
