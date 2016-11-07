package com.flydubai.smartrezmail.server.restful;

import org.glassfish.jersey.server.monitoring.RequestEvent;
import org.glassfish.jersey.server.monitoring.RequestEventListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SmartrezMailRequestEventListener implements RequestEventListener{
	
	private volatile long methodStartTime;
	private static final Logger logger = LoggerFactory.getLogger(SmartrezMailRequestEventListener.class);
    @Override
    public void onEvent(RequestEvent requestEvent) {
        switch (requestEvent.getType()) {
            case RESOURCE_METHOD_START:
                methodStartTime = System.currentTimeMillis();
                break;
            case RESOURCE_METHOD_FINISHED:
                long methodExecution = System.currentTimeMillis() - methodStartTime;
                final String methodName = requestEvent.getUriInfo().getMatchedResourceMethod().getInvocable().getHandlingMethod().getName();
                logger.info("Method '" + methodName + "' executed. Processing time: " + methodExecution + " ms");
                break;
        }
    }
}

