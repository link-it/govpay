/**
 * 
 */
package it.govpay.backoffice.v1.sonde;

/**
 * @author Bussu Giovanni (bussu@link.it)
 * @author  $Author: bussu $
 * @version $ Rev: 12563 $, $Date: 07/mar/2017 $
 * 
 */
public class SommarioSonda {

	private String id;
	private String nome;
	private Integer stato;
	private String descrizioneStato;
	
	public String getNome() {
		return this.nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public Integer getStato() {
		return this.stato;
	}
	public void setStato(Integer stato) {
		this.stato = stato;
	}
	public String getDescrizioneStato() {
		return this.descrizioneStato;
	}
	public void setDescrizioneStato(String descrizioneStato) {
		this.descrizioneStato = descrizioneStato;
	}
	public String getId() {
		return this.id;
	}
	public void setId(String id) {
		this.id = id;
	}
}
