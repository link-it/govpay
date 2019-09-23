package it.govpay.bd.model;

public class UtenzaApplicazione extends Utenza {
	
	public UtenzaApplicazione() {
		super();
	}
	
	public UtenzaApplicazione(Utenza utenzaBase, String codApplicazione) {
		// dati bd.utenza
		this.aclPrincipal = utenzaBase.aclPrincipal;
		this.aclRuoliEsterni = utenzaBase.aclRuoliEsterni;
		this.aclRuoliUtenza = utenzaBase.aclRuoliUtenza;
		this.codApplicazione = codApplicazione;
		this.domini = utenzaBase.domini;
		this.ruoli = utenzaBase.getRuoli();
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
	}

	@Override
	public TIPO_UTENZA getTipoUtenza() {
		return TIPO_UTENZA.APPLICAZIONE;
	}

	private static final long serialVersionUID = 1L;
	
	private transient String codApplicazione;

	public String getCodApplicazione() {
		return codApplicazione;
	}
	
	public String getMessaggioUtenzaDisabilitata() {
		StringBuilder sb = new StringBuilder();
		sb.append("Applicazione [").append(this.getCodApplicazione()).append("] disabilitata");
		return sb.toString();
	}
	
	public String getMessaggioUtenzaNonAutorizzata() {
		StringBuilder sb = new StringBuilder();
		sb.append("Applicazione [").append(this.getCodApplicazione()).append("] non autorizzata ad accedere alla risorsa richiesta");
		return sb.toString();
	}

//	public void setCodApplicazione(String codApplicazione) {
//		this.codApplicazione = codApplicazione;
//	}

}
