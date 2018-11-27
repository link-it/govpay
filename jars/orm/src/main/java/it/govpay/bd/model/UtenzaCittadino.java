package it.govpay.bd.model;

import java.util.ArrayList;

public class UtenzaCittadino extends Utenza {

	public UtenzaCittadino(String codIdentificativo) {
		super();
		this.setCodIdentificativo(codIdentificativo); 
		this.setIdDomini(new ArrayList<>());
		this.setIdTributi(new ArrayList<>());
		this.setDomini(new ArrayList<>());
		this.setTributi(new ArrayList<>());
	}
	
	private static final long serialVersionUID = 1L;
	
	private transient String codIdentificativo;

	public String getCodIdentificativo() {
		return codIdentificativo;
	}

	public void setCodIdentificativo(String codIdentificativo) {
		this.codIdentificativo = codIdentificativo;
	}
	
	@Override
	public TIPO_UTENZA getTipoUtenza() {
		return TIPO_UTENZA.CITTADINO;
	}

	@Override
	public String getIdentificativo() {
		return this.getCodIdentificativo();
	}

}
