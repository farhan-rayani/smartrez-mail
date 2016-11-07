package com.flydubai.smartrezmail.server.restful;

import org.glassfish.jersey.server.monitoring.ApplicationEvent;
import org.glassfish.jersey.server.monitoring.ApplicationEventListener;
import org.glassfish.jersey.server.monitoring.RequestEvent;
import org.glassfish.jersey.server.monitoring.RequestEventListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class SmartrezMailEventListener implements ApplicationEventListener{
	
	private static final Logger logger = LoggerFactory.getLogger(SmartrezMailEventListener.class);
	@Override
    public void onEvent(ApplicationEvent applicationEvent) {
        switch (applicationEvent.getType()) {
            case INITIALIZATION_FINISHED:
                logger.info("SmartrezMail server application started.");
                break;
        	}
        } 
        
        @Override
        public RequestEventListener onRequest(RequestEvent requestEvent) {
            return new SmartrezMailRequestEventListener();
        }
}
