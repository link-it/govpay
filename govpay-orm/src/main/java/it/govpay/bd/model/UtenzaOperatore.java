package it.govpay.bd.model;

public class UtenzaOperatore extends Utenza {

	private static final long serialVersionUID = 1L;
	
	private transient String nome;
	
	public UtenzaOperatore() {
		super();
	}

	@Override
	public String getTipoUtenza() {
		return "operatore";
	}

	@Override
	public String getIdentificativo() {
		return super.getPrincipal();
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

}
