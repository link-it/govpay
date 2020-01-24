package it.govpay.bd.model;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.commons.lang.StringUtils;

public class UtenzaAnonima extends Utenza {

	public static final String ID_UTENZA_ANONIMA = "UTENZA_ANONIMA"; 
	
	public UtenzaAnonima() {
		super();
		this.setPrincipal(null); 
		this.setIdDominiUo(new ArrayList<>());
		this.setIdTipiVersamento(new ArrayList<>());
		this.setDominiUo(new ArrayList<>());
		this.setTipiVersamento(new ArrayList<>());
		this.headers = new HashMap<>();
		this.autorizzazioneDominiStar = true;
		this.autorizzazioneTipiVersamentoStar = true;
		this.abilitato = true;
		this.ruoli = new ArrayList<>();
	}
	
	private static final long serialVersionUID = 1L;
	
	@Override
	public TIPO_UTENZA getTipoUtenza() {
		return TIPO_UTENZA.ANONIMO;
	}

	@Override
	public String getIdentificativo() {
		return StringUtils.isEmpty(this.getPrincipal()) ?  ID_UTENZA_ANONIMA : this.getPrincipal();
	}
}
