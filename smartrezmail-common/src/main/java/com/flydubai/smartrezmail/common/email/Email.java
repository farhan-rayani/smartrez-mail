package com.flydubai.smartrezmail.common.email;

import java.util.Map;
import java.util.Set;

/**
 * 
 * @author Amit.Lulla
 *
 */
public interface Email {

	String getFromAddress();
	
	Set<String> getToAddresses();
	
	Set<String> getCcAddresses();
	
	Set<String> getBccAddresses();
	
	Set<String> getAttachments();
	
	String getTemplate();
	
	String getSubject();
	
	String getBody();
	
	boolean isAsHtml();

	
	Map<String, String> getCidAttNameMap();
	
	String getInlineAttConfigName();
	
	String getEmailConfigGroup();
	
}