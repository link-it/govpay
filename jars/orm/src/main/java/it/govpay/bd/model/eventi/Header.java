package it.govpay.bd.model.eventi;

import java.io.Serializable;

public class Header implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public Header() {
	}
	
	public Header(String nome, String valore) {
		this.nome = nome;
		this.valore = valore;
	}
	
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getValore() {
		return valore;
	}
	public void setValore(String valore) {
		this.valore = valore;
	}
	private String nome;
	private String valore;
}
