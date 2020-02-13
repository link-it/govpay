package it.govpay.bd.model;

public class IdUnitaOperativa extends it.govpay.model.IdUnitaOperativa{
	
	private static final long serialVersionUID = 1L;
	
	private String codDominio;
	private String ragioneSociale;
	
	private String codUO;
	private String ragioneSocialeUO;
	
	
	public IdUnitaOperativa() {}
	
	public IdUnitaOperativa (it.govpay.model.IdUnitaOperativa base) {
		this.setId(base.getId()); 
		this.setIdDominio(base.getIdDominio());
		this.setIdUnita(base.getIdUnita());
	}

	public String getCodDominio() {
		return codDominio;
	}

	public void setCodDominio(String codDominio) {
		this.codDominio = codDominio;
	}

	public String getRagioneSociale() {
		return ragioneSociale;
	}

	public void setRagioneSociale(String ragioneSociale) {
		this.ragioneSociale = ragioneSociale;
	}

	public String getCodUO() {
		return codUO;
	}

	public void setCodUO(String codUO) {
		this.codUO = codUO;
	}

	public String getRagioneSocialeUO() {
		return ragioneSocialeUO;
	}

	public void setRagioneSocialeUO(String ragioneSocialeUO) {
		this.ragioneSocialeUO = ragioneSocialeUO;
	}
	
}
