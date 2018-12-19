/**
 * 
 */
package it.govpay.core.dao.anagrafica.dto;

import java.util.List;

import it.govpay.bd.model.Dominio;
import it.govpay.bd.model.Tributo;
import it.govpay.bd.model.Utenza;

/**
 * @author Bussu Giovanni (bussu@link.it)
 * @author  $Author: bussu $
 * @version $ Rev: 12563 $, $Date: 12 giu 2018 $
 * 
 */
public class LeggiProfiloDTOResponse {

	private String nome;
	private Utenza utente;
	private List<Dominio> domini;
	private List<Tributo> tributi;
	public Utenza getUtente() {
		return this.utente;
	}
	public void setUtente(Utenza utente) {
		this.utente = utente;
	}
	public List<Dominio> getDomini() {
		return this.domini;
	}
	public void setDomini(List<Dominio> domini) {
		this.domini = domini;
	}
	public List<Tributo> getTributi() {
		return this.tributi;
	}
	public void setTributi(List<Tributo> tributi) {
		this.tributi = tributi;
	}
	public String getNome() {
		return this.nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
}
