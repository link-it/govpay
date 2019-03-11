/**
 * 
 */
package it.govpay.core.dao.anagrafica.dto;

import java.util.List;

import it.govpay.bd.model.Dominio;
import it.govpay.bd.model.Utenza;
import it.govpay.model.TipoTributo;

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
	private List<TipoTributo> tipiTributi;
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
	public List<TipoTributo> getTipiTributi() {
		return this.tipiTributi;
	}
	public void setTipiTributi(List<TipoTributo> tributi) {
		this.tipiTributi = tributi;
	}
	public String getNome() {
		return this.nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
}
