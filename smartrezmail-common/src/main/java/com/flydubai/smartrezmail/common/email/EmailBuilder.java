package com.flydubai.smartrezmail.common.email;

import java.util.Map;

/**
 * 
 * @author Amit.Lulla
 *
 */
public interface EmailBuilder{

	EmailBuilder from(String address);

	EmailBuilder to(String... addresses);

	EmailBuilder cc(String... addresses);

	EmailBuilder bcc(String... addresses);

	EmailBuilder withSubject(String subject);

	EmailBuilder withBody(String body);
	
	EmailBuilder withAttachment(String... attachments);
	
	EmailBuilder withTemplate(String templateName, Map<String, Object> templateArgsMap);
	
	EmailBuilder asHtml();

	EmailMessage send();
	
	EmailBuilder withInlineAttachment(String templateName, Map<String, String> cidAttNameMap);
	
	EmailBuilder withEmailConfigGroup(String emailConfigGroup);
	
}