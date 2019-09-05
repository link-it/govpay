/**
 * 
 */
package it.govpay.core.dao.anagrafica.dto;

import java.util.List;

import it.govpay.bd.model.Utenza;
import it.govpay.bd.model.Dominio;
import it.govpay.model.TipoVersamento;

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
	private List<TipoVersamento> tipiVersamento;
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
	public List<TipoVersamento> getTipiVersamento() {
		return tipiVersamento;
	}
	public void setTipiVersamento(List<TipoVersamento> tipiVersamento) {
		this.tipiVersamento = tipiVersamento;
	}
	public String getNome() {
		return this.nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
}
