package com.flydubai.smartrezmail.server.transport;

import java.util.Calendar;
import java.util.Map;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.flydubai.smartrezmail.common.email.CommonConstants;
import com.flydubai.smartrezmail.common.email.Email;
import com.flydubai.smartrezmail.server.util.Constants;
import com.flydubai.util.configurator.data.FZConfiguration;

/**
 * This class performs Fetching the SMTP Session and sending the mail using Config properties.
 * @author Amit.Lulla
 */
@Service
public class PostalService {

	private static final Logger LOGGER = LoggerFactory.getLogger(PostalService.class);
	
	public void send(Email email, FZConfiguration config) throws AddressException, MessagingException {
		Message message = createMessage(email, config);
		send(message, email, config);
	}

	protected Session getSession(Email email, FZConfiguration config) {
		String emailConfigGroup = email.getEmailConfigGroup();
		if (emailConfigGroup == null || emailConfigGroup.isEmpty()) {
			emailConfigGroup = CommonConstants.DEFAULT_EMAIL_CONFIG_GROUP;
		}
		Session session = EmailSession.getEmailSessionMap().get(emailConfigGroup);
		if (session == null) {
			Properties properties = new Properties();
			properties.put(Constants.MAIL_SMTP_HOST, config.getString(CommonConstants.NAME_EMAIL_HOST));
			properties.put(Constants.MAIL_SMTP_AUTH, config.getString(CommonConstants.NAME_EMAIL_AUTH_REQUIRED));
			properties.put(Constants.MAIL_SMTP_PORT, config.getString(CommonConstants.NAME_EMAIL_PORT));
			properties.put(Constants.MAIL_SMTP_STARTTLS, config.getString(CommonConstants.NAME_EMAIL_STARTTLS_ENABLE));
			properties.put(Constants.MAIL_SMTP_SEND_PARTIAL, "true");
			session = Session.getInstance(properties);
			LOGGER.info("New session is created and updated in the session map");
			EmailSession.getEmailSessionMap().put(emailConfigGroup, session);
			
		}
		return session;
	}
	
	/**
	 * Build the Email Message using the given details.
	 * 
	 * @param email
	 * @return
	 * @throws MessagingException
	 */
	protected Message createMessage(Email email, FZConfiguration config) throws MessagingException {
		String emailBody = email.getBody();
		Multipart multipart = new MimeMultipart();
		MimeBodyPart mimeText = new MimeBodyPart();
		multipart.addBodyPart(mimeText);
		
		Message message = new MimeMessage(getSession(email, config));
		message.setFrom(new InternetAddress(email.getFromAddress()));

		for (String to : email.getToAddresses()) {
			message.addRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
		}

		for (String cc : email.getCcAddresses()) {
			message.addRecipients(Message.RecipientType.CC, InternetAddress
					.parse(cc));
		}

		for (String bcc : email.getBccAddresses()) {
			message.addRecipients(Message.RecipientType.BCC, InternetAddress
					.parse(bcc));
		}

		for (String attachment : email.getAttachments()) {
			MimeBodyPart mimeAttachment = new MimeBodyPart();
			FileDataSource fds = new FileDataSource(attachment);
			mimeAttachment.setDataHandler(new DataHandler(fds));
			mimeAttachment.setFileName(fds.getName());
			multipart.addBodyPart(mimeAttachment);
		}
		
		if (email.getCidAttNameMap() != null
				&& !email.getCidAttNameMap().isEmpty()) {
			Map<String, String> cidFileNameMap = email.getCidAttNameMap();
			String pathConfigName = email.getInlineAttConfigName()+Constants.SUFFIX_FILE_PATH;
			String path = config.getString(pathConfigName, "/");
			if (path.endsWith("/")) {
				path = path + "/";
			}
			for (String cidHolder : cidFileNameMap.keySet()) {
				String cid = cidHolder + System.currentTimeMillis();
				String fileName = cidFileNameMap.get(cidHolder);
				String filePath = path + fileName;
				MimeBodyPart inLineAttachment = new MimeBodyPart();
				FileDataSource fds = new FileDataSource(filePath);
				inLineAttachment.setDataHandler(new DataHandler(fds));
				inLineAttachment.setFileName(fds.getName());
				inLineAttachment.setContentID("<" + cid + ">");
				inLineAttachment.setDisposition(MimeBodyPart.INLINE);
				multipart.addBodyPart(inLineAttachment);
				emailBody = emailBody.replace(cidHolder, cid);
			}
		}
		
		mimeText.setContent(emailBody,email.isAsHtml() ? Constants.HTML_TEXT_TYPE : Constants.PLAIN_TEXT_TYPE);
		message.setContent(multipart);
		message.setSubject(email.getSubject());
		message.setHeader(Constants.HEADER_NAME, Constants.HEADER_VALUE);
		message.setSentDate(Calendar.getInstance().getTime());
		message.setHeader(Constants.HEADER_NAME_ENCODE, Constants.HEADER_VALUE_ENCODE);
		return message;
	}
	
	/**
	 * Performs the Message Transport using Session
	 * @param message
	 * @throws NoSuchProviderException
	 * @throws MessagingException
	 */
	protected void send(Message message, Email email, FZConfiguration config) throws NoSuchProviderException,
			MessagingException {
		
		Session emailSession = getSession(email, config);
		Transport smtpTransport =  emailSession.getTransport(config.getString(CommonConstants.NAME_EMAIL_PROTOCOL));
		if (config.getBoolean(CommonConstants.NAME_EMAIL_AUTH_REQUIRED)) {
			final String host = config.getString(CommonConstants.NAME_EMAIL_HOST);
			final String user = config.getString(CommonConstants.NAME_EMAIL_USERNAME);
			final String psd = config.getString(CommonConstants.NAME_EMAIL_PASSWORD);
			final boolean overrideFrom = config.getBoolean(Constants.NAME_OVERRIDE_FROM_ADDRESS, false);
			if(!overrideFrom || message.getFrom() == null || message.getFrom().length == 0) {
				final String fromAdd = config.getString(CommonConstants.NAME_EMAIL_FROM_ADDRESS);
				InternetAddress from = new InternetAddress(fromAdd);
				message.setFrom(from);
			}
			smtpTransport.connect(host, user, psd);
		} else {
			smtpTransport.connect();
		}
		smtpTransport.sendMessage(message, message.getAllRecipients());
		smtpTransport.close();
	}

}
