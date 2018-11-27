package it.govpay.core.dao.commons.exception;

import java.net.URI;
import java.net.URISyntaxException;

public class RedirectException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String location;
	
	public RedirectException(String location) {
		super();
		this.location = location;
	}
	
	public RedirectException(String location, String message) {
		super(message);
		this.location = location;
	}
	
	public RedirectException(String location,Throwable t) {
		super(t);
		this.location = location;
	}
	
	public RedirectException(String location, String message ,Throwable t) {
		super(message,t);
		this.location = location;
	}
	
	public String getLocation() {
		return this.location;
	}
	
	public URI getURILocation() {
		try {
			return new URI(this.location);
		} catch (URISyntaxException e) {
			return null;
		}
	}
}
