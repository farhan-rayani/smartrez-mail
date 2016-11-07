package com.flydubai.smartrezmail.server.restful;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response.Status;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.flydubai.smartrezmail.common.email.EmailMessage;
import com.flydubai.smartrezmail.common.email.EmailService;
import com.flydubai.smartrezmail.server.exception.EmailTransportException;
import com.flydubai.smartrezmail.server.exception.IncompleteEmailException;
import com.flydubai.smartrezmail.server.exception.InvalidEmailAddressException;


/**
 * 
 * @author Amit.Lulla
 * @version 1.0
 * @since Jan 2015
 *
 * REST API for Sending Mail.
 *
 */

@Path("/mail")
public class EmailRestService {
	private static final Logger LOGGER = LoggerFactory.getLogger(EmailRestService.class);
	
	@Autowired
	private EmailService emailService;
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/send")
	public Response sendMail(EmailMessage emailMessage) {
		LOGGER.info("sendMail:ENTRY");
		ResponseBuilder responseBuilder = null;
		String entityValue = "";
		try {
			emailService.sendMail(emailMessage);
			responseBuilder = Response.status(Status.OK);
			entityValue = "Mail sent successfully";
		} catch (IncompleteEmailException ie) {
			responseBuilder = Response.status(Status.PARTIAL_CONTENT);
			entityValue = "Mail Sent failed. " + ie.getMessage();
			LOGGER.error("Mail send failed ", ie);
		} catch (InvalidEmailAddressException ieae){
			responseBuilder = Response.status(Status.PRECONDITION_FAILED);
			entityValue = "Mail Sent failed. " + ieae.getMessage();
			LOGGER.error("Mail send failed ", ieae);
		} catch (EmailTransportException ete){
			responseBuilder = Response.status(Status.EXPECTATION_FAILED);
			entityValue = "Mail Sent failed. " + ete.getMessage();
			LOGGER.error("Mail send failed ", ete);
		} catch (Exception e) {
			responseBuilder = Response.status(Status.INTERNAL_SERVER_ERROR);
			if(e.getMessage() != null) {
				entityValue = "Mail Sent failed " + e.getMessage();
			} else if (e.getLocalizedMessage() != null) {
				entityValue = "Mail Sent failed " + e.getLocalizedMessage();
			} else {
				entityValue = "Mail Sent failed. Internal server error ";
			}
			LOGGER.error("Mail send failed ", e);
		}
		LOGGER.info("sendMail:EXIT");
		responseBuilder = responseBuilder.entity(entityValue).type(MediaType.APPLICATION_JSON);
		return responseBuilder.build();
	}
	
	@POST
	@Path("/refresh")
	public Response refreshSession() {
		LOGGER.info("refresh session map:ENTRY");
		emailService.refreshSession();
		LOGGER.info("refresh session map:EXIT");
		return Response.status(200).entity("session map refreshed").build();
	}
}