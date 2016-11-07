package com.flydubai.smartrezmail.server.transport;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import javax.mail.Session;

public class EmailSession {

	public EmailSession() {
		// TODO Auto-generated constructor stub
	}
	
	private static ConcurrentMap<String, Session> emailSessionMap = new ConcurrentHashMap<String, Session>();

	/**
	 * @return the emailSessionMap
	 */
	public static ConcurrentMap<String, Session> getEmailSessionMap() {
		return emailSessionMap;
	}

	/**
	 * @param emailSessionMap the emailSessionMap to set
	 */
	public static void setEmailSessionMap(
			ConcurrentMap<String, Session> emailSessionMap) {
		EmailSession.emailSessionMap = emailSessionMap;
	}

}
