package it.govpay.bd.model;

import java.util.List;
import java.util.Map;

public class UtenzaOperatore extends Utenza {

	private static final long serialVersionUID = 1L;
	
	private transient String nome;
	private transient Map<String, List<String>> headers;
	
	public UtenzaOperatore() {
		super();
	}
	
	public UtenzaOperatore(Utenza utenzaBase, String nome, Map<String, List<String>> headers) {
		// dati bd.utenza
		this.aclPrincipal = utenzaBase.aclPrincipal;
		this.aclRuoli = utenzaBase.aclRuoli;
		this.domini = utenzaBase.domini;
		this.ruoli = utenzaBase.ruoli;
		this.tipiVersamento = utenzaBase.tipiVersamento;
		// dati model
		this.id = utenzaBase.getId();
		this.principal = utenzaBase.getPrincipal();
		this.principalOriginale = utenzaBase.getPrincipalOriginale();
		this.abilitato = utenzaBase.isAbilitato();
		this.idDomini = utenzaBase.getIdDomini();
		this.idTipiVersamento = utenzaBase.getIdTipiVersamento();
		this.checkSubject = utenzaBase.isCheckSubject();
		this.autorizzazioneDominiStar = utenzaBase.isAutorizzazioneDominiStar();
		this.autorizzazioneTipiVersamentoStar = utenzaBase.isAutorizzazioneTipiVersamentoStar();
		
		this.headers = headers;
		this.nome = nome;
	}

	@Override
	public TIPO_UTENZA getTipoUtenza() {
		return TIPO_UTENZA.OPERATORE;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}
	
	public Map<String, List<String>> getHeaders() {
		return headers;
	}

	public void setHeaders(Map<String, List<String>> headers) {
		this.headers = headers;
	}
}
