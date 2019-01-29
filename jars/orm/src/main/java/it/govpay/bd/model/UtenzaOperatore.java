package it.govpay.bd.model;

public class UtenzaOperatore extends Utenza {

	private static final long serialVersionUID = 1L;
	
	private transient String nome;
	
	public UtenzaOperatore() {
		super();
	}
	
	public UtenzaOperatore(Utenza utenzaBase, String nome) {
		// dati bd.utenza
		this.aclPrincipal = utenzaBase.aclPrincipal;
		this.aclRuoli = utenzaBase.aclRuoli;
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
		this.autorizzazioneDominiStar = utenzaBase.isAutorizzazioneDominiStar();
		this.autorizzazioneTributiStar = utenzaBase.isAutorizzazioneTributiStar();
		
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
}
