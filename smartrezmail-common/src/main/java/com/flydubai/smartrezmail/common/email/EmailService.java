package com.flydubai.smartrezmail.common.email;

/**
 * 
 * @author Amit.Lulla
 *
 */

public interface EmailService {
	
	void sendMail(EmailMessage emailMessage);
	
	void refreshSession();

}
