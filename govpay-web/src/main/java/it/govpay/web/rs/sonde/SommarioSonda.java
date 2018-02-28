/**
 * 
 */
package it.govpay.web.rs.sonde;

/**
 * @author Bussu Giovanni (bussu@link.it)
 * @author  $Author: bussu $
 * @version $ Rev: 12563 $, $Date: 07/mar/2017 $
 * 
 */
public class SommarioSonda {

	private String nome;
	private Integer stato;
	private String descrizioneStato;
	
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public Integer getStato() {
		return stato;
	}
	public void setStato(Integer stato) {
		this.stato = stato;
	}
	public String getDescrizioneStato() {
		return descrizioneStato;
	}
	public void setDescrizioneStato(String descrizioneStato) {
		this.descrizioneStato = descrizioneStato;
	}
}
