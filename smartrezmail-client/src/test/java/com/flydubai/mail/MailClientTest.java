package com.flydubai.mail;

import static org.junit.Assert.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.flydubai.smartrezmail.client.EmailServiceClient;
import com.flydubai.smartrezmail.common.email.EmailMessage;


public class MailClientTest {

	@Test
	public void testClient1() {
		EmailServiceClient client = EmailServiceClient.getInstance();
		Map<String, Object> templateArgsMap = new HashMap<String, Object>();
		templateArgsMap.put("var1", "Test-Application");
		//templateArgsMap.put("var2", "C:\\Users\\amit.lulla\\Desktop\\pnl.log");
		//commented 
		/*client.sendMail(new EmailMessage()
		.to("gokulanathan.s@flydubai.com")
		//.to("vknayanar@gmail.com")
		.from("testMails@flydubai.com")
		.withSubject("Custom Mail - Test")
		.withEmailConfigGroup("EMAIL-SERVICE")
		.withBody("Hello! Prod")
		//.withTemplate("ALERT_TEMPLATE", templateArgsMap)
		.asHtml()
		.send());*/
	}
	
	

}
