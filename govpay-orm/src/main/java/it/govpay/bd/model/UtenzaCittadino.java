package it.govpay.bd.model;

public class UtenzaCittadino extends Utenza {

	public UtenzaCittadino() {
		super();
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
	public String getTipoUtenza() {
		return "cittadino";
	}

	@Override
	public String getIdentificativo() {
		return this.getCodIdentificativo();
	}

}
