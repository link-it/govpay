/**
 * 
 */
package it.govpay.bd.model;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Bussu Giovanni (bussu@link.it)
 * @author  $Author: bussu $
 * @version $ Rev: 12563 $, $Date: 28 giu 2018 $
 * 
 */
public class Nota implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L; 
	
	public static final String UTENTE_SISTEMA = "SISTEMA";
	
	public enum TipoNota {
		NOTA_UTENTE, SISTEMA_FATAL, SISTEMA_WARN, SISTEMA_INFO, ANOMALIA
	}
	
	private String principal = null;
	private String autore = null;
	private Date data = null;
	private String testo = null;
	private TipoNota tipo =null;
	private String oggetto = null;
	
	public String getAutore() {
		return this.autore;
	}
	public void setAutore(String autore) {
		this.autore = autore;
	}
	public Date getData() {
		return this.data;
	}
	public void setData(Date data) {
		this.data = data;
	}
	public String getTesto() {
		return this.testo;
	}
	public void setTesto(String testo) {
		this.testo = testo;
	}
	public TipoNota getTipo() {
		return tipo;
	}
	public void setTipo(TipoNota tipo) {
		this.tipo = tipo;
	}
	public String getOggetto() {
		return oggetto;
	}
	public void setOggetto(String oggetto) {
		this.oggetto = oggetto;
	}
	public String getPrincipal() {
		return principal;
	}
	public void setPrincipal(String principal) {
		this.principal = principal;
	}
	
}
