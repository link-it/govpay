package it.govpay.bd.model.eventi;

import java.io.Serializable;

import it.govpay.model.Rpt;
import it.govpay.model.Canale.TipoVersamento;

public class Controparte implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public Controparte() {
		this.codPsp = Rpt.codPspWISP20;
		this.codCanale = Rpt.codCanaleWISP20;
		this.tipoVersamento = Rpt.tipoVersamentoWISP20;
	}
	
	private String codPsp;
	private TipoVersamento tipoVersamento;
	private String fruitore;
	private String erogatore;
	private String codStazione;
	private String codCanale;

	public String getCodPsp() {
		return codPsp;
	}
	public void setCodPsp(String codPsp) {
		this.codPsp = codPsp;
	}
	public TipoVersamento getTipoVersamento() {
		return tipoVersamento;
	}
	public void setTipoVersamento(TipoVersamento tipoVersamento) {
		this.tipoVersamento = tipoVersamento;
	}
	public String getFruitore() {
		return fruitore;
	}
	public void setFruitore(String fruitore) {
		this.fruitore = fruitore;
	}
	public String getErogatore() {
		return erogatore;
	}
	public void setErogatore(String erogatore) {
		this.erogatore = erogatore;
	}
	public String getCodStazione() {
		return codStazione;
	}
	public void setCodStazione(String codStazione) {
		this.codStazione = codStazione;
	}
	public String getCodCanale() {
		return codCanale;
	}
	public void setCodCanale(String codCanale) {
		this.codCanale = codCanale;
	}
	
	
}
