package com.flydubai.smartrezmail.server.restful;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

import org.glassfish.jersey.server.ServerProperties;


@ApplicationPath("/*")
public class SmartrezMailApplication extends Application{
	 @Override
	    public Set<Class<?>> getClasses() {
	        final Set<Class<?>> classes = new HashSet<Class<?>>();
	        classes.add(EmailRestService.class);
	        classes.add(SmartrezMailEventListener.class);
	        return classes;
	    }
	    
	    @Override
	    public Map<String, Object> getProperties() {
	        Map<String, Object> properties = new HashMap<String, Object>();
	        properties.put(ServerProperties.MONITORING_STATISTICS_MBEANS_ENABLED, true);
	        return properties;
	    }
}
