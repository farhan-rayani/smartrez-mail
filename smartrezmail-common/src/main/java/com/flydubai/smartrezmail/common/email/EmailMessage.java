package com.flydubai.smartrezmail.common.email;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * 
 * @author Amit.Lulla
 *
 */
public class EmailMessage implements EmailBuilder, Email {

	private String fromAddress;
	private Set<String> toAddresses = new HashSet<String>();
	private Set<String> ccAddresses = new HashSet<String>();
	private Set<String> bccAddresses = new HashSet<String>();
	private Set<String> attachments = new HashSet<String>();
	private String template;
	private String subject;
	private String body;
	private boolean asHtml;
	private Map<String, Object> templateArgsMap;
	private String emailConfigGroup;
	
	private Map<String, String> cidAttNameMap ;
	private String inlineAttConfigName;
	//private Map<String, String> cidMap ;

	public EmailMessage send() {
		//validateRequiredInfo();
		//validateAddresses();
		//sendMessage();
		return this;
	}
	
	public EmailBuilder from(String address) {
		this.fromAddress = address;
		return this;
	}

	public EmailBuilder to(String... addresses) {
		for (int i = 0; i < addresses.length; i++) {
			this.toAddresses.add(addresses[i]);
		}
		return this;
	}

	public EmailBuilder cc(String... addresses) {
		for (int i = 0; i < addresses.length; i++) {
			this.ccAddresses.add(addresses[i]);
		}
		return this;
	}

	public EmailBuilder bcc(String... addresses) {
		for (int i = 0; i < addresses.length; i++) {
			this.bccAddresses.add(addresses[i]);
		}
		return this;
	}

	public EmailBuilder withSubject(String subject) {
		this.subject = subject;
		return this;
	}

	public EmailBuilder withBody(String body) {
		this.body = body;
		return this;
	}
	
	public EmailBuilder asHtml() {
		this.asHtml = true;
		return this;
	}
	
	public EmailBuilder withAttachment(String... attachments) {
 		for (int i = 0; i < attachments.length; i++) {
			this.attachments.add(attachments[i]);
		}
		return this;
	}
	
	public EmailBuilder withTemplate(String templateName, Map<String, Object> templateArgsMap) {
		this.template = templateName.toUpperCase();
		this.templateArgsMap = templateArgsMap;
		return this;
	}
	
	/**
	 * This is for INLINE attachment For embedding images with the email.
	 * Template name will be the configurator prefix for FILE_PATH 
	 * eg: templateName = "OLCI_PRE_DEP_EMAIL"
	 * then configurator NAME = OLCI_PRE_DEP_EMAIL_FILE_PATH for the path
	 *  ContentID placeholder list is provided here
	 */
	@Override
	public EmailBuilder withInlineAttachment(String inlineAttConfigName, Map<String, String> cidAttNameMap) {
		this.inlineAttConfigName = inlineAttConfigName.toUpperCase();
		this.cidAttNameMap = cidAttNameMap;
		return this;
	}
	
	public void setBody(String body) {
		this.body = body;
	}

	public String getFromAddress() {
		return fromAddress;
	}
	
	public void setFromAddress(String fromAddress) {
		this.fromAddress = fromAddress;
	}

	public Set<String> getToAddresses() {
		return toAddresses;
	}
	
	public void setToAddresses(Set<String> toAddresses) {
		this.toAddresses = toAddresses;
	}

	public Set<String> getCcAddresses() {
		return ccAddresses;
	}
	public void setCcAddresses(Set<String> ccAddresses) {
		this.ccAddresses = ccAddresses;
	}

	public Set<String> getBccAddresses() {
		return bccAddresses;
	}
	
	public void setBccAddresses(Set<String> bccAddresses) {
		this.bccAddresses = bccAddresses;
	}
	
	public Set<String> getAttachments() {
		return attachments;
	}
	public void setAttachments(Set<String> attachments) {
		this.attachments = attachments;
	}
	
	public String getTemplate() {
		return template;
	}
	
	public void setTemplate(String template) {
		this.template = template;
	}
	
	public Map<String, Object> getTemplateArgsMap() {
		return templateArgsMap;
	}
	
	public void setTemplateArgsMap(Map<String, Object> templateArgsMap) {
		this.templateArgsMap = templateArgsMap;
	}

	public String getSubject() {
		return subject;
	}
	
	public void setSubject(String subject) {
		this.subject = subject ;
	}

	public String getBody() {
		return body;
	}

	public boolean isAsHtml() {
		return asHtml;
	}
	
	public void setAsHtml(boolean asHtml) {
		this.asHtml = asHtml;
	}
	
	/**
	 * @return the cidAttNameMap
	 */
	public Map<String, String> getCidAttNameMap() {
		return cidAttNameMap;
	}

	/**
	 * @param cidAttNameMap the cidAttNameMap to set
	 */
	public void setCidAttNameMap(Map<String, String> cidAttNameMap) {
		this.cidAttNameMap = cidAttNameMap;
	}

	/**
	 * @return the cidMap
	 *//*
	public Map<String, String> getCidMap() {
		return cidMap;
	}

	*//**
	 * @param cidMap the cidMap to set
	 *//*
	public void setCidMap(Map<String, String> cidMap) {
		this.cidMap = cidMap;
	}*/


	@Override
	public String toString(){
		StringBuilder builder = new StringBuilder();
		builder.append("EmailMessage [fromAddress=").append(fromAddress).append(", toAddresses=")
				.append(toAddresses).append(", ccAddresses=").append(ccAddresses)
				.append(", bccAddresses=").append(bccAddresses)
				.append(", attachments=").append(attachments)
				.append(", subject=").append(subject)
				//.append(", body=").append(body)
				.append(", template=").append(template)
				.append(", templateArgsMap=").append(templateArgsMap)
				.append(", asHtml=").append(asHtml)
				.append(", emailConfigGroup=").append(emailConfigGroup)
				.append("]");
		return builder.toString();
	}

	@Override
	public String getInlineAttConfigName() {
		
		return this.inlineAttConfigName;
	}
	
	public void setInlineAttConfigName(String inlineAttConfigName){
		this.inlineAttConfigName = inlineAttConfigName;
	}

	/**
	 * @return the emailConfigGroup
	 */
	public String getEmailConfigGroup() {
		return emailConfigGroup;
	}

	/**
	 * @param emailConfigGroup the emailConfigGroup to set
	 */
	public void setEmailConfigGroup(String emailConfigGroup) {
		this.emailConfigGroup = emailConfigGroup;
	}

	@Override
	public EmailBuilder withEmailConfigGroup(String emailConfigGroup) {
		this.emailConfigGroup = emailConfigGroup;
		return this;
	}

	/*protected EmailBuilder validateAddresses() {
		if (!emailAddressValidator.validate(fromAddress)) {
			throw new InvalidEmailAddressException("From: " + fromAddress);
		}

		for (String email : toAddresses) {
			if (!emailAddressValidator.validate(email)) {
				throw new InvalidEmailAddressException("To: " + email);
			}
		}

		return this;
	}

	*/
}