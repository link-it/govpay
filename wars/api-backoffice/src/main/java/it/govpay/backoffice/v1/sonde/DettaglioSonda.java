/**
 * 
 */
package it.govpay.backoffice.v1.sonde;

import java.util.Date;

import org.openspcoop2.utils.sonde.Sonda;

/**
 * @author Bussu Giovanni (bussu@link.it)
 * @author  $Author: bussu $
 * @version $ Rev: 12563 $, $Date: 07/mar/2017 $
 * 
 */
public class DettaglioSonda {
	
	public enum TipoSonda {
		Coda, Batch, Sconosciuto
	}
	private String id;
	private String nome;
	private Integer stato;
	private String descrizioneStato;
	private long durataStatoValue;
	private String durataStato;
	private String sogliaWarn;
	private String sogliaError;
	private long sogliaWarnValue;
	private long sogliaErrorValue;
	private Date dataUltimoCheck;
	private TipoSonda tipo;
	
	public DettaglioSonda(TipoSonda tipo) {
		this.tipo = tipo;
	}
	
	public DettaglioSonda(Class<? extends Sonda> class1) {
		if(class1.equals(org.openspcoop2.utils.sonde.impl.SondaBatch.class)) {
			this.tipo = TipoSonda.Batch;
			return;
		}
		
		if(class1.equals(org.openspcoop2.utils.sonde.impl.SondaCoda.class)) {
			this.tipo = TipoSonda.Coda;
			return;
		}
		
		this.tipo = TipoSonda.Sconosciuto;
	}

	public long getSogliaWarnValue() {
		return this.sogliaWarnValue;
	}
	
	public String getSogliaWarn() {
		return this.sogliaWarn;
	}
	
	public void setSogliaWarn(long sogliaWarn) {
		this.sogliaWarnValue = sogliaWarn;
		
		switch (this.tipo) {
		case Batch:
			this.sogliaWarn = "Lasso di tempo senza esecuzioni con successo: " + this.toString(sogliaWarn);
			break;
		case Coda:
			this.sogliaWarn = "Numero di elementi accodati: " + sogliaWarn;
			break;
		default:
			
			break;
		}
	}
	
	public long getSogliaErrorValue() {
		return this.sogliaErrorValue;
	}
	
	public String getSogliaError() {
		return this.sogliaError;
	}
	
	public void setSogliaError(long sogliaError) {
		this.sogliaErrorValue = sogliaError;
		
		switch (this.tipo) {
		case Batch:
			this.sogliaError = "Lasso di tempo senza esecuzioni con successo: " + this.toString(sogliaError);
			break;
		case Coda:
			this.sogliaError = "Numero di elementi accodati: " + sogliaError;
			break;
		default:
			
			break;
		}
	}
	
	public Date getDataUltimoCheck() {
		return this.dataUltimoCheck;
	}
	public void setDataUltimoCheck(Date dataUltimoCheck) {
		this.dataUltimoCheck = dataUltimoCheck;
	}
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
	
	public String getDurataStato() {
		return this.durataStato;
	}

	public void setDurataStato(Date inizioStato) {
		if(inizioStato != null) {
			this.durataStatoValue = new Date().getTime() - inizioStato.getTime();
			this.durataStato = this.toString(this.durataStatoValue);
		} else {
			this.durataStatoValue = 0l;
			this.durataStato = null;
		}
	}
	
	public TipoSonda getTipo() {
		return this.tipo;
	}
	
	private String toString(long millis) {
		return org.openspcoop2.utils.Utilities.convertSystemTimeIntoString_millisecondi(millis, false);
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
}
