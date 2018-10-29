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
		this.tributi = utenzaBase.tributi;
		// dati model
		this.id = utenzaBase.getId();
		this.principal = utenzaBase.getPrincipal();
		this.principalOriginale = utenzaBase.getPrincipalOriginale();
		this.abilitato = utenzaBase.isAbilitato();
		this.idDomini = utenzaBase.getIdDomini();
		this.idTributi = utenzaBase.getIdTributi();
		this.checkSubject = utenzaBase.isCheckSubject();
	}

	@Override
	public String getTipoUtenza() {
		return "applicazione";
	}

	@Override
	public String getIdentificativo() {
		return this.getCodApplicazione();
	}

	private static final long serialVersionUID = 1L;
	
	private transient String codApplicazione;

	public String getCodApplicazione() {
		return codApplicazione;
	}

	public void setCodApplicazione(String codApplicazione) {
		this.codApplicazione = codApplicazione;
	}

}
