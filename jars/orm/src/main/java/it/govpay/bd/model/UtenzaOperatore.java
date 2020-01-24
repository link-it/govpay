package it.govpay.bd.model;

import java.util.List;
import java.util.Map;

public class UtenzaOperatore extends Utenza {

	private static final long serialVersionUID = 1L;
	
	private transient String nome;
	public UtenzaOperatore() {
		super();
	}
	
	public UtenzaOperatore(Utenza utenzaBase, String nome, Map<String, List<String>> headers) {
		// dati bd.utenza
		this.aclPrincipal = utenzaBase.aclPrincipal;
		this.aclRuoliEsterni = utenzaBase.aclRuoliEsterni;
		this.aclRuoliUtenza = utenzaBase.aclRuoliUtenza;
		this.dominiUo = utenzaBase.dominiUo;
		this.tipiVersamento = utenzaBase.tipiVersamento;
		this.ruoli = utenzaBase.getRuoli();
		// dati model
		this.id = utenzaBase.getId();
		this.principal = utenzaBase.getPrincipal();
		this.principalOriginale = utenzaBase.getPrincipalOriginale();
		this.abilitato = utenzaBase.isAbilitato();
		this.idDominiUo = utenzaBase.getIdDominiUo();
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
	
	public String getMessaggioUtenzaDisabilitata() {
		StringBuilder sb = new StringBuilder();
		sb.append("Operatore [").append(this.getNome()).append("] disabilitato");
		return sb.toString();
	}
	
	public String getMessaggioUtenzaNonAutorizzata() {
		StringBuilder sb = new StringBuilder();
		sb.append("Operatore [").append(this.getNome()).append("] non autorizzato ad accedere alla risorsa richiesta");
		return sb.toString();
	}
}
