package it.govpay.bd.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UtenzaCittadino extends Utenza {

	public UtenzaCittadino(String codIdentificativo) {
		this(codIdentificativo, new HashMap<>());
	}
	
	public UtenzaCittadino(String codIdentificativo,Map<String, List<String>> headers) {
		super();
		this.setCodIdentificativo(codIdentificativo); 
		this.setIdDominiUo(new ArrayList<>());
		this.setIdTipiVersamento(new ArrayList<>());
		this.setDominiUo(new ArrayList<>());
		this.setTipiVersamento(new ArrayList<>());
		this.autorizzazioneDominiStar = true ;
		this.autorizzazioneTipiVersamentoStar = true;
		this.headers = headers;
		this.abilitato = true;
		this.ruoli = new ArrayList<>();
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
	
	public String getProprieta(String nome) {
		if(headers.containsKey(nome))
			if(!headers.get(nome).isEmpty())
				return headers.get(nome).get(0);
		
		return null;
	}
}
