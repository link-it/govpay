package it.govpay.core.dao.pagamenti.dto;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;

public class RichiestaWebControllerDTO {

	private String idSessione = null;
	private String principal = null;
	private String action = null;
	private String type = null;
	private String wispDominio = null;
	private String wispKeyPA = null;
	private String wispKeyWisp = null;


	public String getIdSessione() {
		return this.idSessione;
	}
	public void setIdSessione(String idSessione) {
		this.idSessione = idSessione;
	}
	public String getPrincipal() {
		return this.principal;
	}
	public void setPrincipal(String principal) {
		this.principal = principal;
	}
	public String getAction() {
		return this.action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	public String getType() {
		return this.type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getWispDominio() {
		return this.wispDominio;
	}
	public void setWispDominio(String wispDominio) {
		this.wispDominio = wispDominio;
	}
	public String getWispKeyPA() {
		return this.wispKeyPA;
	}
	public void setWispKeyPA(String wispKeyPA) {
		this.wispKeyPA = wispKeyPA;
	}
	public String getWispKeyWisp() {
		return this.wispKeyWisp;
	}
	public void setWispKeyWisp(String wispKeyWisp) {
		this.wispKeyWisp = wispKeyWisp;
	} 
	
//	@FormParam("keyPA")  String keyPA,
//	@FormParam("keyWISP") String keyWISP,
//	@FormParam("idDominio")String idDominio,
//	@FormParam("type") String type
	
	public void setParametriBody (List<NameValuePair> parametriBody) {
		if(parametriBody != null && parametriBody.size() > 0) {
			Map<String, String> map = new HashMap<>();
			for (NameValuePair nameValuePair : parametriBody) {
				map.put(nameValuePair.getName(), nameValuePair.getValue());
			}
			this.wispKeyPA = map.get("keyPA");
			this.wispKeyWisp = map.get("keyWISP");
			this.wispDominio = map.get("idDominio");
			this.type = map.get("type");
		}
		
		
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();

		if(this.idSessione != null)
			sb.append("ID: [").append(this.idSessione).append("]");
		if(this.action != null)
			sb.append("\nAction: [").append(this.action).append("]");
		if(this.wispDominio != null)
			sb.append("\nID Dominio: [").append(this.wispDominio).append("]");
		if(this.wispKeyPA != null)
			sb.append("\nKeyPA: [").append(this.wispKeyPA).append("]");
		if(this.wispKeyWisp != null)
			sb.append("\nKeyWISP: [").append(this.wispKeyWisp).append("]");
		if(this.type != null)
			sb.append("\nType: [").append(this.type).append("]");

		return sb.toString();
	}

}
