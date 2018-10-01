package it.govpay.core.dao.pagamenti.dto;

public class RichiestaWebControllerDTOResponse {

	String wispHtml = null;
	String location = null;
	
	public String getWispHtml() {
		return this.wispHtml;
	}
	public void setWispHtml(String wispHtml) {
		this.wispHtml = wispHtml;
	}
	public String getLocation() {
		return this.location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
}
