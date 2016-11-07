package com.flydubai.smartrezmail.server.service;

import java.io.StringWriter;
import java.util.Map;

import javax.mail.SendFailedException;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.flydubai.smartrezmail.common.email.CommonConstants;
import com.flydubai.smartrezmail.common.email.EmailBuilder;
import com.flydubai.smartrezmail.common.email.EmailMessage;
import com.flydubai.smartrezmail.common.email.EmailService;
import com.flydubai.smartrezmail.server.exception.EmailBodyTemplateException;
import com.flydubai.smartrezmail.server.exception.EmailTransportException;
import com.flydubai.smartrezmail.server.exception.IncompleteEmailException;
import com.flydubai.smartrezmail.server.exception.InvalidEmailAddressException;
import com.flydubai.smartrezmail.server.transport.EmailSession;
import com.flydubai.smartrezmail.server.transport.PostalService;
import com.flydubai.smartrezmail.server.util.Constants;
import com.flydubai.smartrezmail.server.validation.EmailAddressValidator;
import com.flydubai.util.configurator.client.ConfiguratorServiceClient;
import com.flydubai.util.configurator.data.ConfigurationContext;
import com.flydubai.util.configurator.data.FZConfiguration;
import com.flydubai.util.configurator.exception.ConfiguratorException;

/**
 * 
 * @author Amit.Lulla
 * 
 * Email Service Implementation Class.
 * Validates the Email message and sends mail using the Postal Service (Javax.mail)
 *
 */
@Service
public class EmailServiceImpl implements EmailService{
	
	private static final Logger LOGGER = LoggerFactory.getLogger(EmailServiceImpl.class);
	
	@Autowired
	private PostalService postalService;

	@Override
	public void sendMail(EmailMessage emailMessage) {
		LOGGER.info("Started Processing Email :: fromAddress:{}||toAddresses:{}||ccAddresses:{}||bccAddresses:{}||attachments:{}||subject:{}||template{}",
				emailMessage.getFromAddress(), emailMessage.getToAddresses(),
				emailMessage.getCcAddresses(), emailMessage.getBccAddresses(),
				emailMessage.getAttachments(), emailMessage.getSubject(),
				emailMessage.getTemplate());
		//this.emailMessage = emailMessage;
		FZConfiguration config = getEmailConfig(emailMessage.getEmailConfigGroup());
		validateRequiredInfo(emailMessage);
		validateAddresses(emailMessage);
		populateBody(emailMessage, config);
		sendMessage(emailMessage, config);
		LOGGER.info("Finished Processing Email toAddresses:{} subject:{} ", emailMessage.getToAddresses(), emailMessage.getSubject());
	}
	
	protected void validateRequiredInfo(EmailMessage emailMessage) throws IncompleteEmailException{
		if (emailMessage.getFromAddress() == null) {
			LOGGER.error("From address cannot be null");
			throw new IncompleteEmailException("From address cannot be null");
		}
		if (emailMessage.getToAddresses().isEmpty()) {
			LOGGER.error("Email should have at least one to address");
			throw new IncompleteEmailException(
					"Email should have at least one to address");
		}
		if (emailMessage.getSubject() == null) {
			LOGGER.error("Subject cannot be null");
			throw new IncompleteEmailException("Subject cannot be null");
		}
		if (emailMessage.getBody() == null) {
			if(emailMessage.getTemplate() == null){
				LOGGER.error("Body cannot be null");
				throw new IncompleteEmailException("Body cannot be null");
			}
		}
	}

	/**
	 * Main method which sends out the email message
	 */
	protected void sendMessage(EmailMessage emailMessage, FZConfiguration config) throws EmailTransportException{
		try {
			postalService.send(emailMessage, config);
		} catch(SendFailedException se) {
			LOGGER.error("Email sent failed Invalid:{}||ValidUnsent:{}||ValidSent:{}",
					se.getInvalidAddresses(), se.getValidUnsentAddresses(),
					se.getValidSentAddresses());
			throw new EmailTransportException("Email could not be sent: "
					+ se.getMessage(), se);
		} catch (Exception e) {
			throw new EmailTransportException("Email could not be sent: "
					+ e.getMessage(), e);
		}
	}
	
	/**
	 * Email address validation
	 * @return
	 */
	protected EmailBuilder validateAddresses(EmailMessage emailMessage) throws InvalidEmailAddressException{
		EmailAddressValidator emailAddressValidator = new EmailAddressValidator();
		if (!emailAddressValidator.validate(emailMessage.getFromAddress())) {
			LOGGER.error("From: {}", emailMessage.getFromAddress());
			throw new InvalidEmailAddressException("From: " + emailMessage.getFromAddress());
		}

		for (String email : emailMessage.getToAddresses()) {
			if (!emailAddressValidator.validate(email)) {
				LOGGER.error("To: {}", email);
				throw new InvalidEmailAddressException("To: " + email);
			}
		}

		return emailMessage;
	}
	
	/**
	 * Prepare the Body.
	 * If body is null, populate it from Template
	 */
	private void populateBody(EmailMessage emailMessage, FZConfiguration config) {
		if(emailMessage.getBody() == null){
			populateBodyFromTemplate(emailMessage, config);
		}
	}
	
	/**
	 * Load template from configurator and set Body of Email using template
	 * @throws EmailBodyTemplateException
	 */
	private void populateBodyFromTemplate(EmailMessage emailMessage, FZConfiguration config) throws EmailBodyTemplateException {
		String templateName = emailMessage.getTemplate();
		Map<String, Object> templateArgsMap = emailMessage.getTemplateArgsMap();
		String templateLocation = config.getString(templateName+Constants.SUFFIX_FILE_PATH);
		String templateFileName = config.getString(templateName+Constants.SUFFIX_FILE_NAME);
		
		//Load Template for Email
		VelocityEngine ve = new VelocityEngine();
		ve.setProperty(RuntimeConstants.RESOURCE_LOADER, Constants.RESOURCE_LOADER_TYPE_FILE);
		ve.setProperty(RuntimeConstants.FILE_RESOURCE_LOADER_PATH, templateLocation);
		ve.init();
		
		//Initialize Velocity Context with Map of Arguments to the template
		VelocityContext context = new VelocityContext(templateArgsMap);
		Template template = ve.getTemplate(templateFileName, "UTF-8");
		StringWriter writer = new StringWriter();
        template.merge(context, writer);
		String emailBody = writer.toString();

		if(emailBody == null){
			LOGGER.error("Failed to populate template as Email body");
			throw new IncompleteEmailException("Failed to populate template as Email body");
		}
		emailMessage.setBody(emailBody);
	}

	@Override
	public void refreshSession() {
		EmailSession.getEmailSessionMap().clear();
	}
	
	private FZConfiguration getEmailConfig(String emailConfiGroup) {
		FZConfiguration emailConfig = null;
		if (emailConfiGroup == null || emailConfiGroup.trim().isEmpty()) {
			emailConfiGroup = CommonConstants.DEFAULT_EMAIL_CONFIG_GROUP;
		}
		ConfiguratorServiceClient client = ConfiguratorServiceClient
				.getInstance();
		ConfigurationContext ctx = new ConfigurationContext.Builder(
				emailConfiGroup).build();
		try {
			emailConfig = client.getConfiguration(ctx);
		} catch (ConfiguratorException e) {
			LOGGER.error("Configuration fetch exception config group:{}", emailConfiGroup, e);
		}

		return emailConfig;
	}

}
