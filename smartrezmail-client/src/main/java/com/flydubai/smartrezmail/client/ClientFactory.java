package com.flydubai.smartrezmail.client;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;

import org.glassfish.jersey.client.ClientConfig;

import com.flydubai.restclient.ClientRequest;
import com.flydubai.restclient.LoadBalancedClient;

public final class ClientFactory {

	public static WebTarget create(String url){
		
		Client client = ClientBuilder.newClient(new ClientConfig());
		ClientRequest clientRequest = new ClientRequest.Builder("MAIL-CLIENT").
				 withUri(url).
				 build();
		WebTarget target = client.target(LoadBalancedClient.getInstance().getURI(clientRequest));
		return target;
	}
	
}
