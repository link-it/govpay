package it.govpay.bd.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

public class UtenzaAnonima extends Utenza {

	public static final String ID_UTENZA_ANONIMA = "UTENZA_ANONIMA"; 
	
	public UtenzaAnonima() {
		super();
		this.setPrincipal(null); 
		this.setIdDomini(new ArrayList<>());
		this.setIdTipiTributo(new ArrayList<>());
		this.setDomini(new ArrayList<>());
		this.setTipiributo(new ArrayList<>());
		this.headers = new HashMap<>();
		this.autorizzazioneDominiStar = true;
		this.autorizzazioneTributiStar = false;
	}
	
	private static final long serialVersionUID = 1L;
	
	private transient Map<String, List<String>> headers;

	@Override
	public TIPO_UTENZA getTipoUtenza() {
		return TIPO_UTENZA.ANONIMO;
	}

	@Override
	public String getIdentificativo() {
		return StringUtils.isEmpty(this.getPrincipal()) ?  ID_UTENZA_ANONIMA : this.getPrincipal();
	}

	public Map<String, List<String>> getHeaders() {
		return headers;
	}

	public void setHeaders(Map<String, List<String>> headers) {
		this.headers = headers;
	}
}
