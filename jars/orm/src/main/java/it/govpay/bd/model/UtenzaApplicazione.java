package it.govpay.bd.model;

public class UtenzaApplicazione extends Utenza {
	
	public UtenzaApplicazione() {
		super();
	}
	
	public UtenzaApplicazione(Utenza utenzaBase, String codApplicazione) {
		// dati bd.utenza
		this.aclPrincipal = utenzaBase.aclPrincipal;
		this.aclRuoli = utenzaBase.aclRuoli;
		this.codApplicazione = codApplicazione;
		this.domini = utenzaBase.domini;
		this.ruoli = utenzaBase.ruoli;
		this.tipiTributo = utenzaBase.tipiTributo;
		// dati model
		this.id = utenzaBase.getId();
		this.principal = utenzaBase.getPrincipal();
		this.principalOriginale = utenzaBase.getPrincipalOriginale();
		this.abilitato = utenzaBase.isAbilitato();
		this.idDomini = utenzaBase.getIdDomini();
		this.idTipiTributo = utenzaBase.getIdTipiTributo();
		this.checkSubject = utenzaBase.isCheckSubject();
		this.autorizzazioneDominiStar = utenzaBase.isAutorizzazioneDominiStar();
		this.autorizzazioneTributiStar = utenzaBase.isAutorizzazioneTributiStar();
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

//	public void setCodApplicazione(String codApplicazione) {
//		this.codApplicazione = codApplicazione;
//	}

}
