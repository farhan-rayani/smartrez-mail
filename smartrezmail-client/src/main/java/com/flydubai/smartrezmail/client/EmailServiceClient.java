package com.flydubai.smartrezmail.client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.flydubai.smartrezmail.common.email.CommonConstants;
import com.flydubai.smartrezmail.common.email.EmailMessage;
import com.flydubai.util.configurator.client.ConfiguratorServiceClient;
import com.flydubai.util.configurator.data.ConfigurationContext;
import com.flydubai.util.configurator.data.FZConfiguration;
import com.flydubai.util.configurator.exception.ConfiguratorException;

/**
 * 
 * @author Amit.Lulla
 * Since 1.0 28 Jan 2015
 * 
 * This is the REST Client to Send Mail Exposed as a Web Service
 *
 */
public class EmailServiceClient {

	private static final Logger logger = LoggerFactory
			.getLogger(EmailServiceClient.class);

	private String emailServiceUrl = null;

	private static EmailServiceClient emailServiceClient = null;

	EmailServiceClient() {
		//String url = getUrl();
		//this.emailServiceUrl = url;
	}
	
	EmailServiceClient(String serverUrl) {
		this.emailServiceUrl = serverUrl;
	}

	private String getUrl() {

		FZConfiguration config = getConfigurations();
		String url = config.getString(CommonConstants.NAME_SERVER_URL);
		return url;
	}

	public static EmailServiceClient getInstance() {
		if (emailServiceClient == null) {
			emailServiceClient = new EmailServiceClient();
		}
		return emailServiceClient;
	}
	
	public static EmailServiceClient getInstance(String serverUrl) {
		if (emailServiceClient == null) {
			emailServiceClient = new EmailServiceClient(serverUrl);
		}
		return emailServiceClient;
	}
	
	public Response sendMail(EmailMessage emailMessage) {
		String url = null;
		if(emailServiceUrl == null){
			url = getUrl();
		} else {
			url = this.emailServiceUrl;
		}
		logger.debug("sendMail:ENTRY:{}", url);
		logger.debug("Send Mail :: {}", emailMessage);
		WebTarget target = ClientFactory.create(url).path(CommonConstants.PATH_SEND_MAIL);
		Response response = target.request().post(Entity.json(emailMessage));
		logger.info("Response :: {}", response);
		logger.debug("URI = {}", target.getUri());
		logger.debug("sendMail:EXIT:{} ", emailMessage);
		return response;
	}
	
	
	/**
	 * This method is to take all the configurations required for email
	 * service client
	 * 
	 * If more configurations is required, then add the configuration parameter
	 * name to the variable "List<String> parameterNames"
	 * 
	 * @return FZConfiguration
	 */
	private FZConfiguration getConfigurations() {
		FZConfiguration fzConfiguration = null;
		List<String> parameterNames = new ArrayList<String>();
		parameterNames
				.add(CommonConstants.NAME_SERVER_URL);
		try {
			ConfiguratorServiceClient client = ConfiguratorServiceClient
					.getInstance();
			ConfigurationContext context = new ConfigurationContext.Builder(CommonConstants.DEFAULT_EMAIL_CONFIG_GROUP)
					.addParameterNames(parameterNames).build();
			fzConfiguration = client.getConfiguration(context);
		} catch (ConfiguratorException e) {
			logger.error(
					"Exception while retrieving configurations for Flight data service ",
					e);
		}
		return fzConfiguration;
	}
	
	/**
	 * Test Class to test the Mail Service using client -- over Http POST.
	 * @param args
	 */
	public static void main(String[] args){
		EmailServiceClient client = EmailServiceClient.getInstance();
		Map<String, Object> templateArgsMap = new HashMap<String, Object>();
		templateArgsMap.put("var1", "Test-Application");
		//templateArgsMap.put("var2", "C:\\Users\\amit.lulla\\Desktop\\pnl.log");
		client.sendMail(new EmailMessage()
		.to("gokulanathan.s@flydubai.com")
		//.to("vknayanar@gmail.com")
		.from("testMails@flydubai.com")
		.withSubject("Custom Mail - Test")
		.withEmailConfigGroup("EMAIL-SERVICE")
		.withBody("Hello! Prod")
		//.withTemplate("ALERT_TEMPLATE", templateArgsMap)
		.asHtml()
		.send());
	}
	

}
