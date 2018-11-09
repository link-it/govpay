package it.govpay.bd.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class UtenzaAnonima extends Utenza {

	public static final String ID_UTENZA_ANONIMA = "UTENZA_ANONIMA"; 
	
	public UtenzaAnonima() {
		super();
		this.setPrincipal(null); 
		this.setIdDomini(new ArrayList<>());
		this.setIdTributi(new ArrayList<>());
		this.setDomini(new ArrayList<>());
		this.setTributi(new ArrayList<>());
		this.headers = new HashMap<>();
	}
	
	private static final long serialVersionUID = 1L;
	
	private transient Map<String, String> headers;

	@Override
	public TIPO_UTENZA getTipoUtenza() {
		return TIPO_UTENZA.ANONIMO;
	}

	@Override
	public String getIdentificativo() {
		return ID_UTENZA_ANONIMA;
	}

	public Map<String, String> getHeaders() {
		return headers;
	}
}
